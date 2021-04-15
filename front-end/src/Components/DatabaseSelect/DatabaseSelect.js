import React from "react";

import Typography from '@material-ui/core/Typography';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { CircularProgress, Divider } from "@material-ui/core";
import NativeSelect from '@material-ui/core/NativeSelect';
import CollapsableAlert from "../CollapsableAlert/CollapsableAlert";
import ConfigureService from "../../Service/ConfigureService";


class DatabaseSelect extends React.Component {
    state = {
        configureService: new ConfigureService(),
        databases: [],
        anchorEl: null,
        loadingAvailableDatabases: true,
        onSelect: this.props.onSelect
    };

    componentDidMount() {
        this.setState({ value: this.props.initialValue })
        this.state.configureService.listAllDatabases(
            function (data) {
                this.setState({ databases: data.data })
                this.setState({ loadingAvailableDatabases: false })
            }.bind(this),
            function (data) {
                console.log(data)
                this.setState({ loadingAvailableDatabases: false, loadingError: true })
            }.bind(this));

    }

    handleChange = (e) => {
        var database = e.target.value
        this.state.configureService.changeDatabases(
            database,
            function (data) {
                this.setState({ database: database })
                if (this.state.onSelect) this.state.onSelect()
            }.bind(this),
            function (data) {
                console.log("did not change db")
            });
    }

    render() {
        if (this.state.loadingAvailableDatabases) {
            return (<CircularProgress />)
        } else if (this.state.loadingError) {
            return (<CollapsableAlert severity="warning" message="Unable to fetch available databases. Check database connection." />)
        }

        var val = this.state.database ? this.state.database : this.props.initialValue ? this.props.initialValue : ""
        return (
            <>
                <FormControl style={{ margin: 1, minWidth: 120 }}>
                    <InputLabel htmlFor="demo-customized-select-native">Database</InputLabel>
                    <NativeSelect
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={val}
                        onChange={this.handleChange}
                    >
                                  <option aria-label="None" value="" />

                        {this.state.databases.map(d => (
                            <option value={d}>{d}</option>
                        ))}

                    </NativeSelect>
                </FormControl>
            </>
        )
    }
}

export default DatabaseSelect