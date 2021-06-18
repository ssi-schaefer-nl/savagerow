import DataGrid, { TextEditor, Row as GridRow } from "react-data-grid";
import React, { useEffect, useState } from 'react';
import Card from '@material-ui/core/Card';
import TablePagination from '@material-ui/core/TablePagination';

import Popover from '@material-ui/core/Popover';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, makeStyles } from '@material-ui/core';

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
                    boxSizing: 'border-box',
                    width: '100%',
                    height: '100%',
                    padding: '1px 6px 0 6px',
                    border: '2px solid #ccc',
                    verticalAlign: 'top',
                    justifyContent: "flex-start"
                }}
                fullWidth={true}
            >
                <Typography>{row[column.key]}</Typography>

            </Button>

            <Popover
                open={Boolean(anchorEl)}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
                transformOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                }}
            >
                <Content column={column} row={row} onRowChange={onRowChange} />
            </Popover>
        </>
    );
}

const Content = ({ column, onRowChange, row }) => {
    const fkTable = column.fk.split(".")[0]
    const fkColumn = column.fk.split(".")[1]
    const [rows, setRows] = useState([])

    useEffect(() => {
        const queryService = new QueryService(fkTable);
        queryService.getRowSet(data => setRows(data.data), () => undefined)

    }, [])

    return (
        <StickyHeadTable fkColumn={fkColumn} rows={rows} onRowChange={onRowChange} column={column} row={row}/>
    )
}


function StickyHeadTable({fkColumn, rows, column, onRowChange, row }) {
    const classes = useStyles();
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [columns, setColumns] = useState([])
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };


    useEffect(() => {
        if (rows[0] != undefined)
            setColumns(Object.keys(rows[0]))
    }, [rows])

    return (
        <Paper className={classes.root}>
            <TableContainer className={classes.container} style={{ overflowY: "hidden" }}>
                <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                        <TableRow>
                            {columns.map((column, i) => (
                                <TableCell
                                    key={column}
                                    align={i == 0 ? "left" : "right"}
                                >
                                    {column}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((r) => {
                            return (
                                <TableRow hover
                                    role="checkbox"
                                    tabIndex={-1}
                                    key={r}
                                    onClick={() => onRowChange({ ...row, [column.key]: r[fkColumn] }, true)}>
                                    {columns.map((column, i) => {
                                        const value = r[column];
                                        return (
                                            <TableCell key={column} align={i == 0 ? "left" : "right"}>
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
            <TablePagination
                component="div"
                count={rows.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onChangePage={handleChangePage}
            />
        </Paper>
    );
}

export default ReferencedRowSelection