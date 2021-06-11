import { Divider } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import ActionFormTextArea from "./ActionFormTextArea"
import ActionFormTextField from "./ActionFormTextField"
import NewActionForm from "./NewActionForm"


import { TextField, Typography } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import QueryService from '../../../../../../../Service/QueryService/QueryService';
import ActionFormRow from "./ActionFormRow"

const UpdateAction = props => {
    const { onSubmit, placeholders, initial, open, onClose } = props
    const [tables, setTables] = useState([])

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [row, setRow] = useState(initial == null ? [] : initial.row)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.row)

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({ name: name, row: row, rowCriteria: rowCriteria, table: table, type: "update" })
    }

    return (
        <NewActionForm open={open} onSubmit={handleSubmit} onClose={onClose}>
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required />

            <Divider />
            <InputLabel shrink required id="table">Table</InputLabel>
            <Select
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%" }}
                onChange={(e) => setTable(e.target.value)}
                value={table}
                required
            >
                {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
            </Select>
            <Typography style={{ marginTop: "1em" }}>Define the fields that must match with a row in order to update it</Typography>
            <ActionFormRow onChange={setRowCriteria} value={rowCriteria} placeholders={placeholders} table={table} />
            <Typography style={{ marginTop: "1em" }}>Define the fields you want to update on the row</Typography>
            <ActionFormRow onChange={setRow} value={row} placeholders={placeholders} table={table} />


        </NewActionForm>
    )
}

export default UpdateAction;