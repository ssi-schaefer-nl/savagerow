import React, { Component, useEffect, useState } from 'react';
import DataGridTable from './DataGrid/DataGridTable';
import NotificationArea from '../../Components/NotificationArea/NotificationArea';
import AddColumn from '../../Components/AddColumn/AddColumn';
import QueryService from '../../Service/QueryService/QueryService';

import DefinitionService from '../../Service/DefinitionService/DefinitionService';
import ManipulationService from '../../Service/ManipulationService/ManipulationService';
import { Box, Button, CircularProgress, Grid } from '@material-ui/core';
import DefineColumnDialog from '../../Components/DefineColumnDialog/DefineColumnDialog';
const SavageTable = (props) => {

  const queryService = new QueryService(props.table)
  const definitionService = new DefinitionService(props.table)
  const manipulationService = new ManipulationService(props.table)
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
    setErrorRows([])
    setNotifications([])
    queryService.getRowSet(data => setRows(data.data), () => addNotification("Unable to fetch table rows for database: " + localStorage.getItem("database"), "error"))
  }

  const reloadSchema = () => {
    queryService.getSchema(data => { setColumns(data.data.columns) }, () => addNotification("Unable to fetch table schema", "error"))
    queryService.getRowSet(data => {
      if (insertedRows.length > 0) insertedRows.forEach(i => setRows(manipulationService.addRow(data.data, i, columns)))
      else setRows(data.data)
    }, () => addNotification("Unable to fetch table rows for database: " + localStorage.getItem("database"), "error"))

  }

  const insertAction = (rId) => {
    rId = rId + 1

    setInsertedRows(irs => [...irs.map(ir => ir >= rId ? ir + 1 : ir), rId])
    setRows(manipulationService.addRow(rows, rId, columns))
    setErrorRows([])
    console.log(rows.length)
  }

  const deleteAction = (rId) => {
    if (insertedRows.includes(rId)) {
      setRows(manipulationService.deleteLocal(rows, rId))
      setInsertedRows(irs => irs.filter(r => r != rId).map(ir => ir > rId ? ir - 1 : ir))
    }
    else {
      manipulationService.delete(rows, rId, (resultingRows) => {
        setInsertedRows(irs => irs.filter(r => r != rId).map(ir => ir > rId ? ir - 1 : ir))
        setRows(resultingRows)
        setErrorRows([])
      }, (e) => addErrorRow(rId, e))
    }
  }

  const saveAction = (rId) => {
    if (insertedRows.indexOf(rId) == -1) return

    manipulationService.save(rows, rId, (resultingRows) => {
      setRows(resultingRows)
      setInsertedRows(irs => irs.filter(r => r != rId))
      removeErrorsForRow(rId)
    }, (e) => addErrorRow(rId, " Error saving the row: " + e))
  }

  const handleRowChange = (newRow, index) => {
    if (insertedRows.indexOf(index) == -1)
      manipulationService.update(rows, newRow, index, (rows) => {

        setRows(rows)
        removeErrorsForRow(index)
      }, (e) => {
        addErrorRow(index, " Error updating row: " + e)
        console.log(e)
      })
    else setRows(manipulationService.updateLocal(rows, newRow, index))
  }

  useEffect(() => {
    loadTableRows()
    queryService.getSchema(data => setColumns(data.data.columns), () => addNotification("Unable to fetch table schema", "error"))
  }, [])


  var highlightedRows = []

  if (errorRows.length > 0) highlightedRows = highlightedRows.concat(errorRows.slice(0).reverse().map(er => { return { id: er.id, message: er.message, type: "error" } }))
  if (insertedRows.length > 0) highlightedRows = highlightedRows.concat(insertedRows.map(i => { return { id: i, message: "This row is not saved", type: "warning" } }))


  if (columns.length == 0) {
    return (
      <Grid container spacing={0} direction="column" alignItems="center" justify="center" style={{ minHeight: '100vh' }}>
        <Grid item xs={3}>
          <CircularProgress />
        </Grid>

      </Grid>
    )
  }

  return (
    <div style={{ height: "70vh" }}>
      <DataGridTable

        rows={rows}
        columns={columnFilter.length > 0 ? columns.filter(c => columnFilter.includes(c.name)) : columns}
        highlightRows={highlightedRows}
        onRowChange={handleRowChange}
        onDelete={deleteAction}
        onInsert={insertAction}
        onSave={saveAction}
        onRefresh={loadTableRows}
        onColumnDelete={(col) => definitionService.dropColumn(col, () => setColumns(columns => columns.filter(c => c.name != col)), (e) => addNotification(e.message, "error"))}
        onColumnRename={(old, newName) => definitionService.renameColumn(old, newName, () => reloadSchema(), (e) => addNotification(e.message, "error"))}
        onColumnInsert={() => setAddColumnDialogOpen(true)}
      />
      <NotificationArea
        notifications={notifications}
        handleClose={(index) => setNotifications(curr => curr.filter((notifications, i) => i !== index))}
      />

      {addColumnDialogOpen &&
        <DefineColumnDialog open={addColumnDialogOpen} table={props.table} handleClose={() => setAddColumnDialogOpen(false)} onSuccess={reloadSchema} />
      }
    </div>
  )
}

export default SavageTable