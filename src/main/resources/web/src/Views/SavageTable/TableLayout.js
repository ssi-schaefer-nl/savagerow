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
import { TextField } from '@material-ui/core';
import PopupForm from "../../Components/PopupForm/PopupForm";
import { Button, Grid, Toolbar } from "@material-ui/core";
import QueryService from '../../Service/QueryService/QueryService';
import CollapsableAlert from "../../Components/CollapsableAlert/CollapsableAlert";
import DatabaseSelect from "../../Components/DatabaseSelect/DatabaseSelect";
import SavageTable from "./SavageTable";
import DefinitionService from "../../Service/DefinitionService/DefinitionService";
import { ContextMenuTrigger } from "react-contextmenu";
import { ContextMenu, MenuItem } from "react-contextmenu"

const Tables = (props) => {
  const classes = useStyles();
  const database = localStorage.getItem("database")
  const [tables, setTables] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)
  const [value, setValue] = React.useState(0);
  const [addTable, setAddTable] = useState(false)
  const [reload, setReload] = useState(false)
  const [newTableName, setNewTableName] = useState("")
  const [selectedTable, setSelectedTable] = useState("")
  const [addTableError, setAddTableError] = useState("")

  const triggerReload = () => setReload(r => !r)

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  useEffect(() => {
    if (database != undefined) {
      new QueryService().getTables(
        (data) => {
          setTables(data.data)
          setLoading(false)
        },
        () => {
          setLoading(false)
          setError(true)
        })
    }
  }, [reload])


  const handleAddTable = () => {
    const defService = new DefinitionService(newTableName)
    defService.createTable(() => {
      triggerReload()
      setAddTable(false)
      setNewTableName("")
      setAddTableError("")
    }, () => setAddTableError("Error adding table. Check its name on errors and make sure it does not already exist"))
  }

  const handleDeleteTable = () => {
    const defService = new DefinitionService(selectedTable)
    defService.deleteTable(() => {
      triggerReload()
      setValue(0)
    }, () => undefined)
  }

  if (database == undefined) return (
    <div style={{ margin: "3em" }}>
      <Typography style={{ marginBottom: "2em" }}>No workspace is selected. Please select a database</Typography>
      <DatabaseSelect onSelect={() => window.location.reload(false)} />
    </div>
  )

  if (loading) {
    return (<><CircularProgress /> </>)
  }


  if (error) {
    return (<CollapsableAlert severity="error" message={"Error while retrieving tables for database: " + database} />)
  }

  return (
    <div>

      <AppBar position="static" color="default" style={{ padding: "0.5em", borderBottom: "1px solid grey" }}>
        <Toolbar >
          <Grid container direction="row" justify="space-between" alignItems="center">

            <Grid item xs={10}>
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
                  <ContextMenuTrigger
                    id="table-context-menu"
                    collect={() => setSelectedTable(table)}
                    holdToDisplay="-1"
                  >
                    <Tab fullWidth textColor="secundary" onClick={() => handleChange(null, tables.indexOf(table))} label={table} {...a11yProps(index)} />
                  </ContextMenuTrigger>
                ))}

              </Tabs>
            </Grid>
            <Grid item>
              <Button variant="contained" color="primary" onClick={() => setAddTable(true)}>Add Table</Button>
            </Grid>
          </Grid>
        </Toolbar>
        <ContextMenu id="table-context-menu">
          <Typography style={{ margin: "0.5em 1em" }}><strong>{selectedTable}</strong></Typography>
          <MenuItem divider />
          <MenuItem onClick={(e) => handleDeleteTable()}>Delete</MenuItem>
        </ContextMenu>
      </AppBar>

      {tables.length > 0 ?
        tables.map((table, index) => (
          <TabPanel value={value} index={index}>
            <SavageTable table={table} />
          </TabPanel>
        ))
        :
        <Grid container style={{ height: "50vh" }} justify="center" alignItems="center">
          <Grid item>
            <Button variant="contained" color="primary" style={{ height: "5em", width: "15em" }} onClick={() => setAddTable(true)}>Create the first table</Button>
          </Grid>
        </Grid>
      }
      <PopupForm open={addTable} title="Add table" onSubmit={handleAddTable} onClose={() => { setAddTable(false); setNewTableName(""); setAddTableError("") }}>
        <TextField label="Table name" value={newTableName} onChange={(e) => setNewTableName(e.target.value)} />
        {addTableError.length > 0 &&
          <Typography color="error">{addTableError}</Typography>
        }
      </PopupForm>
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