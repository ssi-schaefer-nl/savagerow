import { Checkbox, Divider, FormControlLabel, Grid, InputLabel, Select, Typography } from "@material-ui/core"
import { useEffect, useState } from "react"
import { MenuItem } from "react-contextmenu"
import PopupForm from "../../../../../../../Components/PopupForm/PopupForm"
import QueryService from '../../../../../../../Service/QueryService/QueryService'
import ActionFormRow from "../../../../../../../Components/ActionFormRow/ActionFormRow"
import ActionFormTextField from "../ActionFormTextField"
import ActionTooltips from "../../ActionTooltips"
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';
import VerticalLinearStepper from "../../../../../../../Components/VerticalLinearStepper/VerticalLinearStepper"
import PopupWindow from "../../../../../../../Components/PopupWindow/PopupWindow"




const InsertAction = props => {
    const { onSubmit, placeholders, initial, open, onClose } = props
    const [tables, setTables] = useState([])

    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [row, setRow] = useState(initial == null ? [] : initial.row)
    const [triggerWorkflows, setTriggerWorkflows] = useState(initial == null ? false : initial.triggerWorkflows)


    const handleSubmit = () => {
        onSubmit({ name: name, row: row, table: table, type: "insert", triggerWorkflows: triggerWorkflows })

    }
    const steps = [
        {
            "name": "Enter a name",
            "Component": <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required title="Create a new insert action" />,
            "nextAllowed": name != null && name.length > 0
        },
        {
            "name": "Optional: Trigger other workflows",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>{ActionTooltips.Trigger_Other_Workflows()}</Typography>
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
                </>,
        },
        {
            "name": "Specify the target table",
            "Component":
                <>
                    <InputLabel id="select-table">Table</InputLabel>
                    <Select
                        id="select-table"
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "30%" }}
                        onChange={(e) => setTable(e.target.value)}
                        value={table}
                        placeholder="Select the table"
                        required
                    >
                        {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                    </Select>
                </>,
            "nextAllowed": table.length > 0,
        },
        {
            "name": "Define the new row",
            "Component":
                <ActionFormRow onChange={setRow} value={row} placeholders={placeholders} table={table} />,
            "nextButton": "Save",
            "onNext": handleSubmit,
            "nextAllowed": Object.values(row).filter(v => v.length > 0).length > 0
        },
    ]

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])


    return (
        <PopupWindow open={open} onClose={onClose} title="Create Insert Action" wide>
            <div style={{ width: "50vw", height: "60vh" }}>
                <VerticalLinearStepper steps={steps} />
            </div>
        </PopupWindow>
    )
}

export default InsertAction;