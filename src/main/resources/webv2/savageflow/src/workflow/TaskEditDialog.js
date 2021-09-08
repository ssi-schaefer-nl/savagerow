import { Button, Grid, TextField } from "@material-ui/core";
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useEffect, useState } from "react";

const TaskEditDialog = ({ open, handleClose, editTask }) => {
    const [task, setTask] = useState(null)

    useEffect(() => {
        if (editTask != null)
            setTask(editTask)
    }, [editTask])

    if (task == null) return null
    return (
        <Dialog
            open={open}
            onClose={handleClose}
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
                    onChange={(e) => setTask(t => ({ ...t, name: e.target.value }))}
                />
            </DialogTitle>
            <DialogContent style={{ height: '60vh' }}>
                <Grid container direction='row' style={{width: '100%', height: '100%'}}>
                    <Grid item style={{border: '1px solid black'}} xs={2}>
                    </Grid>
                    <Grid item xs={true}>
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">Cancel</Button>
                <Button onClick={handleClose} color="primary" autoFocus>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

export default TaskEditDialog;
