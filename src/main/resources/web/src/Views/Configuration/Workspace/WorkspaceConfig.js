import React, { useEffect, useState } from "react";
import Typography from '@material-ui/core/Typography';
import { Button, Divider, Grid } from "@material-ui/core";
import DatabaseSelect from "../../../Components/DatabaseSelect/DatabaseSelect";
import WorkspaceOverview from "./WorkspaceOverview/WorkspaceOverview";
import WorkspaceService from "../../../Service/WorkspaceService/WorkspaceService";
import { makeStyles } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { grey } from "@material-ui/core/colors";


const WorkspaceConfig = props => {
    const [rerender, setRerender] = useState(false)
    const [importError, setImportError] = useState("")
    const workspaceService = new WorkspaceService()
    const db = localStorage.getItem("database");
    const [expanded, setExpanded] = React.useState("current-database");

    const handleChange = (panel) => (event, isExpanded) => (setExpanded(isExpanded ? panel : false));



    const handleExport = () => {
        window.location.href = '/api/v1/workspace/' + db
    }

    const handleImport = (e) => {
        console.log("DS aD S")
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
            <AccordionSection onChange={handleChange("current-database")} expanded={expanded === "current-database"} title="Current Database">
                <Grid item container direction="row" spacing={5}  xs={3}>
                    <Grid item>
                        <DatabaseSelect reloadSwitch={rerender} onSelect={() => window.location.reload(false)} />
                    </Grid>
                    {db != null &&
                        <Grid item style={{ marginTop: "1em" }}>
                            <Button onClick={handleExport} variant="contained">Export</Button>

                        </Grid>
                    }
                </Grid>
            </AccordionSection>
            <AccordionSection onChange={handleChange("manage-databases")} expanded={expanded === "manage-databases"} title="Manage Databases">
                <Grid item container direction="column" spacing={2} xs={3}>
                    <Grid item>
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
            </AccordionSection>

        </>
    )
}



const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
}));

const AccordionSection = (props) => {
    const classes = useStyles();

    return (
        <Accordion expanded={props.expanded} onChange={props.onChange} style={{margin: "0.5em"}}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
                style={{padding: "1em"}}
                ref={(node) => {
                    if (node) {
                      node.style.setProperty('background-color', `${grey[50]}`,'important');
                    }
                  }}
            >
                <Typography className={classes.heading}>{props.title}</Typography>
            </AccordionSummary>
            <AccordionDetails style={{ flexDirection: "column", padding: "1em" }}>
                {props.children}
            </AccordionDetails>
        </Accordion>
    )
}

export default WorkspaceConfig