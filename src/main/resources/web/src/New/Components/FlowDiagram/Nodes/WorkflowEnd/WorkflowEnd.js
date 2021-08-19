import React from 'react';
import { Handle } from 'react-flow-renderer';
import WorkflowEndShape from './WorkflowEndShape';


const WorkflowEnd = ({ data, isConnectable }) => {

  return (
    <>
      <WorkflowEndShape text={data.label}/>
      <Handle
        type="target"
        position='left'
        id='left'
        style={{ backgroundColor: 'blue', border: "2px solid black" }}
        isConnectable={isConnectable}
      />
    </>
  );
};


export default WorkflowEnd