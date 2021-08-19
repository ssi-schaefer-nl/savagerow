import React from 'react';
import { Handle } from 'react-flow-renderer';
import TaskShape from './TaskShape';


const Task = ({ data, isConnectable }) => {

  return (
    <>
      <TaskShape text={data.label}/>
      <Handle
        type="target"
        position='left'
        id='left'
        style={{ backgroundColor: 'blue', border: "2px solid black" }}
        isConnectable={isConnectable}
      />
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


export default Task