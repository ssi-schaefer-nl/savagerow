import React from 'react';
import { Handle } from 'react-flow-renderer';
import WorkflowTriggerShape from './WorkflowTriggerShape';


const WorkflowTrigger = ({ data, isConnectable }) => {

  return (
    <>
      <WorkflowTriggerShape text={data.label}/>
      <Handle
        type="source"
        position='right'
        id='right'
        style={{ backgroundColor: 'green', border: "2px solid black" }}
        isConnectable={isConnectable}
      />
    </>
  );
};


export default WorkflowTrigger