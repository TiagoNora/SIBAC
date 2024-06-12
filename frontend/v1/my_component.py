import os
import streamlit.components.v1 as components

_component_func = components.declare_component(
    "my_component",
    path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "frontend/build")
)

def my_component(key=None):
    return _component_func(key=key)