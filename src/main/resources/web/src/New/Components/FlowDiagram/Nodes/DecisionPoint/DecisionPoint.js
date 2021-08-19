import React from 'react';
import { Handle } from 'react-flow-renderer';
import DecisionPointShape from './DecisionPointShape';


const DecisionPoint = ({ data, isConnectable }) => {

  return (
    <>
      <DecisionPointShape text={data.label} />
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


export default DecisionPoint