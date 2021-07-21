import DateFnsUtils from '@date-io/date-fns';
import {
    MuiPickersUtilsProvider,
    KeyboardDatePicker,
} from '@material-ui/pickers';

const DatePicker = (props) => {
    const handleChange = (v) => {
        props.onRowChange({ ...props.row, [props.column.key]: v }, false)
    }


    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDatePicker
                style={{
                    appearance: 'none',
                    width: '100%',
                    height: '100%',
                    padding: '1px 6px 0 6px',
                    verticalAlign: 'top',
                }}
                clearable
                showTodayButton
                format="MM/dd/yyyy"
                value={props.row[props.column.key]}
                InputProps={{
                    readOnly: true,
                    disableUnderline: true,
                    shrink: true,
                    style: {
                        fontSize: 14,
                    },
                }}
                onChange={handleChange}
                KeyboardButtonProps={{
                    'aria-label': 'change date',
                }}
            />
        </MuiPickersUtilsProvider>
    )
}

export default DatePicker