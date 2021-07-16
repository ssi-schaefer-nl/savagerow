import { Checkbox, Divider, FormControlLabel, Grid, Select, Typography } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { MenuItem } from "react-contextmenu";
import PopupForm from "../../../../../../Components/PopupForm/PopupForm";
import QueryService from '../../../../../../Service/QueryService/QueryService';
import RowCriterion from "../../../../../../Components/RowCriterion/RowCriterion";
import ActionFormRow from "../../../../../../Components/ActionFormRowAdvanced/ActionFormRowAdvanced";
import ActionFormTextField from "../ActionFormTextField";
import ActionTooltips from "../../ActionTooltips"
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';


const UpdateAction = props => {
    const { onSubmit, workflowTable, initial, open, onClose } = props
    const [tables, setTables] = useState([])
    const [updateThis, setUpdateThis] = useState(true)
    const [tableColumns, setTableColumns] = React.useState(null);

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [fieldUpdates, setFieldUpdates] = useState(initial == null ? [] : initial.fieldUpdates)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.rowCriteria)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)

    useEffect(() => {
        const queryService = new QueryService(workflowTable)
        queryService.getTables(data => setTables(data.data), () => setTables([]))
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))

    }, [])

    const handleSubmit = e => {
        e.preventDefault()
        if (updateThis) {
            const crit = tableColumns.filter(c => c.pk).map(c => ({ column: c.name, operator: "==", required: `{${c.name}}` }))
            onSubmit({ name: name, fieldUpdates: fieldUpdates, rowCriteria: crit, table: workflowTable, type: "update", triggerWorkflows: triggerWorkflows })
        }
        else {
            onSubmit({ name: name, fieldUpdates: fieldUpdates, rowCriteria: rowCriteria, table: table, type: "update", triggerWorkflows: triggerWorkflows })
        }
    }


    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose}>
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action" />

            <Divider />
            <Tooltip title={ActionTooltips.Trigger_Other_Workflows()}>

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
            </Tooltip>
            <Tooltip title={ActionTooltips.TriggeredRow("update")}>
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={updateThis}
                            onChange={(e) => setUpdateThis(e.target.checked)}
                            name="trigger"
                            color="primary"
                        />
                    }
                    label="Update the row that triggered the workflow"
                />
            </Tooltip>
            {!updateThis
                ?
                <>
                    <Grid container direction="row" alignItems="center" spacing={2}>
                        <Tooltip title={ActionTooltips.Row("update")}>
                            <Grid item>
                                <Typography>Update one or more rows in table </Typography>
                            </Grid>
                        </Tooltip>
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

                        <Grid style={{marginTop: "2em"}} container direction="row" alignItems="center" spacing={1}>
                            <Grid item>
                                <Typography>If the rows satisfy the following criteria</Typography>
                            </Grid>
                            <Grid item>
                                <Tooltip title={ActionTooltips.RightClick("value")}>
                                    <InfoIcon fontSize="small" />
                                </Tooltip>
                            </Grid>

                        </Grid>
                        <RowCriterion requireValues={false} onChange={setRowCriteria} value={rowCriteria} placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }} table={table} />
                        <Grid style={{marginTop: "2em"}} container direction="row" alignItems="center" spacing={1}>
                            <Grid item>
                                <Typography>By changing its fields in the following way</Typography>
                            </Grid>
                            <Grid item>
                                <Tooltip title={ActionTooltips.RightClick("value")}>
                                    <InfoIcon fontSize="small" />
                                </Tooltip>
                            </Grid>

                        </Grid>
                        <ActionFormRow
                            onChange={setFieldUpdates}
                            value={fieldUpdates}
                            placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                            table={updateThis ? workflowTable : table}
                        />
                    </>
                    }
                </>
                :
                <>
                    <Grid container direction="row" alignItems="center" spacing={1}>
                        <Grid item>
                            <Typography>By changing its fields in the following way</Typography>
                        </Grid>
                        <Grid item>
                            <Tooltip title={ActionTooltips.RightClick("value")}>
                                <InfoIcon fontSize="small" />
                            </Tooltip>
                        </Grid>

                    </Grid>
                    <ActionFormRow
                        onChange={setFieldUpdates}
                        value={fieldUpdates}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={updateThis ? workflowTable : table}
                    />
                </>

            }


        </PopupForm >
    )
}

export default UpdateAction;