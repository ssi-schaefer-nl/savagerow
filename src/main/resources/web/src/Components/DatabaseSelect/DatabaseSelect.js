import React, { Component, useEffect, useState } from 'react';

import Typography from '@material-ui/core/Typography';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { CircularProgress, Divider } from "@material-ui/core";
import NativeSelect from '@material-ui/core/NativeSelect';
import CollapsableAlert from "../CollapsableAlert/CollapsableAlert";
import DatabaseService from "../../Service/ConfigureService";


export default function DatabaseSelect(props) {
    const configureService = new DatabaseService()
    const { reloadSwitch, onSelect } = props

    const [databases, setDatabases] = useState([])
    const [database, setDatabase] = useState(undefined)
    const [anchorEl, setAnchorEl] = useState(null)
    const [loadingAvailableDatabases, setLoadingAvailableDatabases] = useState(true)
    const [value, setValue] = useState(null)
    const [loadingError, setLoadingError] = useState(false)

    useEffect(() => {
        setValue(localStorage.getItem('database'))

        configureService.listAllDatabases(
            function (data) {
                setDatabases(data.data)
                setLoadingAvailableDatabases(false)
            },
            function (data) {
                setLoadingAvailableDatabases(false)
                setLoadingError(true)
            });
    }, [reloadSwitch])

    const handleChange = (e) => {
        var db = e.target.value
        if (db != null) {
            localStorage.setItem('database', db);
            setDatabase(db)
            if (onSelect) onSelect()
        }
    }

    if (loadingAvailableDatabases) {
        return (<CircularProgress />)
    } else if (loadingError) {
        return (<CollapsableAlert severity="warning" message="Unable to fetch available databases. Check database connection." />)
    }

    var val = database ? database : value ? value : ""
    return (

        <FormControl style={{ margin: 1, minWidth: 120 }}>
            <InputLabel htmlFor="demo-customized-select-native">Database</InputLabel>
            <NativeSelect
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={val}
                onChange={handleChange}
            >
                <option aria-label="None" value={null} />

                {databases.map(d => (
                    <option value={d}>{d}</option>
                ))}

            </NativeSelect>
        </FormControl>
    )

}
