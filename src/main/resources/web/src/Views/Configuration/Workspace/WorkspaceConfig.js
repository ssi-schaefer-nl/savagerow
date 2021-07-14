import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';
import { Button, Divider, Grid } from "@material-ui/core";
import DatabaseSelect from "../../../Components/DatabaseSelect/DatabaseSelect";
import WorkspaceOverview from "./WorkspaceOverview/WorkspaceOverview";
import WorkspaceService from "../../../Service/WorkspaceService/WorkspaceService";


const WorkspaceConfig = props => {
    const [rerender, setRerender] = useState(false)
    const [importError, setImportError] = useState("")
    const workspaceService = new WorkspaceService()
    const db = localStorage.getItem("database");
    const handleExport = () => {
        window.location.href = '/api/v1/' + db + '/workspace'
    }

    const handleImport = (e) => {
        console.log( "DS aD S")
        const file = e.target.files[0]
        console.log(file)
        var reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = function (evt) {
            workspaceService.import(evt.target.result.split(',')[1], () => window.location.reload(false), (e) => setImportError(e))
        }
        reader.onerror = function (evt) {
            setImportError("error reading file");
        }
        e.target.value = ''

    }

    return (
        <>
            <Typography variant="h6" color="primary" style={{ margin: "1em 0" }}>Database</Typography>
            <Divider style={{ margin: "2em 0" }} />
            <Grid container direction="row" spacing={5} style={{ paddingLeft: "2em" }}>
                <Grid item container direction="row" spacing={5} xs={3}>

                    <Grid item>
                        <Typography variant="subtitle2" color="primary" style={{ paddingBottom: "1em" }}>Current Database</Typography>
                        <DatabaseSelect reloadSwitch={rerender} onSelect={() => window.location.reload(false)} />
                    </Grid>
                    {db != null &&
                        <Grid item style={{ marginTop: "3em" }}>
                            <Button onClick={handleExport} variant="contained">Export</Button>

                        </Grid>
                    }
                </Grid>
                <Grid item container direction="column" spacing={2} xs={3} style={{ paddingLeft: "10em" }}>
                    <Grid item>
                        <Typography variant="subtitle2" color="primary">Manage Database</Typography>
                        <WorkspaceOverview onChange={() => setRerender(r => !r)} />

                    </Grid>
                    <Grid item>
                        <Button
                            variant="contained"
                            component="label"

                        >
                            Import
                            <input
                                type="file"
                                accept=".zip"
                                hidden
                                onChange={handleImport}
                            />
                        </Button>
                    </Grid>
                    {importError.length > 0 &&
                        <Grid item>
                            <Typography color="error">{importError}</Typography>

                        </Grid>
                    }
                </Grid>
            </Grid>

        </>
    )
}

export default WorkspaceConfig