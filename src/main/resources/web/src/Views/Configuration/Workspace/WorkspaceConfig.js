import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';
import { Divider, Grid } from "@material-ui/core";
import DatabaseSelect from "../../../Components/DatabaseSelect/DatabaseSelect";
import WorkspaceOverview from "./WorkspaceOverview/WorkspaceOverview";


const WorkspaceConfig = props => {
    const [rerender, setRerender] = useState(false)


    return (
        <>
            <Typography variant="h6" color="primary" style={{ margin: "1em 0" }}>Database</Typography>
            <Divider style={{ margin: "2em 0" }} />
            <Grid container direction="row" spacing={10} style={{ paddingLeft: "2em" }}>
                <Grid item>
                    <Typography variant="subtitle2" color="primary" style={{ paddingBottom: "1em" }}>Current Database</Typography>
                    <DatabaseSelect reloadSwitch={rerender} onSelect={() => window.location.reload(false)} />
                </Grid>
                <Grid item style={{ paddingLeft: "10em" }}>
                    <Typography variant="subtitle2" color="primary">Manage Database</Typography>
                    <WorkspaceOverview onChange={() => setRerender(r => !r)} />
                </Grid>
            </Grid>
        </>
    )
}

export default WorkspaceConfig