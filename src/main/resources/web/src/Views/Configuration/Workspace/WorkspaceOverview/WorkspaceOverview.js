import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import Button from '@material-ui/core/Button';
import { Divider, Grid } from "@material-ui/core";

import { TextField } from '@material-ui/core';
import DatabaseService from "../../../../Service/ConfigureService";
import PopupForm from "../../../../Components/PopupForm/PopupForm";
import CollapsableAlert from "../../../../Components/CollapsableAlert/CollapsableAlert";
import ErrorMessage from "../../../../Service/ErrorMessages/ErrorMessages";


const WorkspaceOverview = props => {
    const [workspaces, setWorkspaces] = useState([])
    const [addWorkspace, setAddWorkspace] = useState(false)
    const [areYouSure, setAreYouSure] = useState(false)
    const [selectedDatabase, setSelectedDatabase] = useState("")
    const [addDatabaseErrorMessage, setAddDatabaseErrorMessage] = useState("")
    const [loadingError, setLoadingError] = useState(false)
    const [deleteDatabaseErrorMessage, setDeleteDatabaseErrorMessage] = useState("")
    const [newDatabaseName, setNewDatabaseName] = useState("")


    useEffect(() => {
        const databaseService = new DatabaseService();
        databaseService.listAllDatabases((res) => setWorkspaces(res.data), () => setLoadingError(true))
    }, [addWorkspace, areYouSure])

    console.log(loadingError)
    const removeSelectedDatabase = () => {
        const database = selectedDatabase

        const databaseService = new DatabaseService()
        databaseService.removeDatabase(database.toLowerCase(), () => {
            setAreYouSure(false)
            if (localStorage.getItem("database") === database) {
                localStorage.removeItem("database")
                setDeleteDatabaseErrorMessage("")
                window.location.reload(false)
            }
            props.onChange()
        }, (res) => {
            setDeleteDatabaseErrorMessage(res.response.data)
        })

    }

    const handleAddWorkspace = (workspace) => {
        const databaseService = new DatabaseService()
        databaseService.addDatabase(newDatabaseName, () => {
            setAddWorkspace(false)
            setNewDatabaseName("")
            setAddDatabaseErrorMessage("")
            props.onChange()
        }, (res) => {
            setAddDatabaseErrorMessage(res.response.data)
        })
    }

    const handleClose = () => {
        setAddDatabaseErrorMessage("")
        setDeleteDatabaseErrorMessage("")
        setNewDatabaseName("")
        setAddWorkspace(false)
        setAreYouSure(false)
    }

    if(loadingError) {
        return (<CollapsableAlert severity="error" message={ErrorMessage.Database.Loading()} />)
    }

    return (
        <>
            <TableContainer style={{ marginTop: "2em", width: "20vw", maxHeight: "30vh" }}>

                <Table stickyHeader  >
                    <TableHead >
                        <TableRow>
                            <TableCell size="small" >
                                Database name
                            </TableCell>
                            <TableCell size="small" align="right">
                                <Button onClick={(e) => setAddWorkspace(true)} >
                                    <AddIcon />
                                </Button>
                            </TableCell>

                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {workspaces.map(w => (
                            <TableRow key={w}>
                                <TableCell size="small" >
                                    {w}
                                </TableCell>
                                <TableCell size="small" align="right">
                                    <Button aria-controls="simple-menu" aria-haspopup="true" onClick={(e) => { setAreYouSure(true); setSelectedDatabase(w) }}>
                                        <RemoveIcon color="error" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer >

            <PopupForm open={addWorkspace} title="Add workspace" onSubmit={handleAddWorkspace} onClose={() => handleClose()}>
                {addDatabaseErrorMessage.length > 0 &&
                    <CollapsableAlert severity="error" message={ErrorMessage.Database.Creating(addDatabaseErrorMessage)} />
                }
                <TextField label="Workspace name" value={newDatabaseName} onChange={(e) => setNewDatabaseName(e.target.value)} />
            </PopupForm>

            <PopupForm open={areYouSure} hide title="Confirm deletion" onSubmit={() => undefined} onClose={() => handleClose()}>

                <Grid container justify="center" spacing={4}>
                    {deleteDatabaseErrorMessage.length > 0 &&
                        <CollapsableAlert severity="error" message={ErrorMessage.Database.Deleting(deleteDatabaseErrorMessage)} />
                    }
                    <Grid item xs={4}>
                        <Button fullWidth variant="contained" onClick={() => removeSelectedDatabase()} color="secondary">Delete</Button>
                    </Grid>
                    <Grid item xs={4}>
                        <Button fullWidth variant="contained" color="primary" onClick={() => handleClose()}>Cancel</Button>
                    </Grid>
                </Grid>
            </PopupForm>

        </>
    )
}

export default WorkspaceOverview