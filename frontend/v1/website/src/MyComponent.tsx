import React from "react";
import { Streamlit, withStreamlitConnection } from "streamlit-component-lib";

const MyComponent = () => {
  const onNodeClick = (nodeInfo: string) => {
    Streamlit.setComponentValue(nodeInfo);
  };

  React.useEffect(() => {
    // Add event listener to handle node click events
    window.addEventListener("nodeClick", (event: CustomEvent) => {
      onNodeClick(event.detail);
    });
  }, []);

  return <div>Click a node to see its information in Streamlit.</div>;
};

export default withStreamlitConnection(MyComponent);
