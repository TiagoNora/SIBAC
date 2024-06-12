import streamlit as st
from collections import defaultdict
import networkx as nx
from pyvis.network import Network
import streamlit.components.v1 as components
import json

# Function to read and parse the factos.txt file
def parse_factos(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    fact_dict = defaultdict(list)
    current_section = None

    for line in lines:
        line = line.strip()
        if line.endswith("Facts:"):
            current_section = line.replace(":", "")
        elif line and current_section:
            fact_dict[current_section].append(line)

    return fact_dict

# Function to create a network graph from facts
def create_graph(facts):
    G = nx.Graph()
    for section, facts_list in facts.items():
        section_node = f"{section}"
        G.add_node(section_node, label=section, color='lightblue')
        previous_fact_node = None
        for fact in facts_list:
            fact_node = f"{section}: {fact}"
            G.add_node(fact_node, label=fact, color='lightgreen')
            if previous_fact_node is None:
                G.add_edge(section_node, fact_node)
            else:
                G.add_edge(previous_fact_node, fact_node)
            previous_fact_node = fact_node
    return G

# Function to display the graph using pyvis
def display_graph(G):
    net = Network(height='800px', width='100%', bgcolor='#222222', font_color='white')

    # Adjusting physics settings
    physics_settings = {
        "enabled": True,
        "barnesHut": {
            "gravitationalConstant": -8000,
            "centralGravity": 0.3,
            "springLength": 95,
            "springConstant": 0.04,
            "damping": 0.09,
            "avoidOverlap": 0.1
        },
        "minVelocity": 0.75
    }
    net.set_options(json.dumps({"physics": physics_settings}))

    net.from_nx(G)
    net.save_graph('factos_graph.html')

    # Add JavaScript for node click events
    js_code = '''
    <script type="text/javascript">
        function onNodeClick(properties) {
            var clickedNodeId = properties.nodes[0];
            if (clickedNodeId) {
                var nodeInfo = nodes.get(clickedNodeId).label;
                window.dispatchEvent(new CustomEvent("nodeClick", { detail: nodeInfo }));
            }
        }

        var nodes = new vis.DataSet(network.body.data.nodes.get());
        network.on("click", onNodeClick);
    </script>
    '''
    with open('factos_graph.html', 'a', encoding='utf-8') as f:
        f.write(js_code)

    with open('factos_graph.html', 'r', encoding='utf-8') as f:
        html_content = f.read()
    components.html(html_content, height=800)

# Path to the factos.txt file
file_path = "information/factos.txt"

# Parse the factos file
fact_dict = parse_factos(file_path)

# Create a network graph from the parsed facts
graph = create_graph(fact_dict)

# Streamlit app title
st.title("Medical Facts Node Structure")

# Display the network graph
display_graph(graph)

# Custom Streamlit component to capture JavaScript events
selected_node_info = components.declare_component("my_component", url="http://localhost:3001")

# Capture the selected node information from JavaScript
node_info = selected_node_info()

if node_info:
    st.write(f"Selected Node: {node_info}")
