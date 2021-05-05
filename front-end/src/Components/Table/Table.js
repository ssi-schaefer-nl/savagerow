import React, { Component, useEffect, useState } from 'react';
import DataGridTable from './DataGrid/DataGridTable';
import NotificationArea from '../NotificationArea/NotificationArea';
import DataGridControlBar from './DataGridControlBar/DataGridControlBar';
import TableService from '../../Service/TableService';
import DefineColumnDialog from '../DefineColumnDialog/DefineColumnDialog';
import AddColumn from './AddColumn/AddColumn';

const SavageTable = (props) => {

  const tableService = new TableService(props.table)
  const [rows, setRows] = useState([])
  const [columnFilter, setColumnFilter] = useState([])
  const [columns, setColumns] = useState([])
  const [notifications, setNotifications] = useState([])
  const [insertedRows, setInsertedRows] = useState([])
  const [errorRows, setErrorRows] = useState([])
  const [addColumnDialogOpen, setAddColumnDialogOpen] = useState(false)


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

  const reloadSchema = () => {
    tableService.getSchema(data => setColumns(data.data.columns), () => addNotification("Unable to fetch table schema", "error"))
    tableService.getRowSet(data => setRows(data.data.rows), () => addNotification("Unable to fetch table rows for database: " + localStorage.getItem("database"), "error"))
  }

  const insertAction = (rId) => {
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
        addErrorRow(index, " Error updating row: " + e)
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
        onInsert={(idx) => insertAction(idx, true)}
        onSave={saveAction}
        onRefresh={loadTableRows}
        onColumnDelete={(col) => {
          tableService.dropColumn(col, () => setColumns(columns => columns.filter(c => c.column != col)), (e) => {addNotification(e, "error")})
        }}

        onColumnRename={(old, newName) => {console.log(old + " to " + newName)}}
        onColumnInsert={() => setAddColumnDialogOpen(true)}
      />
      <AddColumn
        open={addColumnDialogOpen}
        table={props.table}
        handleClose={() => setAddColumnDialogOpen(false)}
        onSuccess={reloadSchema}
      />
    </div>
  )
}

export default SavageTable