import React, { Component } from "react";
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
import { yellow } from "@material-ui/core/colors";
import { AddOutlined } from "@material-ui/icons";


class Tables extends Component {
  state = {
    tables: [],
    configureService: new ConfigureService(),
    loading: true,
    database: "",
  }

  componentDidMount() {
    this.state.configureService.getCurrentDatabase(
      function (data) {
        this.setState({ database: data.data })
      }.bind(this),
      function (data) {
        this.setState({ database: null, loadCurrentDatabaseError: true })
        console.log(data)
      }.bind(this));

    this.state.configureService.getTables(
      function (data) {
        this.setState({ tables: data.data })
        this.setState({ loading: false })
      }.bind(this),
      function (data) {
        console.log(data)
        this.setState({ loading: false, loadTableDataError: true })

      }.bind(this));


  }

  render() {
    if (this.state.loading) {
      return (<></>)
    }

    if (!this.state.loadCurrentDatabaseError && this.state.database.length == 0) return (
      <div>
        <Typography style={{ marginBottom: "2em" }}>No database is selected. Please select a database</Typography>
        <DatabaseSelect onSelect={() => window.location.reload(false)} />
      </div>
    )
    if (this.state.loadTableDataError || this.state.loadCurrentDatabaseError) {
      return (<CollapsableAlert severity="error" message="Error while retrieving data." />)
    }

    return (
      <ScrollableTabsButtonAuto tables={this.state.tables} database={this.state.database} />
    );
  }
}


function ScrollableTabsButtonAuto(props) {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <div className={classes.root}>

      <AppBar position="static" color="default">
        <Toolbar>
          <Typography variant="h6" color="primary" style={{ marginRight: "1em", fontWeight: "bold"}}>
            {props.database}
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
            {props.tables.map((table, index) => (
              <Tab label={table} {...a11yProps(index)} />
            ))}

          </Tabs>

        </Toolbar>
      </AppBar>

      {props.tables.map((table, index) => (
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