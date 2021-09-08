import React from 'react';
import { Handle } from 'react-flow-renderer';
import { green } from '@material-ui/core/colors';
import PlayCircleFilledIcon from '@material-ui/icons/PlayCircleFilled';

const Trigger = ({ data, isConnectable }) => {

  return (
    <>
      <div style={{
        border: "4px solid black",
        backgroundColor: green[200],
        padding: "1em",
        display: 'flex',
      }}>
        <PlayCircleFilledIcon style={{ paddingRight: '1em' }} />
        {data.label}
      </div >
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


export default Trigger