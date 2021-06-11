import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';

import { makeStyles, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import EditIcon from '@material-ui/icons/EditOutlined';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";
import Button from '@material-ui/core/Button';
import { Redirect, Route, Switch, useHistory, useRouteMatch } from "react-router";
import WorkflowService from "../../../Service/WorkflowService/WorkflowService";
import ManageWorkflows from "./ManageWorkflows/ManageWorkflows";


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
    
    const { url } = useRouteMatch();
    const history = useHistory();
    const workflowService = new WorkflowService()
    
    const [expanded, setExpanded] = React.useState(null);
    const [summary, setSummary] = useState([])

    const types = ["insert", "update", "delete"]

    const handleChange = (panel) => (event, isExpanded) => (setExpanded(isExpanded ? panel : false));

    useEffect(() => (workflowService.getDbSummary(setSummary, () => undefined)), [])

    return (
        <Switch>
            <Route exact path={url}>
                <div className={classes.root}>
                
                    {types.map(type => {
                        const presentingType = type.charAt(0).toUpperCase() + type.slice(1)
                        return (
                            <AccordionSection onChange={handleChange(type)} expanded={expanded === type} title={presentingType + " Workflows"}>
                                <WorkflowOverview
                                    summary={summary.map(s => ({ "table": s.table, "workflows": s[type] }))}
                                    onEdit={(table) => history.push(`${url}/${type}/${table}`)}
                                />
                            </AccordionSection>
                        )
                    })}
                </div>
            </Route>
            {types.map(type => (
                <Route path={`${url}/${type}/:table`}>
                    <ManageWorkflows type={type} onChange={() => workflowService.getDbSummary(setSummary, () => undefined)}/>
                </Route>
            ))}
            <Route>
                <Redirect to={url} />
            </Route>
        </Switch >
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
            <AccordionDetails style={{ flexDirection: "column", padding: "0em" }}>
                {props.children}
            </AccordionDetails>
        </Accordion>
    )
}

const WorkflowOverview = (props) => {
    const { onEdit, summary } = props

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