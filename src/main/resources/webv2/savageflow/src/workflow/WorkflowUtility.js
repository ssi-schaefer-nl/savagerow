import { TRIGGER_ID } from "./FlowDiagramElementUtils"


const getTaskInput = (id, workflow) => {
    let tsk = workflow.trigger && workflow.trigger.task == id
        ? workflow.trigger
        : workflow.tasks.find(t => t.neighbors.includes(id))
    if (tsk == undefined) return {}
    return tsk.output
}

const addTaskToWorkflow = (workflow, name, type, subtype) => {
    const w = { ...workflow }
    let id = 0

    if (w.tasks && w.tasks.length > 0)
        id = (Math.max.apply(Math, w.tasks.map(n => parseInt(n.id, 0))) + 1).toString()

    const task = {
        id: id,
        name: name,
        taskType: type,
        subtype: subtype,
        neighbors: []
    }

    if (w.tasks == null) w.tasks = [task]
    else w.tasks.push(task)
    return w
}

const addTriggerToWorkflow = (workflow, name, triggerType) => {
    const w = { ...workflow }
    const trigger = { name: name, triggerType: triggerType, task: null }
    w.trigger = trigger
    return w
}

const addDecisionToWorkflow = (workflow, name, triggerType) => {
    const w = { ...workflow }
    return workflow
}
const saveTaskToWorkflow = (workflow, task) => {
    const w = { ...workflow }
    w.tasks = w.tasks.filter(t => t.id !== task.id)
    w.tasks.push(task)
    console.log(task)
    return w
}

const saveTriggerToWorkflow = (workflow, trigger) => {
    const w = { ...workflow }
    w.trigger = trigger
    return w
}

const deleteNode = (workflow, nodeId) => {
    const w = { ...workflow }
    if (nodeId == TRIGGER_ID) {
        w.trigger = null;
    } else if (w.tasks != null) {
        w.tasks = w.tasks.filter(t => t.id != nodeId)
        w.tasks = w.tasks.map(t => (
            { ...t, neighbors: t.neighbors.filter(n => n != nodeId) }
        ))
    }

    return w;
}

const connectNodes = (workflow, source, target) => {
    const w = { ...workflow }
    if (source == TRIGGER_ID) {
        if (w.trigger != null) {
            w.trigger.task = target
        }
    }
    else if (w.tasks != null) {

        const task = w.tasks.find(t => t.id == source)
        if (task != null) task.neighbors.push(target)

    }
    return w
}
export {addDecisionToWorkflow, deleteNode, connectNodes, getTaskInput, addTaskToWorkflow, addTriggerToWorkflow, saveTaskToWorkflow, saveTriggerToWorkflow }
