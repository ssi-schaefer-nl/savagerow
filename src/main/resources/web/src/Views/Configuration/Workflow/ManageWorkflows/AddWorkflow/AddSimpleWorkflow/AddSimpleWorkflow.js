import React, { useState } from "react";
import { TextField, Typography } from "@material-ui/core";
import HorizontalLinearStepper from "../../../../../../Components/HorizontalLinearStepper/HorizontalLinearStepper"
import CreateWorkflowActions from "./CreateWorkflowActions/CreateWorkflowActions"


export default function AddSimpleWorkflow(props) {
    const [name, setName] = useState(null);
    const [actions, setActions] = useState([]);

    const steps = [
        { "name": "Enter a name", "Component": <WorkflowStepName onChange={setName} value={name} /> },
        { "name": "Create actions", "Component": <WorkflowStepCreateActions /> },
        { "name": "Add conditions", "Component": <p>Not Implemented</p> },
        { "name": "Validate Workflow", "Component": <WorkflowStepValidate /> },

    ]
    return (
        <div>
            <HorizontalLinearStepper steps={steps} />
        </div>
    )
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


const WorkflowStepCreateActions = props => {
    const [actions, setActions] = useState([]);
    const { onChange } = props

    return (
        <div>
            <Typography variant="h6">Create actions for the workflow</Typography>
            <CreateWorkflowActions onChange={onChange} />
        </div>
    )
}

const WorkflowStepValidate = props => {
    const { name, actions } = props

    return (
        <div>
            <div>
                <Typography variant="h6">Validate the workflow configuration</Typography>
            </div>
        </div>
    )
}
