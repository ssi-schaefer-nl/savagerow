import DataGrid, { TextEditor, Row as GridRow } from "react-data-grid";
import React, { useEffect, useState } from 'react';
import Card from '@material-ui/core/Card';
import Pagination from '@material-ui/lab/Pagination';
import Toolbar from '@material-ui/core/Toolbar';

import Popover from '@material-ui/core/Popover';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Paper, makeStyles } from '@material-ui/core';

import Button from '@material-ui/core/Button';

import { CircularProgress, Grid, Select, Typography } from '@material-ui/core';

import QueryService from "../../../Service/QueryService/QueryService";

const useStyles = makeStyles({
    root: {
        width: '70vw',
    },
    container: {
        maxHeight: 440,
    },
});


const ReferencedRowSelection = ({ row, onRowChange, column }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const fkTable = column.fk.split(".")[0]
    const fkColumn = column.fk.split(".")[1]
    const [rows, setRows] = useState([])

    useEffect(() => {
        const queryService = new QueryService(fkTable);
        queryService.getRowSet(data => setRows(data.data), () => undefined)

    }, [])
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };


    return (
        <>
            <Button
                onClick={handleClick}
                
                style={{
                    appearance: 'none',
                    width: '100%',
                    height: '100%',
                    padding: '1px 6px 0 6px',
                    verticalAlign: 'top',
                    justifyContent: "flex-start"
                }}
                fullWidth={true}
            >
                <Typography>{row[column.key]}</Typography>

            </Button>

            <Popover
                open={Boolean(anchorEl)}
                anchorReference="anchorPosition"
                anchorPosition={{ top: 150, left: 200 }}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'left',
                }}
                onClose={handleClose}
            >
                <StickyHeadTable fkColumn={fkColumn} rows={rows} onRowChange={onRowChange} column={column} row={row} />
            </Popover>
        </>
    );
}



function StickyHeadTable({ fkColumn, rows, column, onRowChange, row }) {
    const classes = useStyles();
    const [page, setPage] = React.useState(1);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [columns, setColumns] = useState([])
    const [filter, setFilter] = useState("")

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };


    useEffect(() => {
        if (rows[0] != undefined)
            setColumns(Object.keys(rows[0]))
    }, [rows])

    let filteredRows = rows

    if (filter.length > 0) filteredRows = rows.filter(r => Object.values(r).join(' ').toLowerCase().includes(filter))
    console.log(filteredRows.length)
    return (
        <Paper className={classes.root}>
            <TableContainer className={classes.container} style={{ overflowY: "hidden" }}>
                <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                        <TableRow>
                            <TableCell
                                key={fkColumn}
                                align={"left"}
                            >
                                {fkColumn}
                            </TableCell>
                            {columns.filter(c => c != "rowid" && c != fkColumn).map((column, i) => (
                                <TableCell
                                    key={column}
                                    align="right"
                                >
                                    {column}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredRows.slice((page - 1) * rowsPerPage, (page - 1) * rowsPerPage + rowsPerPage).map((r) => {
                            return (
                                <TableRow hover
                                    role="checkbox"
                                    tabIndex={-1}
                                    key={r[fkColumn]}
                                    onClick={() => onRowChange({ ...row, [column.key]: r[fkColumn] }, true)}
                                >
                                    <TableCell key={fkColumn} align="left">
                                        {r[fkColumn]}
                                    </TableCell>
                                    {columns.filter(c => c != "rowid" && c != fkColumn).map((column, i) => {
                                        const value = r[column];
                                        return (
                                            <TableCell key={column} align="right">
                                                {value}
                                            </TableCell>
                                        );
                                    })}
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
            <Toolbar>
                <Grid direction="row" container justify="space-between" style={{ marginRight: "2em" }}>
                    <Grid item>
                        <Pagination count={Math.ceil(filteredRows.length / rowsPerPage)} page={page} onChange={handleChangePage} />
                    </Grid>
                    <Grid item xs={3}>
                        <TextField
                            value={filter}
                            onChange={(e) => setFilter(e.target.value.toLowerCase())}
                            label="Search"
                            type="search"
                            placeholder="Type here to search through the rows"
                            variant="outlined"
                            size="small"
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                        />
                    </Grid>
                </Grid>
            </Toolbar>
        </Paper>
    );
}

export default ReferencedRowSelection