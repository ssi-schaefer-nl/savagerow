import { Checkbox, Divider, FormControlLabel, Grid, InputLabel, Select, Typography } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { MenuItem } from "react-contextmenu";
import PopupForm from "../../../../../../../Components/PopupForm/PopupForm";
import QueryService from '../../../../../../../Service/QueryService/QueryService';
import RowCriterion from "../../../../../../../Components/RowCriterion/RowCriterion";
import ActionFormTextField from "../ActionFormTextField";
import Tooltip from '@material-ui/core/Tooltip';
import ActionTooltips from "../../ActionTooltips";
import InfoIcon from '@material-ui/icons/Info';
import VerticalLinearStepper from "../../../../../../../Components/VerticalLinearStepper/VerticalLinearStepper";
import PopupWindow from "../../../../../../../Components/PopupWindow/PopupWindow";




const DeleteAction = props => {
    const { onSubmit, workflowTable, initial, open, onClose } = props
    const [tables, setTables] = useState([])
    const [tableColumns, setTableColumns] = React.useState(null);

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [deleteThis, setDeleteThis] = useState(false)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.rowCriteria)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)



    const handleSubmit = () => {
        if (deleteThis) {
            const crit = tableColumns.filter(c => c.pk).map(c => ({ column: c.name, comparator: "==", required: `{${c.name}}` }))
            onSubmit({ name: name, rowCriteria: crit, table: workflowTable, type: "delete", triggerWorkflows: triggerWorkflows })
        }
        else {
            onSubmit({ name: name, rowCriteria: rowCriteria, table: table, type: "delete", triggerWorkflows: triggerWorkflows })
        }

    }

    const steps = [
        {
            "name": "Enter a name",
            "Component": <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action" />,
            "nextAllowed": name != null && name.length > 0
        },
        {
            "name": "Optional: Trigger other workflows",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>{ActionTooltips.Trigger_Other_Workflows()}</Typography>
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
                </>,
        },
        {
            "name": "Optional: Delete triggering row",
            "Component":
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
                </Tooltip>,
            "onNext": deleteThis ? handleSubmit : null,
            "nextButton": deleteThis ? "Save" : null,
        },
        {
            "name": "Specify the target table",
            "Component":
                <>
                    <InputLabel id="select-table">Table</InputLabel>
                    <Select
                        id="select-table"
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "30%" }}
                        onChange={(e) => {
                            setTable(e.target.value)
                            setRowCriteria([])
                        }}
                        value={table}
                        required
                    >
                        {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                    </Select>
                </>,
            "nextAllowed": table.length > 0,
            "disabled": deleteThis
        },
        {
            "name": "Specify the row criteria",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        The row criteria will determine which row(s) will be deleted. The rows that satisfy all criteria will be deleted.

                    </Typography>
                    <RowCriterion
                        requireValues={false}
                        onChange={setRowCriteria}
                        value={rowCriteria}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={table}
                    />
                </>,
            "nextButton": "Save",
            "onNext": handleSubmit,
            "nextAllowed":  rowCriteria.length > 0 && rowCriteria.filter(r => Object.values(r).filter(v => v.length == 0).length > 0).length == 0,
            "disabled": deleteThis
        },
    ]

    useEffect(() => {
        const queryService = new QueryService(workflowTable)
        queryService.getTables(data => {
            setTables(data.data)
            if (initial != null) setTable(initial.table)
        }, () => { if (initial != null) setTable(initial.table) })
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))
    }, [])



    return (
        <PopupWindow open={open} onClose={onClose} title="Create Delete Action" wide>
            <div style={{ width: "50vw", height: "60vh" }}>
                <VerticalLinearStepper steps={steps} />
            </div>
        </PopupWindow >
    )
}

export default DeleteAction;