import React from 'react';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import { blue, green, grey } from '@material-ui/core/colors';


const WorkflowEndShape = ({ text }) => {

  return (
      <div style={{
        border: "4px solid black",
        backgroundColor: blue[200],
        padding: "1em",
        display: 'flex',
      }}>
        <ExitToAppIcon style={{ paddingRight: '1em' }} />
        {text}
      </div >

  );
};


export default WorkflowEndShape