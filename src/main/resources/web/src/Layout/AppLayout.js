import React, { Component } from "react";
import clsx from 'clsx';
import { makeStyles, rgbToHex, useTheme } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Home from "../Views/Home";
import Contact from "../Views/Contact";
import Configure from "../Views/Configuration/Configure";
import Tables from "../Views/DataTables/Tables";
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import {
  Route, Switch,
} from "react-router-dom";
import StorageIcon from '@material-ui/icons/Storage';
import HomeIcon from '@material-ui/icons/Home';
import InfoIcon from '@material-ui/icons/Info';
import SettingsIcon from '@material-ui/icons/Settings';

import {
  Link,
} from "react-router-dom";
import RoutedComponent from "./RoutedComponent";
import { grey, red } from "@material-ui/core/colors";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: 'nowrap',

  },
  drawerOpen: {
    width: drawerWidth,
    backgroundColor: grey[200],

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
    backgroundColor: grey[200],
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
  content: {
    flexGrow: 1,
    width: "80%",
    height: "100vh",
    padding: theme.spacing(0),
    backgroundColor: '#FFFFFF'
  },
}));

export default function OverviewFlow() {
  const classes = useStyles();
  const theme = useTheme();
  const [open, setOpen] = React.useState(false);


  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar
        position="fixed"
        className={clsx(classes.appBar, {
          [classes.appBarShift]: open,
        })}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
            className={clsx(classes.menuButton, {
              [classes.hide]: open,
            })}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap>
            SavageRow
          </Typography>
        </Toolbar>
      </AppBar>
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
          <Typography variant="h6" noWrap >
            Menu
          </Typography>
          <IconButton onClick={handleDrawerClose}>
            {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
          </IconButton>

        </div>
        <Divider />
        <List>
          <ListElement link="/" text="Home" icon={HomeIcon} />
          <ListElement link="/tables" text="Tables" icon={StorageIcon} />
          <ListElement link="/configuration" text="Configuration" icon={SettingsIcon} />
        </List>
        <Divider />
        <List>
          <ListElement link="/contact" text="Contact" icon={InfoIcon} />
        </List>
      </Drawer>

      <main className={classes.content} >

        <div className={classes.toolbar} />
        <Switch>
          <Route exact path="/" ><Home/></Route>
          <Route path="/tables" ><Tables/></Route>
          <Route path="/contact" ><Contact/></Route>
          <Route path="/configuration"><Configure/></Route>
        </Switch>

      </main>

    </div>
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