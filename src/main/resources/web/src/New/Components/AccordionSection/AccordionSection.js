import { makeStyles } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import { grey } from "@material-ui/core/colors";
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import React from "react";


const useStyles = makeStyles((theme) => ({
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
}));

const AccordionSection = (props) => {
    const classes = useStyles();
    return (
        <Accordion expanded={props.expanded} onChange={props.onChange} style={{margin: '0' }}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
                style={{ padding: "0.5em"}}
                ref={(node) => {
                    if (node) {
                        node.style.setProperty('background-color', `${grey[100]}`, 'important');
                    }
                }}
            >
                <Typography className={classes.heading}>{props.title}</Typography>
            </AccordionSummary>
            <AccordionDetails style={{ flexDirection: "column", padding: "0.5em", overflow: 'auto' }}>
                {React.Children.toArray(props.children).map(child =>
                    <div style={{marginBottom: "0.5em"}}>
                        {child}
                    </div>
                )}
            </AccordionDetails>
        </Accordion>
    )
}

export default AccordionSection