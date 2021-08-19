import AppBar from '@material-ui/core/AppBar';
import { grey } from "@material-ui/core/colors";
import CssBaseline from '@material-ui/core/CssBaseline';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import MenuIcon from '@material-ui/icons/Menu';
import clsx from 'clsx';
import React from "react";
import {
  Route, Switch
} from "react-router-dom";
import Configure from "../Views/Configuration/Configure";
import Home from "../Views/Home";
import Tables from "../Views/SavageTable/TableLayout";
import Sidebar from './Sidebar';


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
  const [open, setOpen] = React.useState(false);
  const [expand, setExpand] = React.useState(null);
  const database = localStorage.getItem("database")



  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar
        position="fixed"
        className={clsx(classes.appBar)}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={() => setOpen(o => !o)}
            edge="start"
            className={clsx(classes.menuButton)}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap>
            {database != null ? database.charAt(0).toUpperCase() + database.slice(1) : "SavageRow"}
          </Typography>
        </Toolbar>
      </AppBar>

      <Sidebar open={open} />
    
      <main className={classes.content} >

        <div className={classes.toolbar} />
        <Switch>
          <Route exact path="/" ><Home /></Route>
          <Route path="/tables" ><Tables /></Route>
          <Route path="/configuration"><Configure /></Route>
        </Switch>

      </main>

    </div>
  );
}