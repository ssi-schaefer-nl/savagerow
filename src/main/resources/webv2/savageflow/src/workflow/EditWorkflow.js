import { Button, CircularProgress, Grid, Typography } from "@material-ui/core";
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useEffect, useState } from "react";
import FlowDiagram from "./FlowDiagram";
import TaskEditDialog from "./TaskEditDialog";
import WorkflowService from "./WorkflowService";

const EditWorkflow = ({ workflowId, onCancel }) => {
    const [workflow, setWorkflow] = useState(null)
    const [loadingFailure, setLoadingFailure] = useState(false)
    const [editTaskId, setEditTaskId] = useState(null)

    useEffect(() => {
        new WorkflowService().get(workflowId, (w) => setWorkflow(w), () => setLoadingFailure(true))
    }, [])

    window.onbeforeunload = (event) => {
        const e = event || window.event;
        e.preventDefault();
        if (e) e.returnValue = '';
        return '';
    };


    const content = () => {
        if (workflow != null) return (
            <Grid item xs={12}>
                <FlowDiagram workflow={workflow} onClickNode={setEditTaskId} />
            </Grid>
        )
        else if (loadingFailure) return (
            <Grid item style={{ height: "50vh" }} container alignItems='center' justifyContent='center'>
                <Typography color="error">Error loading the workflow. Please try again.</Typography>
            </Grid>
        )
        else return (
            <Grid item style={{ height: "50vh" }} container alignItems='center' justifyContent='center'>
                <CircularProgress />
            </Grid>
        )
    }

    return (
        <Grid container spacing={1}>
            <Grid item container direction='row' spacing={2}>
                <Grid item>
                    <Button variant="outlined" onClick={onCancel}>Cancel</Button>
                </Grid>
                <Grid item>
                    <Button variant="outlined">Save</Button>
                </Grid>
            </Grid>
            {content()}
            {editTaskId &&
                <TaskEditDialog open={Boolean(editTaskId)} editTask={workflow && workflow.tasks.find(t => t.id == editTaskId)} handleClose={() => setEditTaskId(null)} />
            }

        </Grid>
    )

}

export default EditWorkflow