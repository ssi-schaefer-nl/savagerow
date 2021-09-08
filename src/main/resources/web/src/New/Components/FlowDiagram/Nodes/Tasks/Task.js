import React from 'react';
import { Handle } from 'react-flow-renderer';
import AssignmentIcon from '@material-ui/icons/Assignment';
import { orange } from '@material-ui/core/colors';


const Task = ({ data, isConnectable }) => {

  return (
    <>
      <div style={{
        border: "4px solid black",
        borderRadius: '10px',
        backgroundColor: orange[200],
        padding: "1em",
        display: 'flex',
      }}>
        <AssignmentIcon style={{ paddingRight: '1em' }} />
        {data}
      </div >      <Handle
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