import { Checkbox, Divider, FormControlLabel, Grid, Select, Typography } from "@material-ui/core"
import { useEffect, useState } from "react"
import { MenuItem } from "react-contextmenu"
import PopupForm from "../../../../../../Components/PopupForm/PopupForm"
import QueryService from '../../../../../../Service/QueryService/QueryService'
import ActionFormRow from "../../../../../../Components/ActionFormRow/ActionFormRow"
import ActionFormTextField from "../ActionFormTextField"
import ActionTooltips from "../../ActionTooltips"
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';


const InsertAction = props => {
    const { onSubmit, placeholders, initial, open, onClose } = props
    const [tables, setTables] = useState([])

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [row, setRow] = useState(initial == null ? [] : initial.row)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({ name: name, row: row, table: table, type: "insert", triggerWorkflows: triggerWorkflows })

    }

    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose}>
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action" />

            <Divider />
            <Tooltip title={ActionTooltips.Trigger_Other_Workflows()}>

                <FormControlLabel
                    control={
                        <Checkbox
                            checked={triggerWorkflows}
                            onChange={(e) => setTriggerWorkflows(e.target.checked)}
                            name="trigger"
                            color="primary"
                        />
                    }
                    label="Trigger other workflows with this action"
                />
            </Tooltip>
            <Grid container direction="row" alignItems="center" spacing={2}>
                <Grid item>
                    <Tooltip title={ActionTooltips.Row("insert")}>
                        <Typography>Insert a new row into table </Typography>
                    </Tooltip>
                </Grid>

                <Grid item>
                    <Select
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "30%" }}
                        onChange={(e) => setTable(e.target.value)}
                        value={table}
                        required
                    >
                        {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                    </Select>
                </Grid>
            </Grid>

            {table.length > 0 &&
                <>
                    <Grid container direction="row" alignItems="center" spacing={1}>
                        <Grid item>
                            <Typography>With the following fields</Typography>
                        </Grid>
                        <Grid item>
                            <Tooltip title={ActionTooltips.RightClick("row")}>
                                <InfoIcon fontSize="small"/>
                            </Tooltip>
                        </Grid>

                    </Grid>
                    <ActionFormRow onChange={setRow} value={row} placeholders={placeholders} table={table} />

                </>
            }
        </PopupForm>
    )
}

export default InsertAction;