// import React, { useState } from 'react';

// import { Grid, TextField } from '@material-ui/core';
// import { InputLabel, Select } from '@material-ui/core';
// import { ContextMenu, ContextMenuTrigger, MenuItem } from 'react-contextmenu';
// import ActionOnRowForm from './ActionOnRowForm';




// const CustomTextField = props => {
//     const placeholders = props.placeholders
//     const [appender, setAppender] = useState(() => () => undefined)

//     const handleChange = (e) => {
//         props.onChange(e.target.value)
//     }

//     const Input = () => {
//         return (
//             <TextField
//                 InputLabelProps={{ shrink: true }}
//                 style={{ width: "70%", marginLeft: "0" }}
//                 autoComplete='off'
//                 onChange={handleChange}
//                 required={props.required}
//                 value={props.value}
//                 label={props.label}
//                 id={props.id}
//             />
//         )
//     }

//     if (placeholders == undefined) return <Input />

//     return (
//         <>
//             <ContextMenuTrigger id={`${props.id}-menu`} collect={() => setAppender(() => (x) => handleChange(props.value != undefined ? props.value + x : props.value))}>
//                 <Input />
//             </ContextMenuTrigger>

//             <ContextMenu id={`${props.id}-menu`}>
//                 <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
//                 <MenuItem divider />
//                 {placeholders.map(f => (<MenuItem onClick={(e) => appender(`{${f}}`)}>{f}</MenuItem>))}
//             </ContextMenu>
//         </>
//     )
// }

// const CustomTextArea = props => {
//     const placeholders = props.placeholders

//     const [appender, setAppender] = useState(() => () => undefined)

//     const handleChange = (e) => {
//         props.onChange(e.target.value)
//     }

//     const Input = () => {
//         return (
//             <TextField
//                 InputLabelProps={{ shrink: true }}
//                 style={{ width: "70%" }}
//                 autoComplete='off'
//                 maxWidth={50}
//                 multiline
//                 rows={5}
//                 onChange={handleChange}
//                 required={props.required}
//                 value={props.value}
//                 label={props.label}
//                 id={props.id}
//             />
//         )
//     }
//     if (placeholders == undefined) return <Input />

//     return (
//         <>
//             <ContextMenuTrigger id={`${props.id}-menu`} collect={() => setAppender(() => (x) => handleChange(props.value != undefined ? props.value + x : props.value))}>
//                 <Input />
//             </ContextMenuTrigger>

//             <ContextMenu id={`${props.id}-menu`}>
//                 <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
//                 <MenuItem divider />
//                 {placeholders.map(f => (<MenuItem onClick={(e) => appender(`{${f}}`)}>{f}</MenuItem>))}
//             </ContextMenu>
//         </>
//     )
// }

// const CustomSelectField = props => {
//     return (
//         <Grid item style={{ margin: "1em 0" }}>
//             <InputLabel shrink required={props.required} id={props.id}>{props.label}</InputLabel>
//             <Select
//                 InputLabelProps={{ shrink: true }}
//                 style={{ width: "70%" }}
//                 {...props}
//             >
//                 {props.items.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
//             </Select>
//         </Grid>

//     )
// }


// const actionFormModels = (type) => {
//     switch (type) {
//         case "email":
//             return {
//                 email: {
//                     field: <CustomTextField />,
//                     id: "email",
//                     label: "E-mail",
//                     required: true,
//                 },
//                 subject: {
//                     field: <CustomTextField />,
//                     id: "subject",
//                     label: "Subject",
//                     required: true,
//                 },
//                 body: {
//                     field: <CustomTextArea />,
//                     id: "body",
//                     label: "Body",
//                     required: true,
//                 },
//             }
//         case "insert":
//             return {
//                 row: {
//                     field: <ActionOnRowForm />,
//                     id: "row",
//                     label: "Row",
//                     required: true,
//                 }
//             }
//         default: return {}
//     }
// }

// export default actionFormModels