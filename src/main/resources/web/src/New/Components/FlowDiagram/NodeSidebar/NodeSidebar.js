import { Grid } from "@material-ui/core";
import { grey } from "@material-ui/core/colors";
import React from "react";
import DecisionPointShape from "../Nodes/DecisionPoint/DecisionPointShape";
import TaskShape from "../Nodes/Task/TaskShape";
import WorkflowEndShape from "../Nodes/WorkflowEnd/WorkflowEndShape";
import WorkflowTriggerShape from "../Nodes/WorkflowTrigger/WorkflowTriggerShape";


const NodeSidebar = (props) => {
    const onDragStart = (event, nodeType) => {
        event.dataTransfer.setData('application/reactflow', nodeType);
        event.dataTransfer.effectAllowed = 'move';
    }


    return (
        <Grid container direction='column' spacing={1} style={{
            backgroundColor: grey[50],
            padding: '1em',
            height: props.height,
        }}>
            <Grid item onDragStart={(event) => onDragStart(event, 'Workflow Trigger')} draggable>
                <WorkflowTriggerShape text="Workflow Trigger" />
            </Grid>
            <Grid item onDragStart={(event) => onDragStart(event, 'Decision Point')} draggable>
                <DecisionPointShape text="Decision Point" />
            </Grid>
            <Grid item onDragStart={(event) => onDragStart(event, 'Task')} draggable>
                <TaskShape text="Task" />
            </Grid>
            <Grid item onDragStart={(event) => onDragStart(event, 'Workflow End')} draggable>
                <WorkflowEndShape text="Workflow End" />
            </Grid>
        </Grid>
    );
}

export default NodeSidebar;
