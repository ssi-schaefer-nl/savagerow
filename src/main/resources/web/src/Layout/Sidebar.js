import Collapse from '@material-ui/core/Collapse';
import { grey } from "@material-ui/core/colors";
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import { ExpandLess, ExpandMore } from "@material-ui/icons";
import HomeIcon from '@material-ui/icons/Home';
import SettingsIcon from '@material-ui/icons/Settings';
import StorageIcon from '@material-ui/icons/Storage';
import clsx from 'clsx';
import React from "react";
import {
    Link
} from "react-router-dom";


const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',

    },
    drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
        borderRight: "1px solid grey"
    },

    drawerClose: {
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: theme.spacing(7) + 1,
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9) + 1,
        },
        borderRight: "1px solid grey"

    },
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: theme.spacing(0, 1, 0, 3),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
    },
    sublistElement: {
        borderWidth: "2px",
        borderStyle: "solid",
        borderColor: grey[50],
        backgroundColor: "white"
    }
}));

export default function Sidebar(props) {
    const { open } = props
    const classes = useStyles();
    const [expand, setExpand] = React.useState(null);

    const handleClick = (menu) => {
        if (expand != null) setExpand(null)
        else setExpand(menu)
    }

    if(!open && expand != null) setExpand(null)

    return (
        <Drawer
            variant="permanent"
            className={clsx(classes.drawer, {
                [classes.drawerOpen]: open,
                [classes.drawerClose]: !open,
            })}
            classes={{
                paper: clsx({
                    [classes.drawerOpen]: open,
                    [classes.drawerClose]: !open,
                }),
            }}

        >
            <div className={classes.toolbar}>

            </div>
            <Divider />
            <List>
                <ListElement link="/" text="Home" icon={HomeIcon} />
                <ListElement link="/tables" text="Tables" icon={StorageIcon} />
                {open ?
                    <SublistElement
                        handleClick={() => handleClick('config')}
                        open={expand == 'config'}
                        TheIcon={SettingsIcon}
                        text="Configuration"
                    >
                        <ListItem className={classes.sublistElement} component={Link} to="/configuration/database" button key="Database">
                            <ListItemText primary="Database" />
                        </ListItem>
                        <ListItem className={classes.sublistElement} component={Link} to="/configuration/workflows" button key="Workflow">
                            <ListItemText primary="Workflow" />
                        </ListItem>
                    </SublistElement>

                    :
                    <ListElement link="/configuration" text="Configuration" icon={SettingsIcon} />
                }

            </List>
        </Drawer>
    );
}

const ListElement = (props) => {
    const TheIcon = props.icon;

    return (
        <ListItem component={Link} to={props.link} button key={props.text}>
            <ListItemIcon><TheIcon /></ListItemIcon>
            <ListItemText primary={props.text} />
        </ListItem>
    )
}

const SublistElement = (props) => {
    const { handleClick, open, TheIcon, text } = props

    return (
        <>
            <ListItem button onClick={handleClick} style={open ? {backgroundColor: grey[50]} : undefined}>
                <ListItemIcon>
                    <TheIcon />
                </ListItemIcon>
                <ListItemText primary={text} />
                {open ? <ExpandLess /> : <ExpandMore />}
            </ListItem>
            <Collapse style={{backgroundColor: grey[50], padding: "0em 0em 0.5em 0.5em"}} in={open} timeout="auto" unmountOnExit >
                <List component="div" disablePadding>
                    {props.children}
                </List>
            </Collapse >
        </>
    )
}