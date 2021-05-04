import React, { Component, useEffect, useState } from 'react';
import DataGridControlBar from './DataGridControlBar';
import DataGridTable from './DataGridTable';
import NotificationArea from '../NotificationArea/NotificationArea';
import TableService from './TableService';

const SavageTable = (props) => {
  const tableService = new TableService(props.table)
  const [rows, setRows] = useState([])
  const [columnFilter, setColumnFilter] = useState([])
  const [columns, setColumns] = useState([])
  const [notifications, setNotifications] = useState([])
  const [insertedRows, setInsertedRows] = useState([])
  const [errorRows, setErrorRows] = useState([])


  const addNotification = (content, severity) => {
    const notification = { content: content, severity: severity }
    setNotifications(notifications => [...notifications, notification])
  }

  const removeErrorsForRow = id => {
    setErrorRows(er => er.filter(r => r.id != id))
  }

  const addErrorRow = (id, message) => {
    const error = { id: id, message: message }
    removeErrorsForRow(id)
    setErrorRows(er => [...er, error])
  }

  const loadTableRows = () => {
    setInsertedRows([])
    setNotifications([])
    setErrorRows([])
    tableService.getRowSet(data => setRows(data.data.rows), () => addNotification("Unable to fetch table rows for database: " + localStorage.getItem("database"), "error"))

  }

  const insertAction = (rId, before) => {
    if (!before)
      rId = rId + 1

    setInsertedRows(irs => [...irs.map(ir => ir >= rId ? ir + 1 : ir), rId])
    setRows(tableService.addRow(rows, rId))
    setErrorRows([])
  }

  const deleteAction = (rId) => {
    tableService.delete(rows, rId, (resultingRows) => {
      setInsertedRows(irs => irs.filter(r => r != rId).map(ir => ir > rId ? ir - 1 : ir))
      setRows(resultingRows)
      setErrorRows([])
    }, (e) => addErrorRow(rId, e))
  }

  const saveAction = (rId) => {
    if (insertedRows.indexOf(rId) == -1) return

    tableService.save(rows, rId, (resultingRows) => {
      setRows(resultingRows)
      setInsertedRows(irs => irs.filter(r => r != rId))
      removeErrorsForRow(rId)
    }, (e) => addErrorRow(rId, " Error saving the row: " + e))
  }

  const handleRowChange = (newRow, index) => {
    if (insertedRows.indexOf(index) == -1)
      tableService.update(rows, newRow, index, (rows) => {
        setRows(rows)
        removeErrorsForRow(index)
      }, (e) => {
        var message = e.message.length == 0 ? " Undefined error during row update" : " Error updating row: " + e.message
        addErrorRow(index, message)
        console.log(e)
      })
    else
      setRows(tableService.updateLocal(rows, newRow, index))
  }

  useEffect(() => {
    loadTableRows()
    tableService.getSchema(data => setColumns(data.data.columns), () => addNotification("Unable to fetch table schema", "error"))
  }, [])

  var highlightedRowsFinal = []
  if (errorRows.length > 0) {
    highlightedRowsFinal = highlightedRowsFinal.concat(errorRows.slice(0).reverse().map(er => { return { id: er.id, message: er.message, type: "error" } }))
  }

  if (insertedRows.length > 0) {
    highlightedRowsFinal = highlightedRowsFinal.concat(insertedRows.map(i => { return { id: i, message: "This row is not saved", type: "warning" } }))
  }


  return (
    <div>
      <NotificationArea
        notifications={notifications}
        handleClose={(index) => setNotifications(curr => curr.filter((notifications, i) => i !== index))}
      />
      <DataGridControlBar rowCount={rows.length} columns={columns} onChangeColumnFilter={setColumnFilter} />

      <DataGridTable
        rows={rows}
        columns={columnFilter.length > 0 ? columns.filter(c => columnFilter.includes(c.column)) : columns}
        highlightRows={highlightedRowsFinal}
        onRowChange={handleRowChange}
        onDelete={deleteAction}
        onInsertAbove={(idx) => insertAction(idx, true)}
        onInsertBelow={(idx) => insertAction(idx, false)}
        onSave={saveAction}
        onRefresh={loadTableRows}
      />
    </div>
  )
}

export default SavageTable