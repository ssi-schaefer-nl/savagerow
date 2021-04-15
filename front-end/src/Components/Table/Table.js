import React, { Component } from 'react';
import DataGrid, { TextEditor } from "react-data-grid";

import TableDataService from '../../Service/TableDataServices';
import { CircularProgress, Typography } from '@material-ui/core';
import CollapsableAlert from '../CollapsableAlert/CollapsableAlert';
import ContentWithContextMenu from '../ContentWithContextMenu/ContentWithContextMenu';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import { green, grey, yellow } from '@material-ui/core/colors';
import SyncIcon from '@material-ui/icons/Sync';
import StorageIcon from '@material-ui/icons/Storage';

class SavageTable extends Component {
  state = {
    tableDataService: new TableDataService(),
    alert: null,
    columns: [],
    rows: [],
    unsavedRowsIndices: [], // Rows that are inserted to the table but not yet saved to the database
    dataLoadingError: false,
    loading: true,
  }

  componentDidMount() {
    this.loadTableData()
  }

  loadTableData = () => {
    this.state.tableDataService.getTableData(this.props.table,
      function (data) {
        var rows = data.data.data
        var columns = data.data.tableSchema.columns.map(col => ({
          key: col.column,
          name: col.column,
          resizable: true,
          nullable: col.nullable,
          editor: col.editable ? TextEditor : undefined
        }));
        this.setState({
          columns: columns,
          rows: rows,
          loading: false,
          indexOfSelectedRow: rows.length,
          unsavedRowsIndices: []
        })

      }.bind(this),
      function (data) {
        console.log(data)
        this.setState({ loading: false, dataLoadingError: true })
      }.bind(this));
  }

  render() {
    if (this.state.loading) {
      return (<CircularProgress />)
    }
    if (this.state.dataLoadingError) {
      window.location.reload(false)
    }
    return (
      <div>
        {this.state.alert &&
          <CollapsableAlert
            severity={this.state.alert.severity}
            message={this.state.alert.message}
            onClose={() => this.setState({ alert: null })}
          />}
        <ContentWithContextMenu
          content={
            <DataGrid
              columns={this.state.columns}
              rows={this.state.rows}
              rowGetter={i => this.state.rows[i]}
              onRowsChange={this.update}
              enableCellSelect={true}
              style={{ 'height': "70vh", overflowX: 'hidden' }}
              className="fill-grid"
              minHeight="1000"
              onSelectedCellChange={this.handleCellChange}
            />
          }
          menuItems={Object.keys(this.contextMenuItems)}
          action={(a) => this.contextMenuItems[a]()}
        />
        <BottomNavigation showLabels style={{backgroundColor: grey[50], borderWidth:0.5, borderStyle:"solid", borderColor:grey[300]}}>
          {this.state.unsavedRowsIndices.length > 0 &&
            <BottomNavigationAction
              label="Unsaved rows"
              icon={<SyncIcon style={{ color: yellow[500] }} />}
            />
          }

          <BottomNavigationAction label={"Rows: " + this.state.rows.length} icon={<StorageIcon />} />
          <BottomNavigationAction label={"Columns: " + this.state.columns.length} icon={<StorageIcon />} />
        </BottomNavigation>
      </div >
    );
  }

  contextMenuItems = {
    "Insert": () => { this.insertRow() },
    "Delete": () => { this.deleteRow() },
    "Save": () => { this.save(true) },
    "Reload": () => { this.loadTableData() }
  }

  handleCellChange = (c) => {
    if (c.rowIdx != this.state.indexOfSelectedRow && this.state.unsavedRowsIndices.includes(this.state.indexOfSelectedRow)) {
      this.save(false)
    }
    this.state.indexOfSelectedRow = c.rowIdx

  }

  insertRow = () => {
    var insertRowBeforeIndex = this.state.indexOfSelectedRow;
    var newRow = {}
    this.state.columns.forEach(col => { newRow[col.key] = "" })
    var rows = this.state.rows

    rows.splice(insertRowBeforeIndex, 0, newRow) //Insert empty row at index idx
    console.log("inserted at " + insertRowBeforeIndex)

    this.setState({ rows: rows })
    this.state.unsavedRowsIndices = this.state.unsavedRowsIndices.map(i => (
      insertRowBeforeIndex <= i ? i + 1 : i
    ))

    this.state.unsavedRowsIndices.push(insertRowBeforeIndex)
  }

  deleteRow = () => {
    var rowSelectedForDeletion = this.state.indexOfSelectedRow

    if (this.state.unsavedRowsIndices.includes(rowSelectedForDeletion)) {
      this.removeRowFromTable(rowSelectedForDeletion)
    }
    else {
      var data = { row: this.state.rows[rowSelectedForDeletion] }

      this.state.tableDataService.deleteTableData(
        this.props.table,
        data,
        function () {
          this.removeRowFromTable(rowSelectedForDeletion)
        }.bind(this),
        function (data) {
          console.log(data)
        }.bind(this));
    }
  }

  removeRowFromTable = (rowIndex) => {
    var data = this.state.rows
    data.splice(rowIndex, 1)
    var indexInUnsavedRowIndex = this.state.unsavedRowsIndices.indexOf(rowIndex)
    if (indexInUnsavedRowIndex != -1) {
      this.state.unsavedRowsIndices.splice(indexInUnsavedRowIndex, 1)
    }
    this.setState({ rows: data })
  }

  save = (explicitSave) => {
    var columnsWithMissingData = []
    var alert = null

    this.state.unsavedRowsIndices.forEach(unsavedRowIndex => {
      var unsavedRow = this.state.rows[unsavedRowIndex]
      var colsRequiringValue = this.state.columns.filter(col => {
        if (!col.nullable && col.editor && !unsavedRow[col.key]) {
          if (columnsWithMissingData.indexOf(col.key) === -1) {
            columnsWithMissingData.push(col.key);
          }
          return col.key
        }
      })
      var emptyColumns = Object.keys(unsavedRow).filter(col => {if(unsavedRow[col] == "") return col})
      console.log(emptyColumns.length + "   " + Object.keys(unsavedRow).length)
      if (colsRequiringValue.length == 0 && emptyColumns.length < Object.keys(unsavedRow).length) {
        this.saveRow(unsavedRowIndex)
      }
    })

    if (explicitSave || this.state.alert != null) {
      if (columnsWithMissingData.length > 0) {
        alert = {
          severity: "warning",
          message: "One or more rows have incomplete data and cannot be stored (missing column:" + columnsWithMissingData.map(col => " " + col) + ")",
        }
      }

    }
    this.setState({ alert: alert })
  }

  saveRow = (rowIndex) => {
    var data = { row: this.state.rows[rowIndex] }
    console.log(data)
    this.state.tableDataService.addTableData(this.props.table, data,
      function (data) {
        this.state.rows[rowIndex] = data.data.row
        var idx = this.state.unsavedRowsIndices.indexOf(rowIndex)
        console.log(idx)
        this.state.unsavedRowsIndices.splice(idx, 1); // Unmark row as new
        this.forceUpdate();

      }.bind(this),
      function (data) {
        console.log(data)
      }.bind(this));
  }

  update = (updatedRows, index) => {
    var indexOfUpdatedRow = index.indexes[0];
    var newInstanceOfUpdatedRow = updatedRows[indexOfUpdatedRow];

    if (this.state.unsavedRowsIndices.includes(indexOfUpdatedRow)) {
      var rows = this.state.rows;
      rows[indexOfUpdatedRow] = newInstanceOfUpdatedRow;
      this.setState({ rows: rows })
      return
    }

    var oldInstanceOfUpdatedRow = this.state.rows[indexOfUpdatedRow];
    var newInstanceOfUpdatedRow = updatedRows[indexOfUpdatedRow];
    var data = { oldRow: oldInstanceOfUpdatedRow, newRow: newInstanceOfUpdatedRow }
    
    var rows = this.state.rows;
    this.state.rows[indexOfUpdatedRow] = newInstanceOfUpdatedRow;
    this.setState({ rows: rows })

    this.state.tableDataService.updateTableData(this.props.table, data,
      function () {
 
      }.bind(this),
      function (data) {
        var rows = this.state.rows;
        this.state.rows[indexOfUpdatedRow] = oldInstanceOfUpdatedRow;
        this.setState({ rows: rows })
      }.bind(this));

  }
}



export default SavageTable;