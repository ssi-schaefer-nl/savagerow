import React, { useEffect, useState } from "react";
import { TextField, Typography } from "@material-ui/core";
import HorizontalLinearStepper from "../../../../../../Components/HorizontalLinearStepper/HorizontalLinearStepper"
import CreateWorkflowActions from "./CreateWorkflowActions/CreateWorkflowActions"
import WorkflowService from "../../../../../../Service/WorkflowService/WorkflowService";

export default function AddSimpleWorkflow(props) {
    const { table, type, onFinish } = props
    const workflowService = new WorkflowService()

    const [saving, setSaving] = useState(false)
    const [valid, setValid] = useState(false)
    const [finalStatus, setFinalStatus] = useState(null)
    const [name, setName] = useState("");
    const [actions, setActions] = useState([]);

    const saveWorkflow = () => {
        const data = { table: table, name: name, actions: actions, active: true, type: type}
        setSaving(true)
        setFinalStatus("Saving..")
        workflowService.saveWorkflow(type, data, () => {
            setSaving(false)
            setValid(true)
            setFinalStatus("Workflow has been sucessfully added")
        }, (data) => {
            setSaving(false)
            setValid(false)
            setFinalStatus(data)
        })

    }

    const steps = [
        {
            "name": "Enter a name",
            "Component": <WorkflowStepName onChange={setName} value={name} />,
            "nextAllowed": name != null && name.length > 0
        },
        {
            "name": "Create actions",
            "Component": <CreateWorkflowActions actions={actions} table={table} onChange={setActions} />,
            "nextAllowed": actions.length > 0
        },
        {
            "name": "Add conditions",
            "Component": <p>Not Implemented</p>,
            "nextButton": "Save",
            "onNext": saveWorkflow
        },
        {
            "name": "Finalize",
            "Component": <FinalizeStep message={finalStatus} saving={saving} valid={valid} />,
            "nextButton": "Finish",
            "nextAllowed": valid,
            "onNext": onFinish
        },

    ]
    return <HorizontalLinearStepper steps={steps} />
}


const WorkflowStepName = props => {
    const { onChange, value } = props

    return (
        <div>
            <Typography variant="h6">Enter a name for the workflow</Typography>
            <TextField id="standard-basic" label="Workflow name" value={value} onChange={(e) => onChange(e.target.value)} />
        </div>
    )
}

const FinalizeStep = (props) => {
    const { valid, message } = props

    return (
        <>
            <Typography variant="h6">Finalizing</Typography>
            <Typography color={valid ? "default" : "error"}>
                {message}
            </Typography>
        </>
    )
}