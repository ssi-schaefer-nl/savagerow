import { Divider } from "@material-ui/core"
import { useState } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import ActionFormTextArea from "./ActionFormTextArea"
import ActionFormTextField from "./ActionFormTextField"
import NewActionForm from "./NewActionForm"

const EmailAction = props => {
    const { onSubmit, placeholders, onClose, open, initial } = props
    const [appender, setAppender] = useState(() => () => undefined)
    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [email, setEmail] = useState(initial == null ? "" : initial.email)
    const [subject, setSubject] = useState(initial == null ? "" : initial.subject)
    const [body, setBody] = useState(initial == null ? "" : initial.body)

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({name: name, email: email, subject: subject, body: body, type: "email"})
    }

    return (
            <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose}>
                <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required />

                <Divider />

                <ActionFormTextField id="email" onChange={setEmail} value={email} label="E-mail" required />

                <ContextMenuTrigger holdToDisplay={-1} id="placeholders-menu" collect={() => setAppender(() => (x) => setSubject(s => s == undefined ? x : s + x))}>
                    <ActionFormTextField id="subject" onChange={setSubject} value={subject} label="Subject" required />
                </ContextMenuTrigger>

                <ContextMenuTrigger holdToDisplay={-1} id="placeholders-menu" collect={() => setAppender(() => (x) => setBody(b => b == undefined ? x : b + x))}>
                    <ActionFormTextArea id="body" onChange={setBody} value={body} label="Body" required />
                </ContextMenuTrigger>

                <ContextMenu id="placeholders-menu">
                    <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
                    <MenuItem divider />
                    {placeholders.map(f => (
                        <MenuItem onClick={(e) => appender(`{${f}} `)}>{f}</MenuItem>
                    ))}
                </ContextMenu>

            </PopupForm>
    )
}

export default EmailAction;