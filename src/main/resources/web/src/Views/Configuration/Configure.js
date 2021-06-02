import React, { useState } from "react";
import Typography from '@material-ui/core/Typography';

import { CircularProgress, Divider, Grid, Paper, Table, TableCell, TableContainer, TableRow } from "@material-ui/core";
import DatabaseSelect from "../../Components/DatabaseSelect/DatabaseSelect";
import EditIcon from '@material-ui/icons/Edit';
import Workflow from "./Workflow/Workflow";

import { makeStyles } from '@material-ui/core/styles';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import OverviewFlow from "../../Components/Flow/OverviewFlow";
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import AppBar from '@material-ui/core/AppBar';
import TabPanel from "../../Components/TabPanel/TabPanel";
import { Link, Redirect, Route, Switch, useHistory, useLocation, useRouteMatch } from "react-router-dom";

function createConfigSection(name, PanelComponent) {
    return { "name": name, "component": PanelComponent }
}

const ConfigurationSections = [
    createConfigSection("Overview", <p>This is the configuration overview</p>),
    createConfigSection("Database", <DatabaseSelect />),
    createConfigSection("Workflows", <Workflow />)
]


function a11yProps(index) {
    return {
        id: `scrollable-auto-tab-${index}`,
        'aria-controls': `scrollable-auto-tabpanel-${index}`,
    };
}



const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        width: '100%',

    },
}));

export default function Configure(props) {
    const classes = useStyles();
    const { url, path } = useRouteMatch();
    const history = useHistory();
    const urlTokenized = useLocation().pathname.substring(1).split("/")
    let section = 0;

    if (urlTokenized.length > 1) {
        section = ConfigurationSections.findIndex(s => s.name.toLowerCase() === urlTokenized[1])
    } else {
        history.replace(`${url}/${ConfigurationSections[0].name.toLowerCase()}`)
    }

    const [value, setValue] = React.useState(section);

    const handleChange = (event, newValue) => {
        history.push(`${url}/${ConfigurationSections[newValue].name.toLowerCase()}`)
        setValue(newValue);
    }

    return (
        <div className={classes.root}>

            <AppBar position="static" color="default" style={{ padding: "0.5em", borderBottom: "1px solid grey" }}>
                <Toolbar >
                    <Typography variant="h6" color="primary" style={{ marginRight: "1em", fontWeight: "bold" }}>
                        Configuration
                    </Typography>
                    <Tabs
                        selectionFollowsFocus
                        value={value}
                        onChange={handleChange}
                        indicatorColor="primary"
                        textColor="secundary"
                        variant="scrollable"
                        scrollButtons="auto"
                        aria-label="scrollable auto tabs example"
                    >
                        {ConfigurationSections.map((section, index) => (
                            <Tab label={section.name} {...a11yProps(index)} />
                        ))}

                    </Tabs>

                </Toolbar>
            </AppBar>

            <div style={{ margin: "1em" }}>
                <Switch>
                    {ConfigurationSections.map((section, index) => (
                        <Route path={`${url}/${section.name.toLowerCase()}`} >
                            {section.component}
                        </Route>
                    ))}
                    <Route>
                        <Redirect to={`${url}/${ConfigurationSections[0].name.toLowerCase()}`}/>
                    </Route>
                </Switch>
            </div>

        </div>
    );

}



