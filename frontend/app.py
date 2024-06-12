import streamlit as st
import networkx as nx
from pyvis.network import Network
import streamlit.components.v1


# Function to read and parse the file
def parse_factos_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    exams = {}
    current_exam = None
    current_facts = []

    patient_name = ''
    patient_age = ''
    patient_condition = ''

    for line in lines:
        stripped_line = line.strip()
        if not stripped_line:
            continue

        if ' Facts:' in stripped_line:
            if current_exam is not None:
                exams[current_exam] = current_facts
            current_exam = stripped_line.replace(' Facts:', '')
            current_facts = []
        elif 'Name: ' in stripped_line:
            patient_name = stripped_line.replace('Name: ', '')
        elif 'Age: ' in stripped_line:
            patient_age = stripped_line.replace('Age: ', '')
        elif 'Physical Condition: ' in stripped_line:
            patient_condition = stripped_line.replace('Physical Condition: ', '')

        else:
            current_facts.append(stripped_line)

    if current_exam is not None:
        exams[current_exam] = current_facts

    return exams, patient_name, patient_age, patient_condition

# Function to create a knowledge graph
def create_knowledge_graph(exam_data):
    G = nx.DiGraph()

    for exam, facts in exam_data.items():
        previous_node = exam
        for i, fact in enumerate(facts):
            # Add node for each fact
            G.add_node(fact)
            # Add edge from previous node to current fact
            G.add_edge(previous_node, fact, label=f"{i + 1}")
            previous_node = fact

    return G

# Streamlit app
st.title("Exam Knowledge Graph")

# Path to the factos.txt file
factos_file_path = 'information/factos.txt'

# Parse the file
exams_data, patient_name, patient_age, patient_condition = parse_factos_file(factos_file_path)

# Sidebar for selecting the exam and displaying patient information
selected_exam = st.sidebar.selectbox("Select Exam", list(exams_data.keys()))

# Display patient information
st.sidebar.subheader("Patient Information")
st.sidebar.markdown(f"Name: {patient_name}")
st.sidebar.markdown(f"Age: {patient_age}")
st.sidebar.markdown(f"Physical Condition: {patient_condition}")

# Create knowledge graph for the selected exam
graph = create_knowledge_graph({selected_exam: exams_data[selected_exam]})

# Visualize knowledge graph
st.header(f"Knowledge Graph for {selected_exam}")

# Draw the graph with interactive nodes
net = Network(height="600px", width="100%", notebook=True)
net.from_nx(graph)

# Save the graph as HTML file
net.show("graph.html")

# Display the graph content using HTML component
st.subheader("Graph Content")
html_content = open("graph.html", "r").read()
st.components.v1.html(html_content, width=800, height=600, scrolling=True)