import React from 'react';
import AssignmentIcon from '@material-ui/icons/Assignment';
import { orange } from '@material-ui/core/colors';


const TaskShape = ({ text }) => {

  return (
      <div style={{
        border: "4px solid black",
        borderRadius: '10px',
        backgroundColor: orange[200],
        padding: "1em",
        display: 'flex',
      }}>
        <AssignmentIcon style={{ paddingRight: '1em' }} />
        {text}
      </div >

  );
};


export default TaskShape