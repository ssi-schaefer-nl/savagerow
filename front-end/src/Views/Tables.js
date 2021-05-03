import React, { Component, useState, useEffect } from "react";
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import SavageTable from "../Components/Table/Table";
import { Button, Toolbar } from "@material-ui/core";
import ConfigureService from "../Service/ConfigureService";
import CollapsableAlert from "../Components/CollapsableAlert/CollapsableAlert";
import DatabaseSelect from "../Components/DatabaseSelect/DatabaseSelect";

const Tables = (props) => {
  const configureService = new ConfigureService()
  const classes = useStyles();
  const database = localStorage.getItem("database")
  const [tables, setTables] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  useEffect(() => {
    configureService.getTables(
      (data) => {
        setTables(data.data)
        setLoading(false)
      },
      () => {
        setLoading(true)
        setError(true)
      })
  }, [])



  if (loading) {
    return (<></>)
  }

  if (database == null) return (
    <div>
      <Typography style={{ marginBottom: "2em" }}>No database is selected. Please select a database</Typography>
      <DatabaseSelect onSelect={() => window.location.reload(false)} />
    </div>
  )
  if (error) {
    return (<CollapsableAlert severity="error" message={"Error while retrieving tables for database: " + database} />)
  }

  return (
    <div className={classes.root}>

      <AppBar position="static" color="default">
        <Toolbar>
          <Typography variant="h6" color="primary" style={{ marginRight: "1em", fontWeight: "bold" }}>
            {database}
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
            {tables.map((table, index) => (
              <Tab label={table} {...a11yProps(index)} />
            ))}

          </Tabs>

        </Toolbar>
      </AppBar>

      {tables.map((table, index) => (
        <TabPanel value={value} index={index}  >
          <SavageTable table={table} />
        </TabPanel>
      ))}
    </div>
  );

}

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`scrollable-auto-tabpanel-${index}`}
      aria-labelledby={`scrollable-auto-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3} padding="1em 0.5em">
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

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

export default Tables;