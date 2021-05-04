import React from "react";
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import ListItemText from '@material-ui/core/ListItemText';
import Select from '@material-ui/core/Select';
import Checkbox from '@material-ui/core/Checkbox';
import { Button, Container, Grid, IconButton } from "@material-ui/core";
import DeleteIcon from '@material-ui/icons/Delete';
import HighlightOffIcon from '@material-ui/icons/HighlightOff';
import { grey } from "@material-ui/core/colors";
const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(0.5),
        width: "100%",
        // maxWidth: 300,
    },
    chips: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    chip: {
        margin: 2,
    },
    noLabel: {
        marginTop: theme.spacing(3),
    },

    button: {
        "&:hover": {
            backgroundColor: "transparent"
        }
    }

}));


const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    anchorOrigin: {
        vertical: "bottom",
        horizontal: "left"
    },
    transformOrigin: {
        vertical: "top",
        horizontal: "left"
    },
    getContentAnchorEl: null,
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 10 + ITEM_PADDING_TOP,
            width: 200,
        },

    }
};


const MultiSelect = (props) => {
    const classes = useStyles();
    const { placeholder, onChange, options } = props;
    const [selection, setSelection] = React.useState([]);

    const onChangeSelection = (event) => {
        setSelection(event.target.value)
        onChange(event.target.value)
    }

    const clear = () => {
        setSelection([])
        onChange([])
    }

    return (
        <Grid container direction="row" alignItems="center" justify="center">

            <Grid item xs={9}>
                <FormControl className={classes.formControl}>
                    <InputLabel id="demo-mutiple-checkbox-label">{placeholder}</InputLabel>
                    <Select
                        labelId="demo-mutiple-checkbox-label"
                        id="demo-mutiple-checkbox"
                        multiple
                        value={selection}
                        onChange={onChangeSelection}
                        input={<Input />}
                        renderValue={(selected) => selected.join(', ')}
                        MenuProps={MenuProps}
                    >
                        <MenuItem disabled value="">
                            <em>{placeholder}</em>
                        </MenuItem>
                        {options.map((o) => (
                            <MenuItem key={o} value={o}>
                                <Checkbox checked={selection.indexOf(o) > -1} />
                                <ListItemText primary={o} />
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Grid>
            <Grid item>
                <Button
                    size="small"
                    aria-label="delete"
                    onClick={clear}
                    style={{ marginLeft: "0.5em", marginTop: "1.4em" }}
                    disableFocusRipple={true}
                    disableRipple
                >
                    <HighlightOffIcon />
                </Button>
            </Grid>

        </Grid>)
}

export default MultiSelect