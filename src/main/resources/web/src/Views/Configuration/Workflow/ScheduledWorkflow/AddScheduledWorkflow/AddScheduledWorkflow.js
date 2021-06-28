import React, { useEffect, useState } from "react";
import { TextField, Typography } from "@material-ui/core";
import HorizontalLinearStepper from "../../../../../Components/HorizontalLinearStepper/HorizontalLinearStepper"
import CreateWorkflowActions from "../../Common/CreateWorkflowActions/CreateWorkflowActions"
import QueryService from "../../../../../Service/QueryService/QueryService";
import { Select } from '@material-ui/core';
import { MenuItem } from "react-contextmenu";
import WorkflowConditions from "../../Common/WorkflowCondition/WorkflowCondition"
import WorkflowService from "../../../../../Service/WorkflowService/WorkflowService";
import Input from '@material-ui/core/Input';

export default function AddScheduledWorkflow(props) {
    const { onFinish, existing } = props

    const [saving, setSaving] = useState(false)
    const [valid, setValid] = useState(false)
    const [finalStatus, setFinalStatus] = useState(null)

    const [name, setName] = useState(existing == null ? "" : existing.name);
    const [time, setTime] = useState(existing == null ? "13:00" : existing.period.time)
    const [days, setDays] = useState(existing == null ? [] : existing.period.days)
    const [actions, setActions] = useState(existing == null ? [] : existing.actions);
    const [conditions, setConditions] = useState(existing == null ? [] : existing.conditions);


    const saveWorkflow = () => {
        const data = { name: name, period: {time: time, days: days}, actions: actions, active: true, conditions: conditions }
        const workflowService = new WorkflowService()
        setSaving(true)
        setFinalStatus("Saving..")
        workflowService.saveScheduledWorkflow(data, () => {
            setSaving(false)
            setValid(true)
            setFinalStatus("Workflow has been sucessfully saved")
        }, (data) => {
            setSaving(false)
            setValid(false)
            setFinalStatus(data)
        })

    }

    const editingSteps = [
        {
            "name": "Enter a name",
            "Component": <WorkflowStepName disabled={existing != null} onChange={setName} value={name} />,
            "nextAllowed": name != null && name.length > 0
        },
        {
            "name": "Specify the period",
            "Component": <WorkflowStepPeriod setDays={setDays} days={days} setTime={setTime} time={time} />,
            "nextAllowed": days.length > 0 && time.length > 0
        },
        {
            "name": "Create actions",
            "Component": <CreateWorkflowActions actions={actions} onChange={setActions} />,
            "nextAllowed": actions.length > 0,
        },
        {
            "name": "Add conditions",
            "Component": <WorkflowConditions onChange={(c) => setConditions(c)} conditions={conditions} />,
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
            <Typography style={{ marginBottom: "2em" }}>
                The name of a workflow serves as a short description of the workflow.
                Scheduled workflows are solely identified by its name.
            </Typography>
            <TextField id="standard-basic" disabled={disabled} label="Workflow name" value={value} autoComplete='off' onChange={(e) => onChange(e.target.value)} />
        </div>
    )
}


const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: 48 * 4.5 + 8,
            width: 250,
        },
    },
};

const WorkflowStepPeriod = props => {
    const { days, setDays, time, setTime } = props

    return (
        <div>
            <Typography variant="h6">Specify the period with which the workflow must be repeatedly executed</Typography>
            <Typography>
                The period for the workflow defines how often a  upon which kind of event the workflow has to be executed.
            </Typography>
            <div style={{ margin: "5em 2em", width: "100%" }} >
                Execute the workflow at

                <TextField
                    id="time"
                    type="time"
                    inputProps={{
                        step: 300,
                    }}
                    style={{ margin: "0 1em", width: "7em" }}
                    value={time}
                    onChange={(e) => setTime(e.target.value)}

                />
                on every
                <Select
                    id="mutiple"
                    multiple
                    value={days}
                    onChange={(e) => setDays(e.target.value)}
                    input={<Input />}
                    style={{ margin: "0 1em", minWidth: "5em" }}
                    MenuProps={MenuProps}
                >
                    {["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"].map((name) => (
                        <MenuItem key={name} value={name}>
                            {name}
                        </MenuItem>
                    ))}
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