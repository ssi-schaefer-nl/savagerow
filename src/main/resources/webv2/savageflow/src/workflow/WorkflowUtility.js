import { TRIGGER_ID } from "./FlowDiagramElementUtils"


const getTaskInput = (id, workflow) => {
    let tsk = workflow.trigger && workflow.trigger.task == id
        ? workflow.trigger
        : workflow.tasks.find(t => t.next == id)
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
        next: null
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
        const prevNode = w.tasks.findIndex(i => i.next == nodeId)
        if(prevNode != -1) w.tasks[prevNode].next = null
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
        if (task != null) task.next = target

    }
    return w
}
export { deleteNode, connectNodes, getTaskInput, addTaskToWorkflow, addTriggerToWorkflow, saveTaskToWorkflow, saveTriggerToWorkflow }
