import DefineColumnDialog from "../../DefineColumnDialog/DefineColumnDialog";
import React, { useState } from 'react';
import TableService from "../../../Service/TableService";

export default function AddColumn(props) {
    const { onSuccess, table, handleClose, open } = props
    const tableService = new TableService(table)
    const [error, setError] = useState(undefined)

    const onClose = () => {
        setError(undefined)
        handleClose()
    }

    const submit = (data) => {
        tableService.addColumn(data,
            () => {
                onSuccess()
                onClose()
            },
            (e) => {console.log(e); setError(e)})
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