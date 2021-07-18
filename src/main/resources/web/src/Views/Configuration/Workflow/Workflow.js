import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';

import { makeStyles } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";
import TriggeredWorkflow from "./TriggeredWorkflow/TriggeredWorkflow";
import ScheduledWorkflow from "./ScheduledWorkflow/ScheduledWorkflow";
import DatabaseSelect from "../../../Components/DatabaseSelect/DatabaseSelect";


const Workflow = (props) => {
    const [expanded, setExpanded] = React.useState("triggered-workflows");
    const database = localStorage.getItem("database")

    const handleChange = (panel) => (event, isExpanded) => (setExpanded(isExpanded ? panel : false));


    return (
        <>
            {database == null ?
                <div style={{ margin: "3em" }}>
                    <Typography style={{ marginBottom: "2em" }}>No workspace is selected. Please select a database</Typography>
                    <DatabaseSelect onSelect={() => window.location.reload(false)} />
                </div>
                :
                <AccordionSection onChange={handleChange("triggered-workflows")} expanded={expanded === "triggered-workflows"} title="Triggered Workflows">
                    <TriggeredWorkflow />
                </AccordionSection>
                // <AccordionSection onChange={handleChange("scheduled-workflows")} expanded={expanded === "scheduled-workflows"} title="Scheduled Workflows">
                //         <ScheduledWorkflow />
                //     </AccordionSection>
            }
        </>
    )
}

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
}));

const AccordionSection = (props) => {
    const classes = useStyles();

    return (
        <Accordion expanded={props.expanded} onChange={props.onChange} style={{margin: "0.5em"}}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
                style={{padding: "1em"}}
                ref={(node) => {
                    if (node) {
                      node.style.setProperty('background-color', `${grey[50]}`,'important');
                    }
                  }}
            >
                <Typography className={classes.heading}>{props.title}</Typography>
            </AccordionSummary>
            <AccordionDetails style={{ flexDirection: "column", padding: "1em" }}>
                {props.children}
            </AccordionDetails>
        </Accordion>
    )
}


export default Workflow;