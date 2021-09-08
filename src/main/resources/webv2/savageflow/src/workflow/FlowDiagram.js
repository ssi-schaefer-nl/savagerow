import { Menu, MenuItem, Typography } from '@material-ui/core';
import React, { useEffect, useRef, useState } from 'react';
import ReactFlow, { addEdge, Controls, ReactFlowProvider, removeElements } from 'react-flow-renderer';
import { getLayoutedElements, createReactFlowElements } from './FlowDiagramElementUtils';
import { ReactFlowTaskNode, ReactFlowTriggerNode } from './ReactFlowNodes';


const initialState = {
    mouseX: null,
    mouseY: null,
};

const nodeTypes = {
    triggerNode:ReactFlowTriggerNode,
    taskNode: ReactFlowTaskNode
}



const FlowDiagram = ({ workflow, onClickNode }) => {
    const reactFlowWrapper = useRef(null);
    const [reactFlowInstance, setReactFlowInstance] = useState(null);
    const [anchor, setAnchor] = useState(initialState);
    const [elements, setElements] = useState([])

    useEffect(() => {
        if (workflow != undefined) setElements(createReactFlowElements(workflow))
    }, [workflow]);


    useEffect(() => {
        if (reactFlowInstance != undefined) reactFlowInstance.fitView()
    }, [reactFlowInstance]);


    const onLoad = (_reactFlowInstance) => setReactFlowInstance(_reactFlowInstance)

    const handleClick = (event) => {
        event.preventDefault();
        setAnchor({
            mouseX: event.clientX - 2,
            mouseY: event.clientY - 4,
        });
    };

    const onConnect = (params) => {
        const els = addEdge({ ...params, type: 'step', arrowHeadType: 'arrowclosed' }, elements)
        setElements(getLayoutedElements(els))
    }

    const handleClose = () => setAnchor(initialState);

    const placeNewNode = (type) => {
        const reactFlowBounds = reactFlowWrapper.current.getBoundingClientRect()
        const position = reactFlowInstance.project({
            x: anchor.mouseX - reactFlowBounds.left,
            y: anchor.mouseY - reactFlowBounds.top,
        });

        const newNode = {
            id: `${elements.length}`,
            data: { label: 'New Node' },
            type: type,
            position,
        }

        setElements(els => [...els, newNode])
        handleClose()
    }


    const onElementsRemove = (elementsToRemove) => {
        const els = getLayoutedElements(removeElements(elementsToRemove, elements))
        setElements(els)
    }

    return (
        <div style={{ height: "80vh", border: "1px solid grey" }} className="reactflow-wrapper" ref={reactFlowWrapper}>
            <ReactFlowProvider>

                <ReactFlow
                    elements={elements}
                    onPaneContextMenu={handleClick}
                    onLoad={onLoad}
                    snapToGrid
                    onConnect={onConnect}
                    onElementsRemove={onElementsRemove}
                    nodeTypes={nodeTypes}
                    connectionLineType='step'
                    onNodeDoubleClick={(e, n) => onClickNode(n.id)}
                >
                    <Controls />
                </ReactFlow>

                <Menu
                    keepMounted
                    open={anchor.mouseY !== null}
                    onClose={handleClose}
                    anchorReference="anchorPosition"
                    anchorPosition={
                        anchor.mouseY !== null && anchor.mouseX !== null
                            ? { top: anchor.mouseY, left: anchor.mouseX }
                            : undefined
                    }
                >
                    <Typography style={{ margin: "0.5em 1em 0em 1em" }}><strong>Workflow Element</strong></Typography>
                    <MenuItem disabled divider />
                    <MenuItem disabled={Boolean(elements.find(e => e.type === 'triggerNode'))} onClick={() => placeNewNode('triggerNode')}>Trigger</MenuItem>
                    <MenuItem onClick={() => placeNewNode('taskNode')}>Task</MenuItem>
                </Menu>
            </ReactFlowProvider>
        </div >

    )
}


export default FlowDiagram