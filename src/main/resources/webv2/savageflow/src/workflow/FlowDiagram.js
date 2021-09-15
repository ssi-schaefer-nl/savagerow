import React, { useEffect, useRef, useState } from 'react';
import ReactFlow, { Controls, ReactFlowProvider } from 'react-flow-renderer';
import { ReactFlowTaskNode, ReactFlowTriggerNode } from './ReactFlowNodes';

const nodeTypes = {
    triggerNode: ReactFlowTriggerNode,
    taskNode: ReactFlowTaskNode
}


const FlowDiagram = ({ elements, onClickNode, onConnectNodes, onNodeContextMenu, onPaneContextMenu }) => {
    const reactFlowWrapper = useRef(null);
    const [reactFlowInstance, setReactFlowInstance] = useState(null);

    useEffect(() => {
        if (reactFlowInstance != undefined) {
            reactFlowInstance.fitView()
            reactFlowInstance.zoomOut()
        }
    }, [reactFlowInstance, elements]);

    const onLoad = (_reactFlowInstance) => setReactFlowInstance(_reactFlowInstance)
    const onConnect = (params) => onConnectNodes(params.source, params.target)
    

    return (
        <div style={{ height: "80vh"}} className="reactflow-wrapper" ref={reactFlowWrapper}>
            <ReactFlowProvider>
                <ReactFlow
                    elements={elements}
                    onLoad={onLoad}
                    nodeTypes={nodeTypes}
                    onConnect={onConnect}
                    onNodeDoubleClick={(e, n) => onClickNode(n.id)}
                    onNodeContextMenu={onNodeContextMenu}
                    onPaneContextMenu={onPaneContextMenu}
                >
                    <Controls />
                </ReactFlow>

            </ReactFlowProvider>
        </div >

    )
}


export default FlowDiagram