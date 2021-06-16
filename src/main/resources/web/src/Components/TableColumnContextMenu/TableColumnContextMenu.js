import { Typography } from "@material-ui/core";
import { ContextMenu, MenuItem } from "react-contextmenu"

const TableColumnContextMenu = props => {
    const { placeholders, id, appender, onClick } = props
    return (
        <ContextMenu id={id}>
            <Typography style={{ margin: "0.5em 1em" }}><strong>Placeholders of table {placeholders.table}</strong></Typography>
            <MenuItem divider />
            {placeholders.values.map(f => (<MenuItem onClick={(e) => onClick(f)}>{f}</MenuItem>))}
        </ContextMenu>
    )
}

export default TableColumnContextMenu