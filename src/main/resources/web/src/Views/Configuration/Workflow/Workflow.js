import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';

import { makeStyles, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import EditIcon from '@material-ui/icons/Edit';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";
import Button from '@material-ui/core/Button';
import { Redirect, Route, Switch, useHistory, useRouteMatch } from "react-router";
import UpdateWorkflows from "./UpdateWorkflows/UpdateWorkflows";
import InsertWorkflows from "./InsertWorkflows/InsertWorkflows";
import DeleteWorkflows from "./DeleteWorkflows/DeleteWorkflows";
import WorkflowService from "../../../Service/WorkflowService/WorkflowService";


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
    const [expanded, setExpanded] = React.useState(null);
    const { url, path } = useRouteMatch();
    const history = useHistory();
    const workflowService = new WorkflowService()
    const [summary, setSummary] = useState([])

    useEffect(() => {
        workflowService.getDbSummary(setSummary, () => undefined);
    }, [])

    const handleChange = (panel) => (event, isExpanded) => {
        setExpanded(isExpanded ? panel : false);
    };

    return (
        <Switch>
            <Route exact path={url}>
                <div className={classes.root}>
                    <ConfigurationSection onChange={handleChange('update')} expanded={expanded === 'update'} title="Update Workflows">
                        <WorkflowOverview
                            summary={summary.map(s => ({ "table": s.table, "workflows": s.update }))}
                            onEdit={(table) => { history.push(`${url}/update/${table}`) }}
                        />
                    </ConfigurationSection>
                    <ConfigurationSection onChange={handleChange('insert')} expanded={expanded === 'insert'} title="Insert Workflows">
                        <WorkflowOverview
                            summary={summary.map(s => ({ "table": s.table, "workflows": s.insert }))}
                            onEdit={(table) => { history.push(`${url}/insert/${table}`) }}
                        />
                    </ConfigurationSection>
                    <ConfigurationSection onChange={handleChange('delete')} expanded={expanded === 'delete'} title="Delete Workflows">
                        <WorkflowOverview
                            summary={summary.map(s => ({ "table": s.table, "workflows": s.delete }))}
                            onEdit={(table) => { history.push(`${url}/delete/${table}`) }}
                        />
                    </ConfigurationSection>
                </div>
            </Route>
            <Route path={`${url}/update/:table`}>
                <UpdateWorkflows />
            </Route>
            <Route path={`${url}/insert/:table`}>
                <InsertWorkflows />
            </Route>
            <Route path={`${url}/delete/:table`}>
                <DeleteWorkflows />
            </Route>
            <Route>
                <Redirect to={url} />
            </Route>
        </Switch>
    )
}

const ConfigurationSection = (props) => {
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
            <AccordionDetails style={{ flexDirection: "column", padding: "0em" }}>

                {props.children}
            </AccordionDetails>
        </Accordion>
    )
}

const WorkflowOverview = (props) => {
    const { onEdit, summary } = props

    console.log(summary)

    return (
        <div>
            <TableContainer component={Paper} style={{ maxHeight: "50vh" }}>
                <Table stickyHeader >
                    <TableHead >
                        <TableRow>
                            <TableCell>Table</TableCell>
                            <TableCell align="right">Active Workflows</TableCell>
                            <TableCell align="right">Total Workflows</TableCell>
                            <TableCell align="right" />
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {summary.map((data) => (
                            <TableRow key={data.table}>
                                <TableCell component="th" scope="row">{data.table}</TableCell>
                                <TableCell align="right">{data.workflows.active}</TableCell>
                                <TableCell align="right">{data.workflows.total}</TableCell>
                                <TableCell align="right">
                                    <Button onClick={() => onEdit(data.table)}>
                                        <EditIcon />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>

                </Table>
            </TableContainer>
        </div >
    )

}

export default Workflow;