import React, { Component, useState, useEffect } from "react";
import PropTypes from 'prop-types';
import { makeStyles, withStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import CircularProgress from '@material-ui/core/CircularProgress';
import Tooltip from '@material-ui/core/Tooltip';

import { Button, Toolbar } from "@material-ui/core";
import QueryService from '../../Service/QueryService/QueryService';
import CollapsableAlert from "../../Components/CollapsableAlert/CollapsableAlert";
import DatabaseSelect from "../../Components/DatabaseSelect/DatabaseSelect";
import SavageTable from "./SavageTable";

const Tables = (props) => {
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
    new QueryService().getTables(
      (data) => {
        setTables(data.data)
        setLoading(false)
      },
      () => {
        setLoading(false)
        setError(true)
      })
  }, [])



  if (loading) {
    return (<><CircularProgress /> </>)
  }

  if (database == null) return (
    <div style={{margin: "3em"}}>
      <Typography style={{ marginBottom: "2em" }}>No workspace is selected. Please select a workspace</Typography>
      <DatabaseSelect onSelect={() => window.location.reload(false)} />
    </div>
  )
  if (error) {
    return (<CollapsableAlert severity="error" message={"Error while retrieving tables for database: " + database} />)
  }

  return (
    <div>

      <AppBar position="static" color="default" style={{ padding: "0.5em", borderBottom: "1px solid grey" }}>
        <Toolbar >
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
        <TabPanel value={value} index={index}>
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
        <Box>
          {children}
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