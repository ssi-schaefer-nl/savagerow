import TableTriggerConfiguration from "./Triggers/TableTriggerConfiguration/TableTriggerConfiguration"

const ConfigurationDialogSwitch = element => {
    switch (element.type) {
        case "Table Trigger": return <TableTriggerConfiguration id={element.id} />
        
        default: return null
    }
}


export default ConfigurationDialogSwitch