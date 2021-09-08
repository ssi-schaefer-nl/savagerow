import React from 'react';
import { Handle } from 'react-flow-renderer';
import CloseOutlinedIcon from '@material-ui/icons/CloseOutlined';
import { yellow } from '@material-ui/core/colors';


const Evaluation = ({ data, isConnectable }) => {

  return (
    <>
      <div style={{
        border: "4px solid black",
        borderRadius: '20px',
        backgroundColor: yellow[300],
        padding: "1em",
        display: 'flex',
      }}>
        <CloseOutlinedIcon style={{ paddingRight: '1em' }} />
        {data.label}
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
        id='right-top'
        style={{ top: 15, backgroundColor: 'green', border: "2px solid black" }}
        isConnectable={isConnectable}
      />
      <Handle
        type="source"
        position='right'
        id='right-bottom'
        style={{ bottom: 5, top: 'auto', backgroundColor: 'green', border: "2px solid black" }}
        isConnectable={isConnectable}
      />
    </>
  );
};


export default Evaluation