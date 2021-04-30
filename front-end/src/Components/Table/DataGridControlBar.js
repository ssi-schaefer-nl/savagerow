import React, { Component, useEffect, useState } from 'react';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import { green, grey, yellow } from '@material-ui/core/colors';
import StorageIcon from '@material-ui/icons/Storage';
import InfoIcon from '@material-ui/icons/Info';
import AddIcon from '@material-ui/icons/Add';

const DataGridControlBar = (props) => {
    const rowCount = props.rowCount
    const columnCount = props.columnCount
    var actions = []
    actions.push(<BottomNavigationAction label={"Rows: " + rowCount} icon={<StorageIcon />} />)
    actions.push(<BottomNavigationAction label={"Add Column"} icon={<AddIcon />} />)
    
    return (
      <BottomNavigation showLabels style={{ backgroundColor: grey[50], borderWidth: 0.5, borderStyle: "solid", borderColor: grey[300] }}>
        {actions}
      </BottomNavigation>
    )
  }
  
export default DataGridControlBar