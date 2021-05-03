import React, { Component, useEffect, useState } from 'react';

import Typography from '@material-ui/core/Typography';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { CircularProgress, Divider } from "@material-ui/core";
import NativeSelect from '@material-ui/core/NativeSelect';
import CollapsableAlert from "../CollapsableAlert/CollapsableAlert";
import ConfigureService from "../../Service/ConfigureService";


export default function DatabaseSelect(props) {
    const configureService = new ConfigureService()
    const onSelect = props.onSelect
    const [databases, setDatabases] = useState([])
    const [database, setDatabase] = useState(undefined)
    const [anchorEl, setAnchorEl] = useState(null)
    const [loadingAvailableDatabases, setLoadingAvailableDatabases] = useState(true)
    const [initialValue, setInitialValue] = useState(null)
    const [loadingError, setLoadingError] = useState(false)

    useEffect(() => {
        setInitialValue(props.initialValue)

        configureService.listAllDatabases(
            function (data) {
                setDatabases(data.data)
                setLoadingAvailableDatabases(false)
            },
            function (data) {
                setLoadingAvailableDatabases(false)
                setLoadingError(true)
            });
    }, [])

    const handleChange = (e) => {
        var db = e.target.value
        configureService.changeDatabases(
            db,
            () => {
                localStorage.setItem('database', db);
                setDatabase(db)
                if (onSelect) onSelect()
            },
            undefined
        );
    }

    if (loadingAvailableDatabases) {
        return (<CircularProgress />)
    } else if (loadingError) {
        return (<CollapsableAlert severity="warning" message="Unable to fetch available databases. Check database connection." />)
    }

    var val = database ? database : initialValue ? initialValue : ""
    return (

        <FormControl style={{ margin: 1, minWidth: 120 }}>
            <InputLabel htmlFor="demo-customized-select-native">Database</InputLabel>
            <NativeSelect
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={val}
                onChange={handleChange}
            >
                <option aria-label="None" value="" />

                {databases.map(d => (
                    <option value={d}>{d}</option>
                ))}

            </NativeSelect>
        </FormControl>
    )

}


class DatabaseSelectOld extends React.Component {
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

// export default DatabaseSelect