import React from 'react';
import CloseOutlinedIcon from '@material-ui/icons/CloseOutlined';
import { yellow } from '@material-ui/core/colors';


const DecisionPointShape = ({ text }) => {

  return (
      <div style={{
        border: "4px solid black",
        borderRadius: '20px',
        backgroundColor: yellow[300],
        padding: "1em",
        display: 'flex',
      }}>
        <CloseOutlinedIcon style={{ paddingRight: '1em' }} />
        {text}
      </div >

  );
};


export default DecisionPointShape