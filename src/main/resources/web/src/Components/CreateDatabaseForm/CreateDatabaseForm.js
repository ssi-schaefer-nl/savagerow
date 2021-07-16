import React, { useState } from 'react';
import TextField from '@material-ui/core/TextField';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Grid, Divider } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/MoreVert';
import Button from '@material-ui/core/Button';
import PopupForm from '../PopupForm/PopupForm';

const CreateDatabaseForm = (props) => {
    const { onCreate } = props
    const [workspaceName, setWorkspaceName] = useState("")
    const [tables, setTables] = useState([])

    return (
        <Paper style={{ padding: "2em" }}>
            <TextField label="Workspace name" required InputLabelProps={{ shrink: true }} style={{ marginBottom: "2em" }} />
            <TablesList tables={[]} />
        </Paper>
    );
}

const TablesList = props => {
    const { tables, onChange } = props
    const [openAddTablesPopup, setOpenAddTablesPopup] = useState(false)

    const addTable = (table) => { onChange( [...tables, table]); setOpenAddTablesPopup(false) }

    return (
        <>
            <TableContainer style={{ padding: "1em", width: "50%" }}>

                <Table  >
                    <TableHead >
                        <TableRow>
                            <TableCell size="small" >
                                Table name
                            </TableCell>
                            <TableCell size="small" >
                                Number of columns
                            </TableCell>
                            <TableCell size="small" >
                                Primary Key(s)
                            </TableCell>
                            <TableCell size="small" align="right">
                                <Button onClick={() => setOpenAddTablesPopup(true)}>
                                    <AddIcon />
                                </Button>
                            </TableCell>

                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {tables.map(t => (
                            <TableRow key={t.name}>
                                <TableCell size="small">
                                    {t.name}
                                </TableCell>
                                <TableCell size="small">
                                    {t.columns.length}
                                </TableCell>
                                <TableCell size="small">
                                    {t.columns.filter(c => c.pk).map(c => c.name).join(", ")}
                                </TableCell>
                                <TableCell size="small">
                                    <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => undefined}>
                                        <EditIcon />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer >
            <AddTablePopup open={openAddTablesPopup} onSubmit={addTable} onClose={() => setOpenAddTablesPopup(false)} />
        </>
    )
}

const AddTablePopup = props => {
    const { open, onSubmit, onClose } = props
    const [columns, setColumns] = useState([])

    return (
        <PopupForm open={open} onSubmit={onSubmit} onClose={onClose} wide title="Define Table">
            <ColumnsList columns={columns} />

        </PopupForm>
    )
}

const ColumnsList = props => {
    const {columns} = props
    return (
        <TableContainer style={{ padding: "1em", width: "60vw"}}>

            <Table  >
                <TableHead >
                    <TableRow>
                        <TableCell size="small" >
                            Column name
                        </TableCell>
                        <TableCell size="small" >
                            Datatype
                        </TableCell>
                        <TableCell size="small" >
                            Primary key
                        </TableCell>
                        <TableCell size="small" >
                            Nullable
                        </TableCell>
                        <TableCell size="small" >
                            Default value
                        </TableCell>
                        <TableCell size="small" >
                            Foreign key
                        </TableCell>
                        <TableCell size="small" align="right">
                            <Button onClick={() => undefined}>
                                <AddIcon />
                            </Button>
                        </TableCell>

                    </TableRow>
                </TableHead>
                <TableBody>
                    {columns.map(c => (
                        <TableRow key={c.name}>
                            <TableCell size="small" >
                                {c.name}
                            </TableCell>
                            <TableCell size="small" >
                                {c.datatype}
                            </TableCell>
                            <TableCell size="small" >
                                {c.pk ? "Yes" : "No"}
                            </TableCell>
                            <TableCell size="small" >
                                {c.nullable ? "Yes" : "No"}
                            </TableCell>
                            <TableCell size="small" >
                                {c.defaultValue}
                            </TableCell>
                            <TableCell size="small" >
                                {c.fk == null ? "None" : c.fk}
                            </TableCell>
                            <TableCell size="small">
                                <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => undefined}>
                                    <EditIcon />
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer >
    )
}

export default CreateDatabaseForm
