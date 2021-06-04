import { Button, DialogActions, DialogContent, Divider, Grid, List, ListItem, makeStyles, TextField, Toolbar, Tooltip, Typography } from '@material-ui/core';
import React, { useState } from 'react';
import InfoIcon from '@material-ui/icons/Info';
import { ContextMenu, ContextMenuTrigger, MenuItem } from 'react-contextmenu';

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '25ch',
        },
    },
}));


export default function AddAction(props) {
    const { onApply, columns } = props;
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [subject, setSubject] = useState("");
    const [body, setBody] = useState("");

    const [appender, setAppender] = useState(() => () => undefined)
    const availableFields = columns.map(c => c.name)
    const classes = useStyles();

    const onSubmit = (e) => {
        e.preventDefault()
        onApply({
            "name": name,
            "email": email,
            "subject": subject,
            "body": body
        })
    }

    return (
        <div>
            <form onSubmit={onSubmit} className={classes.root} autoComplete="off">
                <DialogContent >
                    <Grid container direction="column" >
                        <DefaultTextField id="name" onChange={(e) => setName(e.target.value)} value={name} label="Action Name" required />
                        <Divider style={{ margin: "2em 0" }} />
                        <DefaultTextField id="email" onChange={(e) => setEmail(e.target.value)} value={email} required label="E-mail address" />

                        <ContextMenuTrigger id="field-menu" collect={() => setAppender(() => (x) => setSubject(d => d += x))} holdToDisplay="-1">
                            <DefaultTextField id="subject" onChange={(e) => setSubject(e.target.value)} required label="Subject" value={subject} />
                        </ContextMenuTrigger>

                        <ContextMenuTrigger id="field-menu" collect={() => setAppender(() => (x) => setBody(d => d += x))}>
                            <DefaultTextArea id="body" onChange={(e) => setBody(e.target.value)} value={body} required label="Body" />
                        </ContextMenuTrigger>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button color="primary" type="submit">Apply</Button>
                </DialogActions>
            </form>

            <ContextMenu id="field-menu">
                <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
                <MenuItem divider />
                {availableFields.map(field => (<MenuItem onClick={(e) => appender(`{${field}} `)}>{field}</MenuItem>))}
            </ContextMenu>
        </div >
    );
}

//

const DefaultTextField = props => {
    return (
        <Grid item >

            <TextField
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%" }}
                autoFocus
                autoComplete='off'
                {...props}
            />
        </Grid>

    )
}

const DefaultTextArea = props => {
    return (
        <Grid item>
            <TextField
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%" }}
                autoFocus
                autoComplete='off'
                maxWidth={50}
                multiline
                rows={5}
                {...props}
            />
        </Grid>
    )
}