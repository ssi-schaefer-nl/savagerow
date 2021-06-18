import { Card } from "@material-ui/core";
import React, { Component } from "react";
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
}));

class Home extends Component {
  render() {
    return (
      <div style={{margin: "1em"}}>
        <h2>Home</h2>
        <CenteredGrid />
      </div>
    );
  }
}

function CenteredGrid() {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper className={classes.paper}>
            Cool stuff will come here
            </Paper>
        </Grid>
        <Grid item xs={6}>
          <Paper className={classes.paper}>Cool stuff coming in here too</Paper>
        </Grid>
        <Grid item xs={6}>
          <Paper className={classes.paper}>More cool stuff</Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>Cool stuff will arrive soon</Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>Stand by for cool stuff</Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>Are you cool? The stuff coming here will be.</Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>Cool.</Paper>
        </Grid>
      </Grid>
    </div>
  );
}
export default Home;
