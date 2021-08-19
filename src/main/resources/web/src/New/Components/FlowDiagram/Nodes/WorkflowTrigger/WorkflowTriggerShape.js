import React from 'react';
import PlayCircleFilledIcon from '@material-ui/icons/PlayCircleFilled';
import { green, grey } from '@material-ui/core/colors';


const WorkflowTriggerShape = ({ text }) => {

  return (
      <div style={{
        border: "4px solid black",
        backgroundColor: green[200],
        padding: "1em",
        display: 'flex',
      }}>
        <PlayCircleFilledIcon style={{ paddingRight: '1em' }} />
        {text}
      </div >

  );
};


export default WorkflowTriggerShape