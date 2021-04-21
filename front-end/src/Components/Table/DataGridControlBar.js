import React, { Component, useEffect, useState } from 'react';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import { green, grey, yellow } from '@material-ui/core/colors';
import StorageIcon from '@material-ui/icons/Storage';

const DataGridControlBar = (props) => {
    const rowCount = props.rowCount
    const columnCount = props.columnCount
  
    return (
      <BottomNavigation showLabels style={{ backgroundColor: grey[50], borderWidth: 0.5, borderStyle: "solid", borderColor: grey[300] }}>
        <BottomNavigationAction label={"Rows: " + rowCount} icon={<StorageIcon />} />
        <BottomNavigationAction label={"Columns: " + columnCount} icon={<StorageIcon />} />
      </BottomNavigation>
    )
  }
  
export default DataGridControlBar