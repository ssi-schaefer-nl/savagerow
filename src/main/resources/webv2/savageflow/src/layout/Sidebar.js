import { grey } from "@material-ui/core/colors";
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import React from "react";
import {
    Link
} from "react-router-dom";
import Routes from './Routes';


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


    if (!open && expand != null) setExpand(null)

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
                {Routes.map(r =>
                    <ListElement link={r.link} text={r.name} icon={r.icon} />
                )}
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
