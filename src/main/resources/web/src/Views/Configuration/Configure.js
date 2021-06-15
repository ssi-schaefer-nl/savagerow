import React from "react";
import Typography from '@material-ui/core/Typography';

import { Divider } from "@material-ui/core";
import DatabaseSelect from "../../Components/DatabaseSelect/DatabaseSelect";
import Workflow from "./Workflow/Workflow";

import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import AppBar from '@material-ui/core/AppBar';
import { Redirect, Route, Switch, useHistory, useLocation, useRouteMatch } from "react-router-dom";

function createConfigSection(name, PanelComponent) {
    return { "name": name, "component": PanelComponent }
}

const ConfigurationSections = [
    createConfigSection("Overview", <>
        <Typography>Nothing here yet.</Typography>
        <Divider style={{ margin: "1em 0em 2em 0em" }} />

    </>),
    createConfigSection("Database", <>
        <Typography>This section allows configuration related to the databases.</Typography>
        <Divider style={{ margin: "1em 0em 2em 0em" }} />
        <DatabaseSelect />
    </>),
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
                        <Redirect to={`${url}/${ConfigurationSections[0].name.toLowerCase()}`} />
                    </Route>
                </Switch>
            </div>

        </div>
    );

}



