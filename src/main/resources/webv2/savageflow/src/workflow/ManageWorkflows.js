import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField, Typography } from "@material-ui/core";
import { useEffect, useState } from "react";
import WorkflowList from "./WorkflowList";
import WorkflowService from "./WorkflowService";

const ManageWorkflows = ({onEdit}) => {
    const [workflow, setWorkflow] = useState([])
    const [load, setLoad] = useState(false)
    const [toDelete, setToDelete] = useState(null)
    const [createNew, setCreateNew] = useState(false)

    const workflowService = new WorkflowService()

    useEffect(() => new WorkflowService().getAll(setWorkflow, console.log), [load])
    const deleteWorkflow = () => {
        workflowService.delete(toDelete, () => {
            setLoad(l => !l)
            setToDelete(null)
        }, () => undefined)
    }

    const createNewWorkflow = (name) => {
        workflowService.createNew(name, () => {
            setCreateNew(false)
            setLoad(l => !l)
        }, console.log)
    }

    const toggleWorkflow = (id) => {
        let wf = workflow.find(w => w.id === id)
        if (wf != undefined) {
            wf.enabled = !wf.enabled
            workflowService.update(wf, () => setLoad(l => !l), console.log)
        }
    }

    return (
        <div>

            <Button variant="outlined" onClick={() => setCreateNew(true)}>Create Workflow</Button>
            <WorkflowList
                workflows={workflow}
                onDelete={setToDelete}
                onEdit={onEdit}
                onToggle={toggleWorkflow}
            />

            <ConfirmDeletePopup open={Boolean(toDelete)} handleClose={() => setToDelete(null)} onConfirm={deleteWorkflow} />
            <CreateNewPopup open={createNew} handleClose={() => setCreateNew(false)} onCreate={createNewWorkflow} />
        </div>
    )
}

const ConfirmDeletePopup = ({ open, handleClose, onConfirm }) => {
    return (
        <Dialog onClose={handleClose} aria-labelledby="simple-dialog-title" open={open}>
            <DialogTitle>Confirm</DialogTitle>
            <DialogContent>Do you really want to delete this workflow?</DialogContent>
            <DialogActions>
                <Button variant="contained" color="secondary" onClick={onConfirm}>Yes</Button>
                <Button variant="contained" onClick={handleClose} >No</Button>
            </DialogActions>

        </Dialog>
    )
}

const CreateNewPopup = ({ open, handleClose, onCreate }) => {
    const [name, setName] = useState(null)

    const close = () => {
        handleClose()
        setName(null)
    }

    const create = () => {
        onCreate(name)
        setName(null)
    }

    return (
        <Dialog onClose={handleClose} aria-labelledby="simple-dialog-title" open={open}>
            <DialogTitle>Create a new workflow</DialogTitle>
            <DialogContent>
                <Typography style={{ margin: '2em 0' }}>Specify a name for the new workflow</Typography>
                <TextField placeholder="Workflow name" value={name} onChange={(e) => setName(e.target.value)} />
            </DialogContent>
            <DialogActions>
                <Button variant="contained" color="primary" onClick={create}>Create</Button>
                <Button variant="contained" onClick={close} >Cancel</Button>
            </DialogActions>

        </Dialog>
    )
}


export default ManageWorkflows