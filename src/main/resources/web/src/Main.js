import { Grid, TextField } from "@material-ui/core";
import React, { Component, useState } from "react";
import {
  HashRouter
} from "react-router-dom";
import FlowDiagram from "./New/Components/FlowDiagram/FlowDiagram";
import NodeSidebar from "./New/Components/FlowDiagram/NodeSidebar/NodeSidebar";
import StartElement from "./New/Components/Workflow/Elements/StartElement/StartElement";

const Main = (props) => {
  const [elements, setElements] = useState([])
  const [element, setElement] = useState(null)
  const height = '90vh'

  const getElement = (id) => elements.find(obj => obj.id == id)


  return (
    <HashRouter>
      <Grid container>
        <Grid item xs={10}>
          <div style={{ height: height }}>
            <FlowDiagram elements={elements} onChangeElements={setElements} onAddedElement={setElement} onEditElement={setElement} />
          </div>
        </Grid>
        <Grid item xs={2}>
          <NodeSidebar height={height} />
        </Grid>
      </Grid>
      {element != null &&
        <StartElement open={true} onClose={() => setElement(null)}/>
      }
    </HashRouter>
  );
}


export default Main;
