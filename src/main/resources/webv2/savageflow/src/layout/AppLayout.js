import { Typography } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import { grey } from '@material-ui/core/colors';
import CssBaseline from '@material-ui/core/CssBaseline';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import MenuIcon from '@material-ui/icons/Menu';
import clsx from 'clsx';
import { useState } from 'react';
import { Route, Switch } from "react-router-dom";
import Routes from './Routes';
import Sidebar from './Sidebar';

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        backgroundColor: grey[100],
    },
    content: {
        flexGrow: 1,
        height: '100vh',
        padding: theme.spacing(3),
        backgroundColor: "white",
    },
}))

const AppLayout = (props) => {
    const classes = useStyles();
    const [open, setOpen] = useState(false);

    return (
        <div className={classes.root}>
            <CssBaseline />
            <AppBar position="fixed" className={classes.appBar}>
                <Toolbar>
                    <IconButton
                        aria-label="open drawer"
                        onClick={() => setOpen(o => !o)}
                        edge="start"
                        className={clsx(classes.menuButton)}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" color="primary" noWrap style={{marginLeft: "2em"}}>SavageFlow</Typography>
                </Toolbar>
            </AppBar>
            <Sidebar open={open} />
            <main className={classes.content}>
                <Toolbar />
                <Switch>
                    {Routes.map(r =>
                        <Route exact={r.exact} path={r.link}>{r.content}</Route>
                    )}
                </Switch>
            </main>
        </div>
    );
}

export default AppLayout;