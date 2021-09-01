import { Button, Grid, TextField } from "@material-ui/core";
import React, { Component, useState } from "react";
import {
  HashRouter
} from "react-router-dom";
import FlowDiagram from "./New/Components/FlowDiagram/FlowDiagram";
import NodeSidebar from "./New/Components/FlowDiagram/NodeSidebar/NodeSidebar";
import PopupWindow from "./New/Components/PopupWindow/PopupWindow";
import ConfigurationDialogSwitch from "./New/Components/Workflow/Configurations/ConfigurationDialogSwitch";

const Main = (props) => {
  const [elements, setElements] = useState([])
  const [editElement, setEditElement] = useState(null)
  const height = '90vh'

  return (
    <HashRouter>
      <div style={{ height: height }}>
        <FlowDiagram elements={elements} onChangeElements={setElements} onEditElement={setEditElement} />
      </div>

      {editElement != null &&
        <PopupWindow open={editElement != null} onClose={() => setEditElement(null)} title={"Edit Configuration"} wide>
          {ConfigurationDialogSwitch(editElement)}
        </PopupWindow>
      }
    </HashRouter>
  );
}

export default Main;
