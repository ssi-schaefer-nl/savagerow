import dagre from 'dagre';
import { addEdge, isNode } from "react-flow-renderer";


const nodeWidth = 200;
const nodeHeight = 80;

const TRIGGER_ID = "trigger"

function createReactFlowElements(workflow) {
    let elements = []
    const trigger = workflow.trigger
    const tasks = workflow.tasks

    if (trigger != undefined) {
        elements.push({ id: TRIGGER_ID, type: "triggerNode", data: { label: trigger.name }, position: { x: 0, y: 0 } })
        if (trigger.task != null) {
            elements = addEdge({
                id: `trigger-${trigger.task}`,
                source: 'trigger',
                target: `${trigger.task}`,
                type: 'step',
                arrowHeadType: 'arrowclosed'
            }, elements)
        }
    }

    if (tasks != undefined) {
        tasks.forEach(t => {
            elements.push({ id: `${t.id}`, type: "taskNode", data: { label: t.name }, position: { x: 0, y: 0 } })
            if (t.next != null) {
                elements = addEdge({
                    id: `${t.id}-${t.next}`,
                    source: `${t.id}`,
                    target: `${t.next}`,
                    type: 'step',
                    arrowHeadType: 'arrowclosed'
                }, elements)
            }
        })
    }

    return getLayoutedElements(elements)
}

const dagreGraph = new dagre.graphlib.Graph().setGraph({ rankdir: "LR", ranksep: 100 })
dagreGraph.setDefaultEdgeLabel(() => ({}));

const getLayoutedElements = (elements) => {
    const isHorizontal = true;

    elements.forEach((el) => {
        if (isNode(el)) {
            dagreGraph.setNode(el.id, { width: nodeWidth, height: nodeHeight });
        } else {
            dagreGraph.setEdge(el.source, el.target);
        }
    });

    dagre.layout(dagreGraph);

    return elements.map((el) => {
        if (isNode(el)) {
            const nodeWithPosition = dagreGraph.node(el.id);
            el.targetPosition = isHorizontal ? 'left' : 'top';
            el.sourcePosition = isHorizontal ? 'right' : 'bottom';

            // unfortunately we need this little hack to pass a slightly different position
            // to notify react flow about the change. Moreover we are shifting the dagre node position
            // (anchor=center center) to the top left so it matches the react flow node anchor point (top left).
            el.position = {
                x: nodeWithPosition.x - nodeWidth / 2 + Math.random() / 1000,
                y: nodeWithPosition.y - nodeHeight / 2,
            };
        }

        return el;
    });
};


const createNewNode = (id, label, type, position) => ({
    id: id,
    data: { label: label },
    type: type,
    position: position
})


export { getLayoutedElements, createReactFlowElements, nodeHeight, nodeWidth, createNewNode, TRIGGER_ID };

