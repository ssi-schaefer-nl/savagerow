import { Checkbox, Divider, FormControlLabel, Grid, InputLabel, Select, Typography } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { MenuItem } from "react-contextmenu";
import PopupForm from "../../../../../../../Components/PopupForm/PopupForm";
import QueryService from '../../../../../../../Service/QueryService/QueryService';
import RowCriterion from "../../../../../../../Components/RowCriterion/RowCriterion";
import ActionFormRow from "../../../../../../../Components/ActionFormRowAdvanced/ActionFormRowAdvanced";
import ActionFormTextField from "../ActionFormTextField";
import ActionTooltips from "../../ActionTooltips"
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';
import VerticalLinearStepper from "../../../../../../../Components/VerticalLinearStepper/VerticalLinearStepper";
import PopupWindow from "../../../../../../../Components/PopupWindow/PopupWindow";


const UpdateAction = props => {
    const { onSubmit, workflowTable, initial, open, onClose } = props
    const [tables, setTables] = useState([])
    const [updateThis, setUpdateThis] = useState(false)
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

    const handleSubmit = () => {
        if (updateThis) {
            const crit = tableColumns.filter(c => c.pk).map(c => ({ column: c.name, comparator: "==", required: `{${c.name}}` }))
            onSubmit({ name: name, fieldUpdates: fieldUpdates, rowCriteria: crit, table: workflowTable, type: "update", triggerWorkflows: triggerWorkflows })
        }
        else {
            onSubmit({ name: name, fieldUpdates: fieldUpdates, rowCriteria: rowCriteria, table: table, type: "update", triggerWorkflows: triggerWorkflows })
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
            "name": "Optional: Update triggering row",
            "Component":
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
                </Tooltip>,
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
            "disabled": updateThis
        },
        {
            "name": "Specify the row criteria",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        The row criteria will determine which row(s) will be updated. The rows that satisfy all criteria will be updated.

                    </Typography>
                    <RowCriterion
                        requireValues={false}
                        onChange={setRowCriteria}
                        value={rowCriteria}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={table}
                    />
                </>,
            "disabled": updateThis,
            "nextAllowed":  rowCriteria.length > 0 && rowCriteria.filter(r => Object.values(r).filter(v => v.length == 0).length > 0).length == 0 
        },
        {
            "name": "Specify the column updates",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        The column updates define how the matching rows will be updated.

                    </Typography>
                    <ActionFormRow
                        onChange={setFieldUpdates}
                        value={fieldUpdates}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={updateThis ? workflowTable : table}
                    />
                </>,
            "nextButton": "Save",
            "nextAllowed":  fieldUpdates.length > 0 && fieldUpdates.filter(r => Object.values(r).filter(v => v.length == 0).length > 0).length == 0,
            "onNext": handleSubmit,
        },
    ]

    return (
        <PopupWindow open={open} onClose={onClose} title="Create Update Action" wide>
            <div style={{ width: "50vw", height: "60vh" }}>
                <VerticalLinearStepper steps={steps} />
            </div>
        </PopupWindow >
    )
}

export default UpdateAction;