import { Streamlit } from "streamlit-component-lib";
import MyComponent from "./MyComponent";
import React from "react";
import ReactDOM from "react-dom";

ReactDOM.render(
  <React.StrictMode>
    <MyComponent />
  </React.StrictMode>,
  document.getElementById("root")
);

// Sample implementation of the custom event dispatch
document.addEventListener("DOMContentLoaded", function () {
  const graphContainer = document.getElementById("graph-container");

  if (graphContainer) {
    // This is a sample; replace it with your actual node click logic
    graphContainer.addEventListener("click", function () {
      const nodeInfo = "Sample Node Information"; // Replace with actual node data
      window.dispatchEvent(new CustomEvent("nodeClick", { detail: nodeInfo }));
    });
  }
});
