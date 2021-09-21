import { Button, Grid, ListSubheader, TextField, Typography } from "@material-ui/core";
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import FileCopyOutlinedIcon from '@material-ui/icons/FileCopyOutlined';
import { useEffect, useState } from "react";
import CrudTaskForm from "./CrudTaskForms";
import DecisionTaskForms from "./DecisionTaskForms";
import WorkflowService from "./WorkflowService";
import FastForwardIcon from '@material-ui/icons/FastForward';

const TaskEditDialog = ({ open, handleClose, task, onChange, onSave, workflow }) => {
    const [input, setInput] = useState(null)

    useEffect(() => {
        new WorkflowService().getTaskInput(workflow.id, task.id, setInput, () => setInput(null))
    }, [])

    const handlePropagate = (original) => {
        const t = { ...task }
        if (t.propagatedParameters != null && Object.keys(t.propagatedParameters).includes(original)) {
            delete t.propagatedParameters[original]
        }
        else {
            if (t.propagatedParameters == null) t.propagatedParameters = {}
            t.propagatedParameters[original] = `${task.id}.${original}`
        }
        onChange(t)
    }

    return (
        <Dialog
            open={open}
            fullWidth
            maxWidth="lg"
        >
            <DialogTitle>
                <TextField
                    InputProps={{
                        style: { fontSize: "1.5em" }
                    }}
                    value={task.name}
                    fullWidth
                    onChange={(e) => onChange(({ ...task, name: e.target.value }))}
                />
            </DialogTitle>
            <DialogContent style={{ height: '60vh' }}>
                <Grid container direction='row' style={{ width: '100%', height: '90%' }}>
                    <Grid item style={{ borderRight: '1px solid grey', maxHeight: '100%', overflow: 'auto' }} xs={3}>
                        <InputParamList params={input} onPropagate={handlePropagate} propagated={task.propagatedParameters} />
                    </Grid>
                    <Grid item xs={true}>
                        <WorkflowTaskEditor task={task} onChange={onChange} workflow={workflow} />
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">Cancel</Button>
                <Button onClick={onSave} color="primary" autoFocus>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

const WorkflowTaskEditor = ({ task, onChange, workflow }) => {
    switch (task.taskType) {
        case "crud": return <CrudTaskForm task={task} onChange={onChange} />
        case "decision": return <DecisionTaskForms workflow={workflow} task={task} onChange={onChange} />
        default: return null;
    }
}

const InputParamList = ({ params, onPropagate, propagated }) => {
    if (params == null) return "Unable to fetch input parameters"
    return (
        <>
            <Typography variant="subtitle"><strong>Input Parameters</strong></Typography>
            <List
                dense
                small
            >
                {Object.keys(params).map(p =>
                    <InputParamListItem
                        name={p}
                        onPropagate={onPropagate}
                        propagated={propagated && Object.keys(propagated).includes(p)}
                        placeholder={params[p]}
                    />
                )}
            </List>
        </>
    );
}

const InputParamListItem = ({ name, placeholder, onPropagate, propagated }) => {

    return (
        <>
            <ListItem style={{ backgroundColor: "white", margin: '0.5em 0' }} >
                <ListItemText primary={name} />
                <ListItemSecondaryAction>
                    <IconButton color={propagated ? "primary" : "default"} edge="end" onClick={() => onPropagate(name)}>
                        <FastForwardIcon />
                    </IconButton>

                    <IconButton edge="end" onClick={() => { navigator.clipboard.writeText(placeholder) }}>
                        <FileCopyOutlinedIcon />
                    </IconButton>
                </ListItemSecondaryAction>

            </ListItem>
        </>
    );
}


export default TaskEditDialog;