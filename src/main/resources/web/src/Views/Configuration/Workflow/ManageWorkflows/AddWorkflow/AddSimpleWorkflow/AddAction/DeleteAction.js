import { Divider, Grid } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import ActionFormTextArea from "./ActionFormTextArea"
import ActionFormTextField from "./ActionFormTextField"
import PopupForm from "./PopupForm"


import { TextField, Typography } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import QueryService from '../../../../../../../Service/QueryService/QueryService';
import ActionFormRow from "./ActionFormRow"
import RowCriterion from "./RowCriterion"

const DeleteAction = props => {
    const { onSubmit, placeholders, initial, open, onClose } = props
    const [tables, setTables] = useState([])

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState("")
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.rowCriteria)

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => { 
            setTables(data.data)
            if(initial != null) setTable(initial.table)
        }, () => { if(initial != null) setTable(initial.table)})
    }, [])

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({ name: name, rowCriteria: rowCriteria, table: table, type: "delete" })
    }

    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose}>
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action"/>

            <Divider />
            <Grid container direction="row" alignItems="center" spacing={2}>
                <Grid item>
                    <Typography>Delete one or more rows from table </Typography>
                </Grid>
                <Grid item>
                    <Select
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "30%" }}
                        onChange={(e) => setTable(e.target.value)}
                        value={table}
                        required
                    >
                        {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                    </Select>   
                </Grid>
            </Grid>
            {table.length > 0 && <>
                <Typography >If they match the following criteria</Typography>
                <RowCriterion requireValues={false} onChange={setRowCriteria} value={rowCriteria} placeholders={placeholders} table={table} />
            </>}

        </PopupForm>
    )
}

export default DeleteAction;