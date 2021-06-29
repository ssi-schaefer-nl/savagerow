
import React, { useEffect, useState } from "react";

import { CircularProgress, Divider, Grid, Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import Button from '@material-ui/core/Button';
import WorkflowService from "../../../../Service/WorkflowService/WorkflowService";
import AddIcon from '@material-ui/icons/Add';
import AddSimpleWorkflow from "./AddSimpleWorkflow/AddSimpleWorkflow";
import FullscreenDialog from "../../../../Components/FullscreenDialog/FullscreenDialog";
import EditIcon from '@material-ui/icons/MoreVert';
import { MenuItem } from "react-contextmenu";
import ErrorMessage from "../../../../Service/ErrorMessages/ErrorMessages";
import CollapsableAlert from "../../../../Components/CollapsableAlert/CollapsableAlert";


const TriggeredWorkflow = (props) => {
    const [workflows, setWorkflows] = useState([])
    const [anchorEditMenu, setAnchorEditMenu] = useState(null);
    const [selectedWorkflow, setSelectedWorkflow] = useState(null);
    const [existingWorkflow, setExistingWorkflow] = useState(null);
    const [openWorkflowDialog, setOpenWorkflowDialog] = useState(false)
    const [error, setError] = useState(false)
    const [loading, setLoading] = useState(true)
    const [reloadTrigger, setReloadTrigger] = useState(false)
    const workflowService = new WorkflowService()

    const triggerReload = () => setReloadTrigger(t => !t)

    useEffect(() => workflowService.getTriggeredWorkflows((data) => {
        setWorkflows(data);
        setLoading(false);
        setError(false)
    }, () => {
        setError(true)
        setLoading(false)
    }), [reloadTrigger])


    const handleAddedSimpleWorkflow = () => {
        setOpenWorkflowDialog(false)
        setSelectedWorkflow(null)
        triggerReload()
        setExistingWorkflow(null)
    }


    const handleClickEdit = (event, indexOfWorkflow) => {
        setAnchorEditMenu(event.currentTarget);
        setSelectedWorkflow(workflows[indexOfWorkflow])
    }


    const handleDelete = () => {
        setAnchorEditMenu(null);


        workflowService.deleteTriggeredWorkflow(selectedWorkflow, triggerReload, () => undefined)
    };

    const handleEdit = () => {
        setAnchorEditMenu(null);
        setExistingWorkflow(selectedWorkflow)
        setOpenWorkflowDialog(true)
    }

    const handleChangeActive = () => {
        setAnchorEditMenu(null);

        const table = selectedWorkflow.table
        const type = selectedWorkflow.type
        const name = selectedWorkflow.name

        workflowService.changeActive(table, type, name, !selectedWorkflow.active, triggerReload, () => undefined)
    };


    if (loading) return <CircularProgress />
    if (error) return <CollapsableAlert severity="error" message={ErrorMessage.Workflow.Loading()} />


    return (
        <div>
            {workflows.length > 0
                ?
                <>
                    <TableContainer component={Paper} style={{ maxHeight: "50vh" }}>
                        <Table stickyHeader >
                            <TableHead >
                                <TableRow>
                                    <TableCell>Table</TableCell>
                                    <TableCell align="right">Workflow Name</TableCell>
                                    <TableCell align="right">Type</TableCell>
                                    <TableCell align="right">Number of actions</TableCell>
                                    <TableCell align="right">Active</TableCell>
                                    <TableCell align="right">
                                        <Button onClick={() => setOpenWorkflowDialog(true)}>
                                            <AddIcon />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {workflows.map((w, i) => (
                                    <TableRow key={`${w.table}-${w.name}`}>
                                        <TableCell component="th" scope="row">{w.table}</TableCell>
                                        <TableCell align="right">{w.name}</TableCell>
                                        <TableCell align="right">{w.type}</TableCell>
                                        <TableCell align="right">{w.actions.length}</TableCell>
                                        <TableCell align="right">{w.active ? "Yes" : "No"}</TableCell>
                                        <TableCell align="right">
                                            <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => handleClickEdit(e, i)}>
                                                <EditIcon />
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))}

                            </TableBody>

                        </Table>
                    </TableContainer>
                    <Menu
                        id="simple-menu"
                        anchorEl={anchorEditMenu}
                        keepMounted
                        open={Boolean(anchorEditMenu)}
                        onClose={() => setAnchorEditMenu(null)}
                    >
                        <MenuItem onClick={() => handleEdit()}>Edit</MenuItem>
                        <MenuItem onClick={() => { handleDelete() }}>Delete</MenuItem>
                        <Divider />
                        <MenuItem onClick={() => { handleChangeActive() }}>{selectedWorkflow != null && selectedWorkflow.active ? "Deactivate" : "Activate"}</MenuItem>
                    </Menu>
                </>
                :
                <Grid container justify="center">
                    <Grid item>
                        <Button variant="contained" color="primary" style={{ width: "15em" }} onClick={() => setOpenWorkflowDialog(true)}>Create simple workflow</Button>
                    </Grid>
                </Grid>
            }
            <FullscreenDialog
                open={openWorkflowDialog}
                handleClose={() => { setExistingWorkflow(null); setOpenWorkflowDialog(false) }}
                title={Boolean(selectedWorkflow) ? "Edit existing workflow" : "Add a new simple workflow"}
            >
                <AddSimpleWorkflow existing={existingWorkflow} onFinish={handleAddedSimpleWorkflow} />
            </FullscreenDialog>

        </div >
    )
}

export default TriggeredWorkflow