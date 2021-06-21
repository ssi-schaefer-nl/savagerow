import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';

import { Divider, Grid, makeStyles, Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";
import Button from '@material-ui/core/Button';
import WorkflowService from "../../../Service/WorkflowService/WorkflowService";
import AddIcon from '@material-ui/icons/Add';
import AddSimpleWorkflow from "./ManageWorkflows/AddWorkflow/AddSimpleWorkflow/AddSimpleWorkflow";
import FullscreenDialog from "../../../Components/FullscreenDialog/FullscreenDialog";
import EditIcon from '@material-ui/icons/MoreVert';
import { MenuItem } from "react-contextmenu";


const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
}));

const Workflow = (props) => {
    const classes = useStyles();
    const workflowService = new WorkflowService()

    const [expanded, setExpanded] = React.useState("simple-workflows");

    const handleChange = (panel) => (event, isExpanded) => (setExpanded(isExpanded ? panel : false));


    return (
        <>
            <Typography variant="h6" color="primary" style={{ margin: "1em 0" }}>Workflows</Typography>

            <AccordionSection onChange={handleChange("simple-workflows")} expanded={expanded === "simple-workflows"} title="Simple Workflows">
                <SimpleWorkflows />
            </AccordionSection>

        </>
    )
}

const AccordionSection = (props) => {
    const classes = useStyles();

    return (
        <Accordion expanded={props.expanded} onChange={props.onChange}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
                style={{ backgroundColor: grey[100] }}
            >
                <Typography className={classes.heading}>{props.title}</Typography>
            </AccordionSummary>
            <AccordionDetails style={{ flexDirection: "column", padding: "1em" }}>
                {props.children}
            </AccordionDetails>
        </Accordion>
    )
}

const SimpleWorkflows = (props) => {
    const [workflows, setWorkflows] = useState([])
    const [anchorEditMenu, setAnchorEditMenu] = useState(null);
    const [selectedWorkflow, setSelectedWorkflow] = useState(null);
    const [existingWorkflow, setExistingWorkflow] = useState(null);
    const [openWorkflowDialog, setOpenWorkflowDialog] = useState(false)
    const [loading, setLoading] = useState(true)
    const workflowService = new WorkflowService()

    const handleAddedSimpleWorkflow = () => {
        setOpenWorkflowDialog(false)
        workflowService.getAllWorkflows(setWorkflows, () => undefined)
        setSelectedWorkflow(null)
        setExistingWorkflow(null)

    }


    const handleClickEdit = (event, indexOfWorkflow) => {
        setAnchorEditMenu(event.currentTarget);
        setSelectedWorkflow(workflows[indexOfWorkflow])
    }


    const handleDelete = () => {
        setAnchorEditMenu(null);

        const table = selectedWorkflow.table
        const type = selectedWorkflow.type
        const name = selectedWorkflow.name

        workflowService.deleteWorkflow(table, type, name,
            () => setWorkflows(s => s.filter(w => JSON.stringify(w) !== JSON.stringify(selectedWorkflow))),
            () => undefined)
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

        workflowService.changeActive(table, type, name, !selectedWorkflow.active,
            () => workflowService.getAllWorkflows(setWorkflows, () => undefined),
            () => undefined)
    };

    useEffect(() => workflowService.getAllWorkflows((data) => {
        setWorkflows(data);
        setLoading(false);
    }, () => undefined), [])

    if (loading) return null

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

export default Workflow;