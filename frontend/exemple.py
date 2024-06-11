import streamlit as st
import networkx as nx
from pyvis.network import Network
import streamlit.components.v1 as components

# Function to generate and visualize the graph
def generate_graph():
    # Create a simple graph using NetworkX
    G = nx.Graph()
    # Adding initial nodes with text
    G.add_node(1, label="Node 1")
    G.add_node(2, label="Node 2")
    G.add_node(3, label="Node 3")
    G.add_node(4, label="Node 4")
    G.add_node(5, label="Node 5")
    G.add_edges_from([(1, 2), (2, 3), (3, 4), (4, 5), (5, 1)])

    return G

# Function to visualize the graph using Pyvis
def visualize_graph(G):
    net = Network(notebook=True)
    for node, data in G.nodes(data=True):
        net.add_node(node, label=data.get('label', str(node)))
    for edge in G.edges():
        net.add_edge(*edge)
    net.save_graph('graph.html')

# Function to load and render the graph
def display_graph():
    with open('graph.html', 'r') as f:
        components.html(f.read(), height=500)

# Streamlit App
st.title("Interactive Graph Visualization")

st.sidebar.title("Graph Controls")

# Generate the initial graph
if 'graph' not in st.session_state:
    st.session_state.graph = generate_graph()

# Add node functionality
add_node_label = st.sidebar.text_input("Node Label")
if st.sidebar.button("Add Node"):
    new_node = len(st.session_state.graph.nodes) + 1
    st.session_state.graph.add_node(new_node, label=add_node_label)
    visualize_graph(st.session_state.graph)
    display_graph()

# Add edge functionality
st.sidebar.write("Enter the nodes to connect:")
node1 = st.sidebar.number_input('Node 1', min_value=1, max_value=len(st.session_state.graph.nodes))
node2 = st.sidebar.number_input('Node 2', min_value=1, max_value=len(st.session_state.graph.nodes))
if st.sidebar.button("Add Edge"):
    st.session_state.graph.add_edge(node1, node2)
    visualize_graph(st.session_state.graph)
    display_graph()

# Display the initial graph
visualize_graph(st.session_state.graph)
display_graph()