import React, { Component, useEffect, useState } from 'react';
import TableService from '../../Service/TableService';
import ContextMenu from '../ContextMenu/ContextMenu';
import DataGridControlBar from './DataGridControlBar';
import DataGridTable from './DataGridTable';
import NotificationArea from './NotificationArea';
import TableManager from './TableManager';



const createNotification = (content, severity) => {
  return {
    content: content,
    severity: severity,
  }
}

const SavageTable = (props) => {
  const tableManager = new TableManager(props.table)
  const [selectedRow, setSelectedRow] = useState(null)
  const [rows, setRows] = useState([])
  const [columns, setColumns] = useState([])
  const [notifications, setNotifications] = useState([])

  const addNotification = (notification) => {
    setNotifications(notifications => [...notifications, notification])
  }
  
  const loadTableRows = () => {
    tableManager.getRowSet(data => setRows(data.data.rows), () => addNotification(createNotification("Unable to fetch table rows", "error")))
  }

  useEffect(() => {
    loadTableRows()
    tableManager.getSchema(data => setColumns(data.data.columns), () => addNotification(createNotification("Unable to fetch table schema", "error")))
  }, [])


  const contextMenuActions = {
    "Insert": () => setRows(tableManager.addRow(rows, selectedRow)),
    "Delete": () => tableManager.delete(rows, selectedRow, (rows) => setRows(rows), (e) => addNotification(createNotification("Unable to delete row: " + e, "warning"))),
    "Save": () => tableManager.save(rows, selectedRow, (rows) => setRows(rows), (e) => addNotification(createNotification("Unable to save data: " + e, "warning"))),
    "Reload": loadTableRows
  }

  return (
    <>
      <NotificationArea
        notifications={notifications}
        handleClose={(index) => setNotifications(curr => curr.filter((notifications, i) => i !== index))}
      />

      <ContextMenu
        menuItems={Object.keys(contextMenuActions)}
        onItemClick={(a) => contextMenuActions[a]()}
      >

        <DataGridTable
          rows={rows}
          columns={columns}
          onRowChange={(newRow, index) => 
            tableManager.update(rows, newRow, index, (rows) => setRows(rows), (e) => addNotification(createNotification("Unable to update row: " + e, "warning")))
          }
          onRowSelect={setSelectedRow}
        />

      </ContextMenu>

      <DataGridControlBar
        rowCount={rows.length}
        columnCount={columns.length}
      />
    </>
  )
}


export default SavageTable