import { Button, TextField, Typography } from '@material-ui/core';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import * as React from 'react';

const AddRowDialog = ({ open, handleClose, schema, onAdd }) => {
    const [row, setRow] = React.useState({})

    const setValue = (column, value) => {
        const r = { ...row }
        r[column.name] = value
        setRow(r)
    }

    const handleAdd = () => {
        onAdd(row)
        setRow({})
    }
    
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            maxWidth="lg"
        >
            <DialogTitle>
                <Typography variant="h6">Add a new row</Typography>
            </DialogTitle>
            <DialogContent style={{ height: '20vh' }}>
                <TableContainer>
                    <Table>
                        <TableHead>
                            <TableRow>
                                {schema.map(c => (
                                    <TableCell>{c.name}</TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow key="row">
                                {schema.map(c => (
                                    <TableCell >
                                        <TextField type={c.datatype == "Integer" ? "number" : "text"} value={row[c.name]} onChange={(e) => setValue(c, e.target.value)} />
                                    </TableCell>
                                ))}
                            </TableRow>

                        </TableBody>
                    </Table>
                </TableContainer>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">Cancel</Button>
                <Button onClick={handleAdd} color="primary" autoFocus>Add</Button>
            </DialogActions>
        </Dialog >
    )
}

export default AddRowDialog