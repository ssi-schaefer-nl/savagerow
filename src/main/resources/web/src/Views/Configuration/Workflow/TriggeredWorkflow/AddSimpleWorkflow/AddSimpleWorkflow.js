import React, { useEffect, useState } from "react";
import { TextField, Typography } from "@material-ui/core";
import HorizontalLinearStepper from "../../../../../Components/HorizontalLinearStepper/HorizontalLinearStepper"
import CreateWorkflowActions from "./CreateWorkflowActions/CreateWorkflowActions"
import WorkflowService from "../../../../../Service/WorkflowService/WorkflowService";
import QueryService from "../../../../../Service/QueryService/QueryService";
import { Select } from '@material-ui/core';
import { MenuItem } from "react-contextmenu";
import WorkflowConditions from "./WorkflowCondition/WorkflowCondition";

export default function AddSimpleWorkflow(props) {
    const { onFinish, existing } = props

    const [saving, setSaving] = useState(false)
    const [valid, setValid] = useState(false)
    const [finalStatus, setFinalStatus] = useState(null)

    const [name, setName] = useState(existing == null ? "" : existing.name);
    const [table, setTable] = useState(existing == null ? "" : existing.table);
    const [type, setType] = useState(existing == null ? "" : existing.type.toLowerCase());
    const [actions, setActions] = useState(existing == null ? [] : existing.actions);
    const [conditions, setConditions] = useState(existing == null ? [] : existing.conditions);


    const saveWorkflow = () => {
        const data = { table: table, name: name, actions: actions, active: true, type: type, conditions: conditions }
        const workflowService = new WorkflowService()
        setSaving(true)
        setFinalStatus("Saving..")
        if (existing != null) {
            data.identifier = existing.identifier
            workflowService.updateTriggeredWorkflow(data, () => {
                setSaving(false)
                setValid(true)
                setFinalStatus("Workflow has been sucessfully saved")
            }, (data) => {
                setSaving(false)
                setValid(false)
                setFinalStatus(data)
            })
        }
        else {
            workflowService.saveTriggeredWorkflow(data, () => {
                setSaving(false)
                setValid(true)
                setFinalStatus("Workflow has been sucessfully saved")
            }, (data) => {
                setSaving(false)
                setValid(false)
                setFinalStatus(data)
            })
        }
    }

    const editingSteps = [
        {
            "name": "Enter a name",
            "Component": <WorkflowStepName onChange={setName} value={name} />,
            "nextAllowed": name != null && name.length > 0
        },
        {
            "name": "Specify the trigger",
            "Component": <WorkflowStepTrigger onChangeTable={setTable} table={table} onChangeType={setType} type={type} />,
            "nextAllowed": table.length > 0 && type.length > 0
        },
        {
            "name": "Optional: Add conditions",
            "Component": <WorkflowConditions onChange={(c) => { console.log(c); setConditions(c); }} conditions={conditions} table={table} />,
        },
        {
            "name": "Create actions",
            "Component": <CreateWorkflowActions actions={actions} table={table} onChange={setActions} />,
            "nextAllowed": actions.length > 0,
            "nextButton": "Save",
            "onNext": saveWorkflow
        },

        {
            "name": "Finalize",
            "Component": <FinalizeStep message={finalStatus} saving={saving} valid={valid} />,
            "nextButton": "Finish",
            "nextAllowed": valid,
            "onNext": onFinish,
            "restrictBack": !saving && valid,
        },

    ]


    return <HorizontalLinearStepper steps={editingSteps} onFinish />
}


const WorkflowStepName = props => {
    const { onChange, value, disabled } = props

    return (
        <div style={{ marginBottom: "5em" }}>
            <Typography variant="h6">Enter a name for the workflow</Typography>
            <Typography style={{ marginBottom: "2em" }}>The name of a workflow serves as a short description and identification of the workflow</Typography>
            <TextField id="standard-basic" disabled={disabled} label="Workflow name" value={value} autoComplete='off' onChange={(e) => onChange(e.target.value)} />
        </div>
    )
}


const WorkflowStepTrigger = props => {
    const { onChangeTable, onChangeType, table, type, disabled } = props
    const [tables, setTables] = useState([])

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    return (
        <div>
            <Typography variant="h6">Specify the trigger that must initiate the workflow</Typography>
            <Typography>
                The trigger for the workflow specifies upon which kind of event the workflow has to be executed.
                These events are actions you perform on the data in your tables, such as editing, deleting or adding a row.
                A workflow is defined by its name, the type of event that triggers it and the table in which this event is triggered.
            </Typography>
            <div style={{ margin: "5em 2em" }} >
                Trigger the workflow when we
                <Select
                    disabled={disabled}
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "10em", margin: "0 1em" }}
                    onChange={(e) => onChangeType(e.target.value)}
                    value={type}
                    required
                >

                    {["delete", "update", "insert"].map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
                a row in table <nbsp />
                <Select
                    disabled={disabled}
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "10em", margin: "0 1em" }}
                    onChange={(e) => onChangeTable(e.target.value)}
                    value={table}
                    required
                >

                    {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
            </div>
        </div>
    )
}

const FinalizeStep = (props) => {
    const { valid, message, saving } = props

    return (
        <div >
            <Typography variant="h6">Finalizing</Typography>
            <Typography style={{ marginBottom: "5em" }} color={valid || saving ? "default" : "error"}>
                {message}
            </Typography>
        </div>
    )
}