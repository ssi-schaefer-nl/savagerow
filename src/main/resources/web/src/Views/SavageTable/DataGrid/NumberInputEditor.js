import { TextField } from "@material-ui/core"

const NumberInputEditor = (props) => {
    const handleChange = (event) => {
        let val = event.target.value
    
        if (!isNaN(val)) props.onRowChange({ ...props.row, [props.column.key]: val }, false)
    }

    return (
        <TextField
            value={props.row[props.column.key]}
            type="number"
            style={{
                appearance: 'none',
                width: '100%',
                height: '100%',
                padding: '3px 8px 0px 8px',
                verticalAlign: 'top',
            }}
            onChange={handleChange}
            InputProps={{
                disableUnderline: true,
                style: {
                    fontSize: 14,
                },
            }}

        />
    )
}

export default NumberInputEditor