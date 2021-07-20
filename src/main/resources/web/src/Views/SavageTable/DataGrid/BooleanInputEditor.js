import { Box, Grid, TextField } from "@material-ui/core"
import Checkbox from '@material-ui/core/Checkbox';

const BooleanInputEditor = (props) => {
    const handleChange = (event) => {
        let val = event.target.checked ? 1 : 0
        props.onRowChange({ ...props.row, [props.column.key]: val }, false)
    }
    var checked = props.row[props.column.key] > 0

    return (
        <Grid container alignItems='center' justify='center'>
            <Checkbox
                style={{padding: '4px 6px 0 6px'}}
                disableRipple
                checked={checked}
                onChange={handleChange}
            />
        </Grid>
    )
}

export default BooleanInputEditor