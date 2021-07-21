import DateFnsUtils from '@date-io/date-fns';
import {
    MuiPickersUtilsProvider,
    KeyboardDateTimePicker,
} from '@material-ui/pickers';

const DateTimePicker = (props) => {
    
    const handleChange = (v) => {
        props.onRowChange({ ...props.row, [props.column.key]: v }, false)
    }


    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDateTimePicker
                style={{
                    appearance: 'none',
                    width: '100%',
                    height: '100%',
                    padding: '1px 6px 0 6px',
                    verticalAlign: 'top',
                }}
                
                showTodayButton={true}
                clearable
                format="MM/dd/yyyy HH:mm"
                value={props.row[props.column.key]}
                InputProps={{
                    readOnly: true,
                    disableUnderline: true,
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

export default DateTimePicker