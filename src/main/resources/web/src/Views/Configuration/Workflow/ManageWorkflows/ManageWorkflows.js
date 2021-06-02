import React, { useEffect } from "react";

import { Button, Divider, Menu, MenuItem } from "@material-ui/core";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import EditIcon from '@material-ui/icons/MoreVert';
import AddIcon from '@material-ui/icons/Add';
import FullscreenDialog from "../../../../Components/FullscreenDialog/FullscreenDialog";
import AddSimpleWorkflow from "./AddWorkflow/AddSimpleWorkflow/AddSimpleWorkflow";
import Workflow from "../Workflow";
import WorkflowService from "../../../../Service/WorkflowService/WorkflowService";

const ManageWorkflows = (props) => {
    const workflowService = new WorkflowService()
    const { table, type } = props
    const [anchorEditMenu, setAnchorEditMenu] = React.useState(null);
    const [anchorAddMenu, setAnchorAddMenu] = React .useState(null);
    const [selectedWorkflow, setSelectedWorkflow] = React.useState(0);
    const [workflowType, setWorkflowType] = React.useState(null)
    const [summary, setSummary] = React.useState([])

    useEffect(() => {
        workflowService.getTableSummary(table, type, (data) => setSummary(data[0].workflows), () => undefined);
    }, [])

    const handleClick = (event, indexOfWorkflow) => {
        setAnchorEditMenu(event.currentTarget);
        setSelectedWorkflow(indexOfWorkflow)
    };

    const handleClose = () => {
        setAnchorEditMenu(null);
        setAnchorAddMenu(null);
    };
    console.log(summary)
    if(summary.length == 0) return null


    return (
        <div>
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
                <MenuItem onClick={handleClose}>Edit</MenuItem>
                <MenuItem onClick={handleClose}>Delete</MenuItem>
                <Divider />
                <MenuItem onClick={handleClose}>{summary[selectedWorkflow].active ? "Deactivate" : "Activate"}</MenuItem>
            </Menu>
            <Menu
                id="add-workflow"
                anchorEl={anchorAddMenu}
                keepMounted
                open={Boolean(anchorAddMenu)}
                onClose={handleClose}
            >
                <MenuItem disabled>Add Workflow</MenuItem>
                <MenuItem onClick={() => { setWorkflowType("simple"); handleClose(); }}>Simple Workflow</MenuItem>
                <MenuItem onClick={() => { setWorkflowType("advanced"); handleClose(); }}>Advanced Workflow</MenuItem>
            </Menu>
            <FullscreenDialog open={Boolean(workflowType)} handleClose={() => setWorkflowType(null)} title={"Add a new workflow".concat(workflowType === "advanced" ? ' (advanced)' : '')} >
                {workflowType === "simple" ? <AddSimpleWorkflow/> : <p> not implemented </p> }
            </FullscreenDialog>
        </div >
    )

}

export default ManageWorkflows