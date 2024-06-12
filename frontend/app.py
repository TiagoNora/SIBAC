import streamlit as st
import networkx as nx
import matplotlib.pyplot as plt
import io

# Function to read and parse the file
def parse_factos_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()
    
    exams = {}
    current_exam = None
    current_facts = []
    
    for line in lines:
        stripped_line = line.strip()
        if not stripped_line:
            continue
        
        if ' Facts:' in stripped_line:
            if current_exam is not None:
                exams[current_exam] = current_facts
            current_exam = stripped_line.replace(' Facts:', '')
            current_facts = []
        else:
            current_facts.append(stripped_line)
    
    if current_exam is not None:
        exams[current_exam] = current_facts

    return exams

# Function to create a knowledge graph
def create_knowledge_graph(exam_data):
    G = nx.DiGraph()

    for exam, facts in exam_data.items():
        previous_node = exam
        for fact in facts:
            # Add node for each fact
            G.add_node(fact)
            # Add edge from previous node to current fact
            G.add_edge(previous_node, fact)
            # Update previous node
            previous_node = fact

    return G

# Streamlit app
st.title("Exam Knowledge Graph")

# Path to the factos.txt file
factos_file_path = 'information/factos.txt'

# Parse the file
exams_data = parse_factos_file(factos_file_path)

# Sidebar for selecting the exam and displaying patient information
selected_exam = st.sidebar.selectbox("Select Exam", list(exams_data.keys()))

# Display patient information
st.sidebar.subheader("Patient Information")
st.sidebar.markdown("Name: Paula Mendes")
st.sidebar.markdown("Age: 64")
st.sidebar.markdown("Physical Condition: 0")

# Create knowledge graph for the selected exam
graph = create_knowledge_graph({selected_exam: exams_data[selected_exam]})

# Visualize knowledge graph
st.header(f"Knowledge Graph for {selected_exam}")

# Draw the graph with improved layout
pos = nx.spring_layout(graph, k=2, iterations=50)  # Adjust k and iterations for better spacing
fig, ax = plt.subplots(figsize=(12, 8))  # Increase the figure size
nx.draw(graph, pos, ax=ax, with_labels=True, node_size=1500, node_color="skyblue", font_size=12, arrowsize=20)
plt.title(selected_exam)
plt.tight_layout()

# Convert matplotlib figure to an image
buf = io.BytesIO()
plt.savefig(buf, format='png')
plt.close(fig)
buf.seek(0)

# Display the image in Streamlit
st.image(buf)

# Display questions and answers in an interesting way
st.header(f"Questions and Answers for {selected_exam}")
for fact in exams_data[selected_exam]:
    if '?' in fact:
        st.markdown(f"**Question:** {fact}")
    else:
        st.markdown(f"*Answer:* {fact}")
