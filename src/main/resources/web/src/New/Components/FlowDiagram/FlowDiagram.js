import React, { useRef, useState } from 'react';
import ReactFlow, {
    addEdge, Background, Controls, removeElements
} from 'react-flow-renderer';
import NodeTypes from './Nodes/NodeTypes';




let id = 1;
const getId = () => `dndnode_${id++}`;

const FlowDiagram = ({ elements, onChangeElements, onEditElement }) => {
    const reactFlowWrapper = useRef(null);
    const [reactFlowInstance, setReactFlowInstance] = useState(null);


    const onLoad = (_reactFlowInstance) => {
        setReactFlowInstance(_reactFlowInstance);
        _reactFlowInstance.fitView();
    }

    const onDragOver = (event) => {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
    };

    const onDrop = (event) => {
        event.preventDefault();
        const reactFlowBounds = reactFlowWrapper.current.getBoundingClientRect();
        const type = event.dataTransfer.getData('application/reactflow');

        const position = reactFlowInstance.project({
            x: event.clientX - reactFlowBounds.left,
            y: event.clientY - reactFlowBounds.top,
        });

        const newNode = {
            id: getId(),
            type,
            position,
            data: { label: `${type}` },
        };

        onChangeElements([...elements, newNode])
    };



    const onElementsRemove = (elementsToRemove) => onChangeElements(removeElements(elementsToRemove, elements));
    const onConnect = (params) => onChangeElements(addEdge({ ...params, type: 'step' }, elements));

    return (
        <div style={{ width: '100%', height: '100%' }} ref={reactFlowWrapper}>
            <ReactFlow
                elements={elements}
                onElementsRemove={onElementsRemove}
                onConnect={onConnect}
                connectionLineType='step'
                onLoad={onLoad}
                snapToGrid={true}
                onDragOver={onDragOver}
                onDrop={onDrop}
                onNodeDoubleClick={(e, node) => onEditElement(node)}

                snapGrid={[5, 5]}
                nodeTypes={NodeTypes}
            >
                <Controls />
                <Background color="#aaa" gap={10} />
            </ReactFlow>

        </div>
    );
};

export default FlowDiagram;