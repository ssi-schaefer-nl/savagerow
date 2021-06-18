import DefineColumnDialog from "../../DefineColumnDialog/DefineColumnDialog";
import React, { useState } from 'react';
import DefinitionService from "../../../Service/DefinitionService/DefinitionService";

export default function AddColumn(props) {
    const { onSuccess, table, handleClose, open } = props
    const definitionService = new DefinitionService(table)
    const [error, setError] = useState(undefined)

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
            (e) => { console.log(e.data); setError(e.data) })
    }

    return (
        <DefineColumnDialog
            open={open}
            onSubmit={submit}
            errorMessage={error}
            handleClose={onClose}
        />
    )
}