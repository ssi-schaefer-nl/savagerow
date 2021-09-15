import { useState } from "react";
import TabMenu from "../common/TabMenu";
import EditWorkflow from "./EditWorkflow";
import ManageWorkflows from "./ManageWorkflows";

const tabs = ["Manage", "Monitor"]

const Workflows = (props) => {
    const [currentTab, setCurrentTab] = useState(tabs[0])
    const [editingWorkflow, setEditingWorkflow] = useState(null)

    const Content = () => {
        if (currentTab === "Manage" && editingWorkflow == null) {
            return <ManageWorkflows onEdit={setEditingWorkflow} />

        }
        else return null
    }

    if (editingWorkflow != null) return <EditWorkflow workflowId={editingWorkflow} onCancel={() => setEditingWorkflow(null)} />
    return (
        <TabMenu tabs={tabs} onChange={setCurrentTab}>
            <Content />
        </TabMenu>
    )

}

export default Workflows