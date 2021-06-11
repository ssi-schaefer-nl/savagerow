import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

import { Divider, TextField, Typography } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';
import { InputLabel, Select  } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import { ContextMenu, ContextMenuTrigger, MenuItem } from 'react-contextmenu';
import QueryService from '../../../../../../../Service/QueryService/QueryService';

const ActionOnRowForm = props => {
    const { placeholders, onChange } = props

    const [appender, setAppender] = useState(() => () => undefined)

    const [tables, setTables] = useState([])
    const [columns, setColumns] = useState(null)

    const [data, setData] = useState({table: '', row: []}) 

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    useEffect(() => {
        if (data['table'] != null && data['table'].length > 0) {
            new QueryService(data['table']).getSchema(data => {
                const tempColumns = data.data.columns
                handleChange('row', [])
                const newRow = {}
                tempColumns.map(c => newRow[c.name] = '')
                handleChange('row', newRow)
                setColumns(tempColumns)
            }, () => setColumns([]))
        }

    }, [data['table']])

    const onRowChange = (column, newValue) => {
        const newRow = {...data['row'], [column]: newValue }
        handleChange('row', newRow)
    }

    const handleChange = (field, value) => {
        const newData = {...data, [field]: value}
        setData(newData)
        onChange(newData)
    }

    return (
        <>
            <InputLabel shrink required id="table">Table</InputLabel>
            <Select
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%" }}
                onChange={(e) => handleChange('table', e.target.value)}
                value={data['table']} 
                required
            >
                {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
            </Select>

            {columns != null && columns.length > 0 &&
                <>
                    <Divider style={{ margin: "2em 0" }} />

                    <Typography style={{ margin: "1em 0em" }}>Define the fields for the row</Typography>
                    <TableContainer component={Paper} style={{ padding: "1em",width: "90%" }}>

                        <Table  >
                            <TableHead >
                                <TableRow>
                                    {columns.map((col) =>
                                        <TableCell size="small" style={{ border: "1px solid", borderColor: grey[200] }}>{col.name + (!col.pk && !col.nullable ? "*" : "")}</TableCell>
                                    )}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                <TableRow key={1}>
                                    {columns.map(c => (
                                        <TableCell size="small" style={{ padding: "0em", margin: "0em", border: "1px solid", borderColor: grey[200] }}>
                                            <ContextMenuTrigger id="x-menu" collect={() => setAppender(() => (x) => onRowChange(c.name, (data['row'][c.name] != undefined ? data['row'][c.name] + x : x)))}>
                                                <TextField
                                                    id={c.name}
                                                    value={data['row'][c.name]}
                                                    required={!c.pk && !c.nullable}
                                                    InputLabelProps={{ shrink: true }}
                                                    InputProps={{ disableUnderline: true, autoComplete: 'new-password' }}
                                                    autoComplete='off'
                                                    onChange={(e) => onRowChange(c.name, e.target.value)}
                                                />
                                            </ContextMenuTrigger>
                                        </TableCell>
                                    ))}
                                </TableRow>

                            </TableBody>
                        </Table>
                    </TableContainer>


                    <ContextMenu id="x-menu">
                        <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
                        <MenuItem divider />
                        {placeholders.map(f => (<MenuItem onClick={(e) => appender(`{${f}}`)}>{f}</MenuItem>))}
                    </ContextMenu>
                </>
            }
        </>

    )
}

export default ActionOnRowForm