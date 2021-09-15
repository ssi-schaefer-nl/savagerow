import { Button, CircularProgress, Grid, Typography } from "@material-ui/core";
import { useEffect, useState } from "react";
import FlowDiagram from "./FlowDiagram";
import FlowDiagramContextMenu from "./FlowDiagramContextMenu";
import { createReactFlowElements, TRIGGER_ID } from "./FlowDiagramElementUtils";
import NodeContextMenu from "./NodeContextMenu";
import TaskEditDialog from "./TaskEditDialog";
import TriggerEditDialog from "./TriggerEditDialog";
import WorkflowService from "./WorkflowService";
import { addTaskToWorkflow, addTriggerToWorkflow, connectNodes, deleteNode, saveTaskToWorkflow, saveTriggerToWorkflow } from "./WorkflowUtility";

const EditWorkflow = ({ workflowId, onCancel }) => {
    const [workflow, setWorkflow] = useState(null)
    const [loadingFailure, setLoadingFailure] = useState(false)
    const [savingFailure, setSavingFailure] = useState(false)

    const [editTask, setEditTask] = useState(null)
    const [editTrigger, setEditTrigger] = useState(null)
    const [nodeIdForContextMenu, setNodeIdForContextMenu] = useState(null)

    const [elements, setElements] = useState([])
    const [rightClickAnchor, setRightClickAnchor] = useState({ x: null, y: null })


    useEffect(() => {
        if (workflow != null) {
            setElements(createReactFlowElements(workflow))
            new WorkflowService().save(workflow, () => setSavingFailure(false), () => setSavingFailure(true))
        } else
            new WorkflowService().get(workflowId, (w) => setWorkflow(w), () => setLoadingFailure(true))
    }, [workflow])

    const handleAddTrigger = (type) => setWorkflow(w => addTriggerToWorkflow(w, "New Trigger", type))

    const handleAddTask = (type, subtype) => setWorkflow(w => addTaskToWorkflow(w, `New ${type} ${subtype} task`, type, subtype))
    // const handleAddDecision = (type) => setWorkflow(w => addDecisionToWorkflow(w, "New Decision", type))

    const handlePaneContextMenu = (event) => {
        event.preventDefault();
        setRightClickAnchor({ x: event.clientX - 2, y: event.clientY - 4 });
    };

    const handleClickNode = (nodeId) => {
        if (nodeId == TRIGGER_ID) setEditTrigger(workflow.trigger)
        else if (workflow.tasks != null) {
            const task = workflow.tasks.find(t => t.id == nodeId)
            setEditTask(task)
        }
    }

    const handleSaveTask = () => {
        setWorkflow(w => saveTaskToWorkflow(w, editTask))
        setEditTask(null)
    }

    const handleSaveTrigger = () => {
        setWorkflow(w => saveTriggerToWorkflow(w, editTrigger))
        setEditTrigger(null)
    }

    const handleDeleteNode = () =>
        setWorkflow(w => deleteNode(w, nodeIdForContextMenu))


    const handleConnectNodes = (source, target) => setWorkflow(w => connectNodes(w, source, target))


    const handleNodeContextMenu = (e, node) => {
        e.preventDefault()
        setRightClickAnchor({ x: e.clientX, y: e.clientY });
        setNodeIdForContextMenu(node.id)
    }

    const handleSaveWorkflow = () => new WorkflowService().save(workflow, () => setWorkflow(null), console.log)

    const content = () => {
        if (workflow != null) return (
            <Grid item xs={12}>

                <FlowDiagram
                    elements={elements}
                    onClickNode={handleClickNode}
                    onConnectNodes={handleConnectNodes}
                    onPaneContextMenu={handlePaneContextMenu}
                    onNodeContextMenu={handleNodeContextMenu}
                />
                {savingFailure && <Typography color="secondary">An error occurred while saving the workflow!</Typography>}
            </Grid>
        )
        else if (loadingFailure) return (
            <Grid item style={{ height: "50vh" }} container alignItems='center' justifyContent='center'>
                <Typography color="error">Error loading the workflow. Please try again.</Typography>
            </Grid>
        )
        else return (
            <Grid item style={{ height: "50vh" }} container alignItems='center' justifyContent='center'>
                <CircularProgress />
            </Grid>
        )
    }

    return (
        <div >
            <Grid container spacing={1}>
                <Grid item container direction='row' spacing={2}>
                    <Grid item>
                        <Button variant="outlined" onClick={onCancel}>Cancel</Button>
                    </Grid>
                    <Grid item>
                        <Button variant="outlined" onClick={handleSaveWorkflow}>Save</Button>
                    </Grid>
                </Grid>
                {content()}

            </Grid>
            {editTask &&
                <TaskEditDialog
                    open={Boolean(editTask)}
                    task={editTask}
                    onSave={handleSaveTask}
                    handleClose={() => setEditTask(null)}
                    onChange={setEditTask}
                    workflowId={workflow.id}
                />
            }
            {editTrigger &&
                <TriggerEditDialog
                    open={Boolean(editTrigger)}
                    trigger={editTrigger}
                    onSave={handleSaveTrigger}
                    handleClose={() => setEditTrigger(null)}
                    onChange={setEditTrigger}
                />
            }
            {nodeIdForContextMenu == null ?
                <FlowDiagramContextMenu
                    anchor={rightClickAnchor}
                    onClose={() => setRightClickAnchor({ x: null, y: null })}
                    onAddTask={handleAddTask}
                    onAddTrigger={handleAddTrigger}
                    // onAddDecision={handleAddDecision}
                    allowTrigger={workflow && workflow.trigger == null}
                />
                :
                <NodeContextMenu
                    anchor={rightClickAnchor}
                    onClose={() => { setRightClickAnchor({ x: null, y: null }); setNodeIdForContextMenu(null) }}
                    onNodeDelete={handleDeleteNode}
                />
            }
        </div>
    )

}


export default EditWorkflow