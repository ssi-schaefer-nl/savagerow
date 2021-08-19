import { grey } from '@material-ui/core/colors';
import React from 'react';

export default [
  {
    id: '1',
    type: 'startNode',
    data: {
      color: grey[200],
      target: 'left',
      source: 'right',
      label: (
        <>
          Data Trigger
        </>
      ),
    },
    position: { x: 250, y: 0 },
  },
];