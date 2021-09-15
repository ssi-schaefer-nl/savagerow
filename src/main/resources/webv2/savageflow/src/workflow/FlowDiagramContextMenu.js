import { Menu, MenuItem, Typography } from "@material-ui/core";


const FlowDiagramContextMenu = ({ anchor, onClose, onAddTask, onAddTrigger, allowTrigger, onAddDecision }) => {
    const handleAddTrigger = (type) => {
        onAddTrigger(type)
        onClose()
    }

    const handleAddTask = (type, subtype) => {
        onAddTask(type, subtype)
        onClose()
    }


    const handleContextMenu = (e) => {
        e.preventDefault()
        onClose()
    }

    const handleAddDecision = (e) => {
        onAddDecision()
        onClose()
    }

    return (
        <Menu
            onContextMenu={handleContextMenu}
            keepMounted
            open={anchor.y !== null}
            onClose={onClose}
            anchorReference="anchorPosition"
            anchorPosition={
                anchor.y !== null && anchor.x !== null
                    ? { top: anchor.y, left: anchor.x }
                    : undefined
            }
        >
            <Typography style={{ margin: "0.5em 1em 0em 1em" }}><strong>Workflow Element</strong></Typography>
            <MenuItem disabled divider />
            <MenuItem disabled={!allowTrigger} onClick={() => handleAddTrigger('table')}>Table Trigger</MenuItem>
            <MenuItem onClick={() => handleAddTask('crud', 'update')} >CRUD Update</MenuItem>
            <MenuItem onClick={() => handleAddTask('crud', 'delete')}>CRUD Delete</MenuItem>
            <MenuItem onClick={() => handleAddTask('crud', 'insert')}>CRUD Insert</MenuItem>
            <MenuItem onClick={() => handleAddTask('crud', 'select')}>CRUD Select</MenuItem>
            {/* <MenuItem onClick={() => handleAddDecision('input')}>Input Decision</MenuItem> */}
            
        </Menu>
    )
}

export default FlowDiagramContextMenu