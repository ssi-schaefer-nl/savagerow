import DefineColumnDialog from "../DefineColumnDialog/DefineColumnDialog";
import React, { useEffect, useState } from 'react';
import DefinitionService from "../../Service/DefinitionService/DefinitionService";
import QueryService from "../../Service/QueryService/QueryService";

export default function AddColumn(props) {
    const { onSuccess, table, handleClose, open } = props
    const definitionService = new DefinitionService(table)
    const [error, setError] = useState(undefined)
    const [emptyTable, setEmptyTable] = useState(false)
    const onClose = () => {
        setError(undefined)
        handleClose()
    }

    const submit = (data) => {
        definitionService.addColumn(data,
            () => {
                onSuccess()
                onClose()                
            },
            (e) => { setError(e.response.data) })
    }

    useEffect(() => {
        if (open) {
            new QueryService(table).getRowSet(data => setEmptyTable(data.data.length == 0), () => setEmptyTable(false))
        }
    }, [open])

    return (
        <DefineColumnDialog
            open={open}
            empty={emptyTable}
            onSubmit={submit}
            errorMessage={error}
            handleClose={onClose}
        />
    )
}