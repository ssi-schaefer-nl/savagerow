import React from 'react';

export default [
  {
    id: '1',
    type: 'input',
    data: {
      label: (
        <>
          <strong>E-mail Alert</strong>
        </>
      ),
    },
    position: { x: 250, y: 0 },
  },
  {
    id: '2',
    data: {
      label: (
        <>
          <strong>Data Update</strong>
        </>
      ),
    },
    position: { x: 500, y: 0 },
  },
  {
    id: '3',
    data: {
      label: (
        <>
          <strong>Event Log</strong>
        </>
      ),
    },
    position: { x: 750, y: 0 },
    style: {
      background: '#D6D5E6',
      color: '#333',
      border: '1px solid #222138',
      width: 180,
    },
  }, ,
  {
    id: 'e1-2',
    source: '1',
    target: '2',
    type: 'smoothstep',
    arrowHeadType: 'arrowclosed'
  },
  {
    id: 'e2-3',
    source: '2',
    type: 'smoothstep',

    arrowHeadType: 'arrowclosed',
    target: '3'
  },
  {
    id: 'e3-4',
    source: '3',
    target: '4',
    animated: true,
    label: 'animated edge',
  },
  {
    id: 'e4-5',
    source: '4',
    target: '5',
    arrowHeadType: 'arrowclosed',
    label: 'edge with arrow head',
  },
  {
    id: 'e5-6',
    source: '5',
    target: '6',
    type: 'smoothstep',
    label: 'smooth step edge',
  },
  {
    id: 'e5-7',
    source: '5',
    target: '7',
    type: 'step',
    style: { stroke: '#f6ab6c' },
    label: 'a step edge',
    animated: true,
    labelStyle: { fill: '#f6ab6c', fontWeight: 700 },
  },
];