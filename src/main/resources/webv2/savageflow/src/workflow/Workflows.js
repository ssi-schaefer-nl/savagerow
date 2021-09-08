import { useState } from "react";
import EditWorkflow from "./EditWorkflow";
import ManageWorkflows from "./ManageWorkflows";

const Workflows = (props) => {
    const [editingWorkflow, setEditingWorkflow] = useState(null)
    window.onbeforeunload = (event) => {
        return null; // undo preventing of refresh set by EditWorkflow
    };

    if (editingWorkflow == null)
        return <ManageWorkflows onEdit={setEditingWorkflow} />
    else
        return <EditWorkflow workflowId={editingWorkflow} onCancel={() => setEditingWorkflow(null)}/>

}

export default Workflows