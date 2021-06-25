import { Checkbox, Divider, FormControlLabel, Grid, Select, Typography } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { MenuItem } from "react-contextmenu";
import PopupForm from "../../../../../../Components/PopupForm/PopupForm";
import QueryService from '../../../../../../Service/QueryService/QueryService';
import RowCriterion from "../../../../../../Components/RowCriterion/RowCriterion";
import ActionFormTextField from "../ActionFormTextField";
import Tooltip from '@material-ui/core/Tooltip';
import ActionTooltips from "../../ActionTooltips";
import InfoIcon from '@material-ui/icons/Info';




const DeleteAction = props => {
    const { onSubmit, workflowTable, initial, open, onClose } = props
    const [tables, setTables] = useState([])
    const [tableColumns, setTableColumns] = React.useState(null);

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState("")
    const [deleteThis, setDeleteThis] = useState(true)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.rowCriteria)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)

    useEffect(() => {
        const queryService = new QueryService(workflowTable)
        queryService.getTables(data => {
            setTables(data.data)
            if (initial != null) setTable(initial.table)
        }, () => { if (initial != null) setTable(initial.table) })

        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))
    }, [])


    console.log(tableColumns)

    const handleSubmit = e => {
        e.preventDefault()
        if (deleteThis) {
            const crit = tableColumns.filter(c => c.pk).map(c => ({ column: c.name, operator: "==", required: `{${c.name}}` }))
            onSubmit({ name: name, rowCriteria: crit, table: workflowTable, type: "delete", triggerWorkflows: triggerWorkflows })
        }
        else {
            onSubmit({ name: name, rowCriteria: rowCriteria, table: workflowTable, type: "delete", triggerWorkflows: triggerWorkflows })
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
            <Tooltip title={ActionTooltips.TriggeredRow("delete")}>

                <FormControlLabel
                    control={
                        <Checkbox
                            checked={deleteThis}
                            onChange={(e) => setDeleteThis(e.target.checked)}
                            name="trigger"
                            color="primary"
                        />
                    }
                    label="Delete the row that triggered the workflow"
                />
            </Tooltip>
            {!deleteThis &&
                (
                    <>
                        <Divider style={{ marginBottom: "2em" }} />
                        <Grid container direction="row" alignItems="center" spacing={2}>
                            <Tooltip title={ActionTooltips.Row("delete")}>

                                <Grid item>
                                    <Typography>Delete one or more rows from table </Typography>
                                </Grid>
                            </Tooltip>
                            <Grid item>
                                <Select
                                    InputLabelProps={{ shrink: true }}
                                    style={{ minWidth: "30%" }}
                                    onChange={(e) => setTable(e.target.value)}
                                    value={workflowTable}
                                    required
                                >
                                    {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                                </Select>
                            </Grid>
                        </Grid>
                        {workflowTable.length > 0 && <>
                            <Grid style={{marginTop: "2em"}} container direction="row" alignItems="center" spacing={1}>
                                <Grid item>
                                    <Typography>If they match the following criteria</Typography>
                                </Grid>
                                <Grid item>
                                    <Tooltip title={ActionTooltips.RightClick("row")}>
                                        <InfoIcon fontSize="small" />
                                    </Tooltip>
                                </Grid>

                            </Grid>
                            <RowCriterion requireValues={false} onChange={setRowCriteria} value={rowCriteria} placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }} table={workflowTable} />
                        </>}
                    </>
                )
            }

        </PopupForm >
    )
}

export default DeleteAction;