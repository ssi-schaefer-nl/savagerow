const ErrorMessage = {
    Database: {
        Loading: () => "Error while loading the databases from the workspace. Check connection to the server.",
        Deleting: (cause) => "Error while deleting the database: " + cause,
        Creating: (cause) => "Error while creating the database: " + cause,
    },
    Workflow: {
        Loading: () => "Error while loading the workflows. Check connection to the server.",
    }
    
}

export default ErrorMessage