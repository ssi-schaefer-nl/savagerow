import { Button, CircularProgress, Grid } from '@material-ui/core';
import { DataGrid } from '@mui/x-data-grid';
import * as React from 'react';
import AddRowDialog from './AddRowDialog';
import TableService from './TableService';

export default function DataTable({ table }) {
    const [schema, setSchema] = React.useState(null)
    const [rows, setRows] = React.useState(null)
    const service = new TableService()
    const [selectionModel, setSelectionModel] = React.useState([]);
    const [reload, setReload] = React.useState(false)
    const [addRow, setAddRow] = React.useState(false)

    React.useEffect(() => {
        if (table != undefined) {
            setSchema(null)
            setRows(null)
            const service = new TableService()
            service.getRows(table, setRows, console.log)
            service.getSchema(table, (s) => setSchema(s.columns), console.log)
        }
    }, [table, reload])

    const handleCellEditCommit = React.useCallback(({ id, field, value }) => {
        let updatedRow = {}

        const updatedRows = rows.map((r) => {
            if (r.rowid === id) {
                const row = { ...r, [field]: value }
                updatedRow = row
                return row
            }
            return r;
        });

        service.update(table, updatedRow, () => setRows(updatedRows), console.log)
        setRows(updatedRows);
    }, [rows]);

    const deleteSelectionModel = () => {
        service.delete(table, selectionModel, () => {
            service.getRows(table, setRows, console.log)
        }, console.log)
    }

    const handleAddRow = () => {
        setAddRow(true)
    }

    const handleSaveRow = (row) => {
        service.add(table, row, () => {
            setReload(true)
            setAddRow(false)
        }, console.log)
    }

    if (schema == undefined || rows == undefined)
        return (
            <Grid container alignItems='center' justify='center' style={{ height: "100%" }}>
                <CircularProgress />
            </Grid>
        )

    return (
        <div style={{ height: "100%", width: '100%' }}>
            <DataGrid
                rows={rows}
                columns={createSchema(schema)}
                onCellEditCommit={handleCellEditCommit}
                onSelectionModelChange={(newSelectionModel) => {
                    setSelectionModel(newSelectionModel);
                }}
                selectionModel={selectionModel}

                getRowId={(row) => row.rowid}
                autoPageSize
                checkboxSelection
                disableSelectionOnClick
            />
            <Button variant="contained" color="primary" style={{ margin: '1em' }} onClick={handleAddRow}>Add</Button>
            {selectionModel.length > 0 &&
                <Button variant="contained" color="secondary" style={{ margin: '1em' }} onClick={deleteSelectionModel}>Delete</Button>
            }
            <AddRowDialog open={addRow} schema={schema} onAdd={handleSaveRow} handleClose={() => setAddRow(false)} />
        </div>
    );
}

const createSchema = (schema) => {
    return schema.map(c => (
        {
            field: c.name,
            headerName: c.name,
            minWidth: 50,
            flex: 1,
            editable: !c.pk,
        }))

}
