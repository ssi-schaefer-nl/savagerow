import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import Button from '@material-ui/core/Button';
import { Divider, Grid } from "@material-ui/core";
import DatabaseSelect from "../../Components/DatabaseSelect/DatabaseSelect";
import Workflow from "./Workflow/Workflow";

import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import AppBar from '@material-ui/core/AppBar';
import { Redirect, Route, Switch, useHistory, useLocation, useRouteMatch } from "react-router-dom";
import { TextField } from '@material-ui/core';
import DatabaseService from "../../Service/ConfigureService";
import PopupForm from "../../Components/PopupForm/PopupForm";

function createConfigSection(name, PanelComponent) {
    return { "name": name, "component": PanelComponent }
}



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

const WorkspaceOverview = props => {
    const [workspaces, setWorkspaces] = useState([])
    const [addWorkspace, setAddWorkspace] = useState(false)
    const [areYouSure, setAreYouSure] = useState(false)
    const [selectedWorkspace, setSelectedWorkspace] = useState("")
    const [addWorkspaceFailed, setAddWorkspaceFailed] = useState(true)
    const [newWorkspaceName, setNewWorkspaceName] = useState("")

    useEffect(() => {
        const databaseService = new DatabaseService();
        databaseService.listAllDatabases((res) => setWorkspaces(res.data), () => undefined)


    }, [])

    const removeWorkspace = (database) => {
        const databaseService = new DatabaseService()
        databaseService.removeDatabase(database.toLowerCase(), () =>
            databaseService.listAllDatabases((res) => setWorkspaces(res.data), () => undefined)
            , () => undefined)
        setAreYouSure(false)
        if (localStorage.getItem("database") === database) {
            localStorage.removeItem("database")
            window.location.reload(false)
        }
        props.onChange()
    }

    const handleAddWorkspace = (workspace) => {
        const databaseService = new DatabaseService()
        databaseService.addDatabase(newWorkspaceName, () =>
            databaseService.listAllDatabases((res) => setWorkspaces(res.data), () => setAddWorkspaceFailed(true))
            , () => undefined)
        setAddWorkspace(false)
        setNewWorkspaceName("")
        props.onChange()
    }

    return (
        <>
            <TableContainer style={{ marginTop: "2em", width: "20vw", maxHeight: "30vh" }}>

                <Table stickyHeader  >
                    <TableHead >
                        <TableRow>
                            <TableCell size="small" >
                                Database name
                            </TableCell>
                            <TableCell size="small" align="right">
                                <Button onClick={(e) => setAddWorkspace(true)} >
                                    <AddIcon />
                                </Button>
                            </TableCell>

                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {workspaces.map(w => (
                            <TableRow key={w}>
                                <TableCell size="small" >
                                    {w}
                                </TableCell>
                                <TableCell size="small" align="right">
                                    <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => { setAreYouSure(true); setSelectedWorkspace(w) }}>
                                        <RemoveIcon color="error" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer >
            <PopupForm open={addWorkspace} title="Add workspace" onSubmit={handleAddWorkspace} onClose={() => { setAddWorkspace(false); setNewWorkspaceName("") }}>
                <TextField label="Workspace name" value={newWorkspaceName} onChange={(e) => setNewWorkspaceName(e.target.value)} />
            </PopupForm>
            <PopupForm open={areYouSure} hide title="Confirm deletion" onSubmit={() => undefined} onClose={() => setAreYouSure(false)}>
                <Grid container justify="center" spacing={4}>
                    <Grid item xs={4}>
                        <Button fullWidth variant="contained" onClick={() => removeWorkspace(selectedWorkspace)} color="secondary">Delete</Button>
                    </Grid>
                    <Grid item xs={4}>
                        <Button fullWidth variant="contained" color="primary" onClick={() => setAreYouSure(false)}>Cancel</Button>
                    </Grid>
                </Grid>
            </PopupForm>
        </>
    )
}

const DatabaseConfigSection = props => {
    const [rerender, setRerender] = useState(false)


    return (
        <>
            <Typography variant="h6" color="primary" style={{ margin: "1em 0" }}>Database</Typography>
            <Divider style={{ margin: "2em 0" }} />
            <Grid container direction="row" spacing={10} style={{ paddingLeft: "2em" }}>
                <Grid item>
                    <Typography variant="subtitle2" color="primary" style={{ paddingBottom: "1em" }}>Current Database</Typography>
                    <DatabaseSelect reloadSwitch={rerender} onSelect={() => window.location.reload(false)} />
                </Grid>
                <Grid item style={{ paddingLeft: "10em" }}>
                    <Typography variant="subtitle2" color="primary">Manage Database</Typography>
                    <WorkspaceOverview onChange={() => setRerender(r => !r)} />
                </Grid>


            </Grid>
        </>
    )
}

const ConfigurationSections = [
    createConfigSection("Database", <DatabaseConfigSection />),
    createConfigSection("Workflows", <Workflow />)
]