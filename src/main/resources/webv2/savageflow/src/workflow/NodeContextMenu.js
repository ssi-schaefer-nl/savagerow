import { Menu, MenuItem, Typography } from "@material-ui/core";


const NodeContextMenu = ({ anchor, onClose, onNodeDelete }) => {

    const handleDelete = () => {
        onClose()
        onNodeDelete()
    }

    const handleContextMenu = (e) => {
        e.preventDefault()
        onClose()
    }

    return (
        <Menu
            keepMounted
            onContextMenu={handleContextMenu}
            open={anchor.y !== null}
            onClose={onClose}
            anchorReference="anchorPosition"
            anchorPosition={
                anchor.y !== null && anchor.x !== null
                    ? { top: anchor.y, left: anchor.x }
                    : undefined
            }
        >
            <Typography style={{ margin: "0.5em 1em 0em 1em" }}><strong>Node Menu</strong></Typography>
            <MenuItem disabled divider />
            <MenuItem onClick={handleDelete} >Delete Node</MenuItem>
        </Menu>
    )
}

export default NodeContextMenu