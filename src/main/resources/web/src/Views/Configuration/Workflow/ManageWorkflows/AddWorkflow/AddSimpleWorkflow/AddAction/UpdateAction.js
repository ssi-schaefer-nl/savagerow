import { Checkbox, Divider, FormControlLabel, Grid } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import ActionFormTextArea from "./ActionFormTextArea"
import ActionFormTextField from "./ActionFormTextField"
import PopupForm from "./PopupForm"


import { TextField, Typography } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import QueryService from '../../../../../../../Service/QueryService/QueryService';
import ActionFormRow from "./ActionFormRowAdvanced"
import RowCriterion from "./RowCriterion"

const UpdateAction = props => {
    const { onSubmit, placeholders, initial, open, onClose } = props
    const [tables, setTables] = useState([])

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [fieldUpdates, setFieldUpdates] = useState(initial == null ? [] : initial.fieldUpdates)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.fieldUpdates)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({ name: name, fieldUpdates: fieldUpdates, rowCriteria: rowCriteria, table: table, type: "update", triggerWorkflows: triggerWorkflows })
    }

    console.log(fieldUpdates)

    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose}>
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action"/>

            <Divider />
            <FormControlLabel
                control={
                    <Checkbox
                        checked={triggerWorkflows}
                        onChange={(e) => setTriggerWorkflows(e.target.checked)}
                        name="trigger"
                        color="primary"
                    />
                }
                label="Trigger other workflows with this action"
            />
            <Grid container direction="row" alignItems="center" spacing={2}>
                <Grid item>
                    <Typography>Update one or more rows in table </Typography>
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
                <Typography >By altering its fields in the following way</Typography>
                <ActionFormRow onChange={setFieldUpdates} value={fieldUpdates} placeholders={placeholders} table={table} />

                <Typography style={{marginTop: "2em"}}>If the rows satisfy the following criteria</Typography>
                <RowCriterion requireValues={false} onChange={setRowCriteria} value={rowCriteria} placeholders={placeholders} table={table} />


            </>}


        </PopupForm>
    )
}

export default UpdateAction;