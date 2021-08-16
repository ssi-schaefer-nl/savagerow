# Brief Use Cases

## 1 Database
### 1.1 Create a new database
The user open the databases configuration page and chooses to create a new database. The system prompts for a name for the database. The user enters a unique name. The system validates the name and creates a new database in the workspace. 

### 1.2 Remove existing database
The user opens the database configuration page and chooses to delete a database from the list of existing databases. The system prompts for confirmation and the user confirms the action. The system deletes the database from the system.

### 1.3 Import existing database
The user opens the database configuration page and chooses to import an existing database. The system prompts file a zip file containing a directory with the database file and, optionally, workflow configuration file. System validates whether the provided file is correct (i.e. file format, structure, required files) and confirms the completion of importing the database.

### 1.4 Switch current database
The user opens the database configuration page and picks an existing database from the list. The system switches its context to the selected database.

### 1.5 Export database
The user opens the database configuration page, locates the database it wants to export and chooses to export the database. The system bundles all relevant files in a zip file and provides it to the user.

