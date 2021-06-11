import React, { useEffect } from "react";

import { Button, Divider, Menu, MenuItem, Toolbar, Typography } from "@material-ui/core";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import EditIcon from '@material-ui/icons/MoreVert';
import AddIcon from '@material-ui/icons/Add';
import FullscreenDialog from "../../../../Components/FullscreenDialog/FullscreenDialog";
import AddSimpleWorkflow from "./AddWorkflow/AddSimpleWorkflow/AddSimpleWorkflow";
import WorkflowService from "../../../../Service/WorkflowService/WorkflowService";
import { useHistory, useParams } from "react-router"

const ManageWorkflows = (props) => {
    const workflowService = new WorkflowService()
    const { type, onChange } = props
    const { table } = useParams();
    const history = useHistory()

    const [anchorEditMenu, setAnchorEditMenu] = React.useState(null);
    const [anchorAddMenu, setAnchorAddMenu] = React.useState(null);
    const [selectedWorkflow, setSelectedWorkflow] = React.useState(null);
    const [workflowType, setWorkflowType] = React.useState(null)
    const [summary, setSummary] = React.useState(null)

    useEffect(() => (workflowService.getTableWorkflows(table, type, (data) => setSummary(data), () => undefined)), [])

    const handleClick = (event, indexOfWorkflow) => {
        setAnchorEditMenu(event.currentTarget);
        setSelectedWorkflow(summary[indexOfWorkflow])
        console.log(summary[indexOfWorkflow])
    };

    const handleAddedSimpleWorkflow = () => {
        setWorkflowType(null)
        workflowService.getTableWorkflows(table, type, (data) => setSummary(data))
        onChange()
    }

    const handleClose = () => {
        setAnchorEditMenu(null);
        setAnchorAddMenu(null);
    };

    const handleDelete = () => {
        const table = selectedWorkflow.table
        const type = selectedWorkflow.type
        const name = selectedWorkflow.name

        workflowService.deleteWorkflow(table, type, name,
            () => setSummary(s => s.filter(w => JSON.stringify(w) !== JSON.stringify(selectedWorkflow))),
            () => undefined)
    };

    const handleChangeActive = () => {
        const table = selectedWorkflow.table
        const type = selectedWorkflow.type
        const name = selectedWorkflow.name

        workflowService.changeActive(table, type, name, !selectedWorkflow.active,
            () => workflowService.getTableWorkflows(table, type, (data) => setSummary(data), () => undefined),
            () => undefined)
    };

if (summary == null) return null // must signal loading


return (
    <div>
        <Toolbar style={{ justifyContent: "space-between" }}>
            <Typography variant="h6">{type.charAt(0).toUpperCase() + type.slice(1)} Workflows ({table})</Typography>
            <Button variant="contained" onClick={() => history.goBack()}>Back</Button>
        </Toolbar>
        <TableContainer component={Paper} style={{ maxHeight: "50vh" }}>
            <Table stickyHeader >
                <TableHead >
                    <TableRow>
                        <TableCell>Workflow name</TableCell>
                        <TableCell align="right">Active</TableCell>
                        <TableCell align="right">
                            <Button aria-controls="add-workflow" aria-haspopup="true" onClick={(e) => setAnchorAddMenu(e.currentTarget)}>
                                <AddIcon />
                            </Button>
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {summary.map((data, i) => (
                        <TableRow key={data.name}>

                            <TableCell component="th" scope="row">{data.name}</TableCell>
                            <TableCell align="right">{data.active ? "Yes" : "No"}</TableCell>
                            <TableCell align="right">
                                <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => handleClick(e, i)}>
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
            onClose={handleClose}
        >
            <MenuItem disabled onClick={handleClose}>Edit</MenuItem>
            <MenuItem onClick={() => { handleClose(); handleDelete(); }}>Delete</MenuItem>
            <Divider />
            <MenuItem onClick={() => {handleClose(); handleChangeActive();} }>{selectedWorkflow != null && selectedWorkflow.active ? "Deactivate" : "Activate"}</MenuItem>
        </Menu>
        <Menu
            id="add-workflow"
            anchorEl={anchorAddMenu}
            keepMounted
            open={Boolean(anchorAddMenu)}
            onClose={handleClose}
        >
            <Typography style={{ margin: "0.5em 1em" }}><strong>Add Workflow</strong></Typography>
            <Divider />
            <MenuItem onClick={() => { setWorkflowType("simple"); handleClose(); }}>Simple Workflow</MenuItem>
            <MenuItem disabled onClick={() => { setWorkflowType("advanced"); handleClose(); }}>Advanced Workflow</MenuItem>
        </Menu>

        <FullscreenDialog
            open={Boolean(workflowType)}
            handleClose={() => setWorkflowType(null)}
            title={"Add a new workflow".concat(workflowType === "advanced" ? ' (advanced)' : '')}
        >
            {workflowType === "simple"
                ? <AddSimpleWorkflow table={table} type={type} onFinish={handleAddedSimpleWorkflow} />
                : <p> not implemented </p>
            }
        </FullscreenDialog>
    </div >
)

}

export default ManageWorkflows