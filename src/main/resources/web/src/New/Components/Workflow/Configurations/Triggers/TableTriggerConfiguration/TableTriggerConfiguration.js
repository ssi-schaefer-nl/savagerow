import React, { useEffect, useState } from "react";
import HorizontalLinearStepper from "../../../../HorizontalLinearStepper/HorizontalLinearStepper";
import { Button, Grid, makeStyles, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from "@material-ui/core";
import QueryService from "../../../../../../Service/QueryService/QueryService";

const TableTriggerConfiguration = () => {
    const [trigger, setTrigger] = useState(null)
    const [parameters, setParameters] = useState(null)
    
    const steps = [
        {
            "name": "Define Table and trigger",
            "Component": <DefineTableTrigger onChange={setTrigger} trigger={trigger} />,
            "nextButton": "Finish"
        },
    ]

    return (
        <div style={{ height: "60vh", width: "70vw" }}>
            <HorizontalLinearStepper steps={steps} />
        </div>

    );
}

const DefineTableTrigger = ({ onChange, trigger }) => {
    const [tables, setTables] = useState([])

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    return (
        <div>
            <Typography variant="h6">Specify the trigger that must initiate the workflow</Typography>
            <Typography>
                The trigger for the workflow specifies upon which kind of event the workflow has to be executed.
                These events are actions you perform on the data in your tables, such as editing, deleting or adding a row.
            </Typography>
            <div style={{ margin: "5em 2em" }} >
                Trigger the workflow when we
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "10em", margin: "0 1em" }}
                    onChange={(e) => onChange({ ...trigger, type: e.target.value })}
                    value={trigger && trigger.type}
                    required
                >

                    {["delete", "update", "insert"].map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
                a row in table <nbsp />
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ minWidth: "10em", margin: "0 1em" }}
                    onChange={(e) => onChange({ ...trigger, table: e.target.value })}
                    value={trigger && trigger.table}
                    required
                >

                    {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
            </div>
        </div>
    )
}

// const useStyles = makeStyles({
//     container: {
//         width: '50%',
//         maxHeight: "20em",
//         overflow: 'auto'
//     },
//     table: {
//     }
// })

// const DefineOutputParameters = ({ initParams, onChange }) => {
//     const classes = useStyles();

//     const [params, setParams] = useState(initParams == undefined ? [] : params)

//     const addParam = () => {
//         const p = [...params, { name: "", value: "" }]
//         setParams(p)
//         onChange(p)
//     }
//     const removeParam = (i) => {
//         const p = [...params]
//         p.splice(i, 1)
//         setParams(p)
//         onChange(p)
//     }

//     const setParamName = (i, name) => {
//         const p = [...params]
//         p[i].name = name
//         setParams(p)
//         onChange(p)
//     }

//     return (
//         <TableContainer component={Paper} className={classes.container}>
//             <Table className={classes.table} stickyHeader>
//                 <TableHead>
//                     <TableRow>
//                         <TableCell>Parameter name</TableCell>
//                         <TableCell>Parameter value</TableCell>
//                         <TableCell align='right'>
//                             <Button onClick={() => addParam()}><AddIcon /></Button>
//                         </TableCell>
//                     </TableRow>
//                 </TableHead>
//                 <TableBody>
//                     {params.map((p, i) => (
//                         <TableRow key={p.name}>
//                             <TableCell scope='row'>
//                                 <TextField value={p.name} onChange={(e) => setParamName(i, e.target.value)}/>
//                             </TableCell>
//                             <TableCell>
//                                 <TextField />
//                             </TableCell>
//                             <TableCell>
//                                 <Button onClick={() => removeParam(i)}><RemoveIcon /></Button>
//                             </TableCell>
//                         </TableRow>
//                     ))}
//                 </TableBody>
//             </Table>
//         </TableContainer>
//     )
// }

export default TableTriggerConfiguration;
