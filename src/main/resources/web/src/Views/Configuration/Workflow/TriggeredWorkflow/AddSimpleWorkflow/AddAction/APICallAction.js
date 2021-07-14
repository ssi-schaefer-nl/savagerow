import { Divider, InputLabel, Typography } from "@material-ui/core"


import { useState } from "react"
import AceEditor from "react-ace"
import "ace-builds/src-noconflict/mode-java"
import "ace-builds/src-noconflict/theme-github"
import 'ace-builds/src-noconflict/mode-json';
import { ContextMenuTrigger } from "react-contextmenu"
import PopupForm from "../../../../../../Components/PopupForm/PopupForm"
import TableColumnContextMenu from "../../../../../../Components/TableColumnContextMenu/TableColumnContextMenu"
import ActionFormTextField from "./ActionFormTextField"



const APICallAction = props => {
    const { onSubmit, placeholders, onClose, open, initial } = props
    const [contextMenuId, setContextMenuId] = useState(Math.floor(Math.random() * 100))

    const [appender, setAppender] = useState(() => () => undefined)
    const [name, setName] = useState(initial == null ? "" : initial.name)
    const [url, setUrl] = useState(initial == null ? "" : initial.url)
    const [body, setBody] = useState(initial == null ? "" : initial.body)
    const [jsonIncorrect, setJsonIncorrect] = useState(false)


    const handleSubmit = e => {
        e.preventDefault()
        try {
            JSON.parse(body)
            onSubmit({ name: name, url: url, jsonBody: body, type: "api" })
            setName("")
            setUrl("")
            setBody("")
        } catch (error) {
            setJsonIncorrect(true)
            console.log(error)
        }
    }

    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose} title="New API Call Action">
            <ActionFormTextField id="name" onChange={setName} value={name} label="Action Name" required />

            <Divider />
            <Typography>Note that the method is POST</Typography>
            {jsonIncorrect && <Typography color="error">The body is incorrect JSON format</Typography>}

            <ContextMenuTrigger holdToDisplay={-1} id={`contextmenu-${contextMenuId}`} collect={() => setAppender(() => (x) => setUrl(s => s == undefined ? x : s + x))}>
                <ActionFormTextField id="url" onChange={setUrl} value={url} label="API Call URL" required />
            </ContextMenuTrigger>

            {/* <ContextMenuTrigger holdToDisplay={-1} id={`contextmenu-${contextMenuId}`} collect={() => setAppender(() => (x) => setBody(b => b == undefined ? x : b + x))}>
                <ActionFormTextArea id="body" onChange={(e) => { setJsonIncorrect(false); setBody(e); }} value={body} label="Body" required />
            </ContextMenuTrigger>

         */}
            <ContextMenuTrigger holdToDisplay={-1} id={`contextmenu-${contextMenuId}`} collect={() => setAppender(() => (x) => setBody(b => b == undefined ? x : b + x))}>
            <InputLabel id="name">JSON Body</InputLabel>

                <AceEditor
                    mode="json"
                    name="body"
                    theme="github"
                    height="300px"
                    onChange={setBody}
                    value={body}
                    showGutter={false}
                    name="UNIQUE_ID_OF_DIV"
                    style={{border: "1px solid grey", borderRadius: "5px"}}
                    editorProps={{ $blockScrolling: true }}
                />
            </ContextMenuTrigger>
            <TableColumnContextMenu id={`contextmenu-${contextMenuId}`} onClick={(f) => appender(`$${f}$`)} placeholders={placeholders} />
        </PopupForm >
    )
}

export default APICallAction;