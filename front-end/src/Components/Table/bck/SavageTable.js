import React from "react";
import DataGrid, { TextEditor } from "react-data-grid";
import "./styles.css";
import './react-contextmenu.css'

import TableDataService from '../../Service/TableDataServices';
import { CircularProgress } from "@material-ui/core";
import { Colorize } from "@material-ui/icons";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import Button from '@material-ui/core/Button';
import CloseIcon from '@material-ui/icons/Close';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Typography from '@material-ui/core/Typography';
import CollapsableAlert from "../CollapsableAlert/CollapsableAlert";
import ContentWithContextMenu from "../ContentWithContextMenu/ContentWithContextMenu";



class SavageTable extends React.Component {
    state = {
        rows: [],
        rowStagedForInsertion: -1,
        newRows: [],
        columns: [],
        loading: true,
        selectedRow: null,
        alert: null,
        tableDataService: new TableDataService(),
        mouseX: null,
        mouseY: null,
    };

    componentDidMount() {
        this.loadTableData()
    }

    loadTableData = () => {
        this.state.tableDataService.getTableData(this.props.table,
            function (data) {
                this.setState({ columns: this.createColumns(data.data.tableSchema.columns) })
                this.setState({ rows: data.data.data })
                this.setState({ loading: false })
                var emptyRow = {}
                this.state.columns.forEach(col => { emptyRow[col.key] = "" })
                var data = this.state.rows
                var idx = data.push(emptyRow) - 1
                this.setState({ rows: data, newRows: [idx] })
            }.bind(this),
            function (data) {
                console.log(data)
                this.setState({ loading: false })
            }.bind(this));
    }

    createColumns(columns) {
        return columns.map(col => {
            var def = {
                key: col.column,
                name: col.column,
                resizable: true,
                nullable: col.nullable
            }
            if (col.editable) {
                def.editor = TextEditor
            }
            return def;
        })
    }

    render() {
        if (this.state.loading) {
            return (<CircularProgress />)
        }
        return (

            <div>
                {this.state.alert && <CollapsableAlert severity={this.state.alert.severity} message={this.state.alert.message} />}
                <ContentWithContextMenu
                    content={
                        <DataGrid
                            columns={this.state.columns}
                            rows={this.state.rows}
                            rowGetter={i => this.state.rows[i]}
                            onRowsChange={this.rowsChanged}
                            enableCellSelect={true}
                            style={{ resize: 'both', 'height': "70vh", maxWidth: "100%" }}
                            className="fill-grid"
                            minHeight="1000"
                            onSelectedCellChange={this.handleCellChange}
                        />
                    }
                    menuItems={Object.keys(this.contextMenuItems)}
                    action={(a) => this.contextMenuItems[a]()}
                />
                <BottomNavigation
                    showLabels
                    className={classes.root}
                >
                    <BottomNavigationAction label="database" icon={<RestoreIcon />} />
                    <BottomNavigationAction label="Favorites" icon={<FavoriteIcon />} />
                    <BottomNavigationAction label="Nearby" icon={<LocationOnIcon />} />
                </BottomNavigation>
            </div >
        );
    }

    contextMenuItems = {
        "Insert": () => { this.insertEmptyRow() },
        "Delete": () => { this.deleteRow() },
        "Save": () => { this.saveSelectRowManually() },
        "Reload": () => { this.loadTableData() }
    }


    saveSelectRowManually = () => {
        if (this.rowStagedForInsertHasAllData()) {
            this.addRow(this.state.rowStagedForInsertion)
        }
    }

    handleCellChange = (c) => {
        console.log(c)
        if (this.shouldProcessStagedRowInsert(c.rowIdx)) {
            console.log("Changed row selection and a row is staged for insertion")
            this.addRow(this.state.rowStagedForInsertion)
        }
        this.state.selectedRow = c.rowIdx
    }

    shouldProcessStagedRowInsert(rowIdx) {
        if (rowIdx != this.state.selectedRow && this.state.rowStagedForInsertion != -1) { // Changed row selection and a row is staged for insertion
            return this.rowStagedForInsertHasAllData()
        }
        return false;
    }

    rowStagedForInsertHasAllData() {
        var row = this.state.rows[this.state.rowStagedForInsertion]
        var colsRequiringValue = this.state.columns.filter(col => {
            if (!col.nullable && col.editor && !row[col.key]) {
                return col.key
            }
        })

        if (colsRequiringValue.length == 0) {
            this.setState({ alert: null })
            return true

        }

        alert = {
            severity: "warning",
            message: "The following columns require a value before entry is stored:" + colsRequiringValue.map(col => " " + col.name),
            row: this.state.rowStagedForInsertion
        }

        this.setState({ alert: alert })
        return false
    }


    insertEmptyRow = () => {
        var idx = this.state.selectedRow;

        var emptyRow = {}
        this.state.columns.forEach(col => { emptyRow[col.key] = "" })
        var data = this.state.rows

        data.splice(idx, 0, emptyRow) //Insert empty row at index idx
        this.state.newRows.push(idx)
        this.setState({ rows: data })

    }

    deleteRow = () => {
        var idx = this.state.selectedRow
        var data = { row: this.state.rows[idx] }
        if (this.state.newRows.includes(idx)) {
            var data = this.state.rows
            data.splice(idx, 1)
            this.setState({ rows: data })
        }
        else {
            this.state.tableDataService.deleteTableData(this.props.table, data,
                function (data) {
                    var data = this.state.rows
                    data.splice(idx, 1)
                    this.setState({ rows: data })
                }.bind(this),
                function (data) {
                    console.log(data)
                }.bind(this));
        }
    }


    rowsChanged = (rows, index) => {
        var indexOfChangedRow = index.indexes[0];
        var oldInstanceOfChangedRow = this.state.rows[indexOfChangedRow];
        var newInstanceOfChangedRow = rows[indexOfChangedRow];

        if (!this.state.newRows.includes(indexOfChangedRow)) { //Changed row is not new, so we can update right away
            this.updateRow(oldInstanceOfChangedRow, newInstanceOfChangedRow, indexOfChangedRow)
        }
        else { // Row is new, so we store locally and wait for a change in row before pushing the new row
            this.setState({ rowStagedForInsertion: indexOfChangedRow })
            var rows = this.state.rows;
            rows[indexOfChangedRow] = newInstanceOfChangedRow;
            this.setState({ rows: rows })
        }

    }

    addRow = (indexOfChangedRow) => {
        var data = { row: this.state.rows[indexOfChangedRow] }
        var newRowsEntryIndex = this.state.newRows.indexOf(indexOfChangedRow)
        this.state.tableDataService.addTableData(this.props.table, data,
            function (data) {
                this.state.newRows.splice(newRowsEntryIndex, 1); // Unmark row as new
            }.bind(this),
            function (data) {
                console.log(data)
            }.bind(this));

        this.setState({ rowStagedForInsertion: -1 }) // unset the row as staged for insertion 

    }

    updateRow = (oldRow, newRow, index) => {
        var data = [
            {
                oldRow: oldRow,
                newRow: newRow
            },
        ]

        this.state.tableDataService.updateTableData(this.props.table, data,
            function (data) {
                var rows = this.state.rows;
                rows[index] = newRow;
                this.setState({ rows: rows })
            }.bind(this),
            function (data) {
                console.log(data)
            }.bind(this));

    }

}

export default SavageTable;