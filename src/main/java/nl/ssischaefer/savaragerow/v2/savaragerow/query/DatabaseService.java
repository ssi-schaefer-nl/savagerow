package nl.ssischaefer.savaragerow.v2.savaragerow.query;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.exception.DatabaseException;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDataSource;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseService {
    public void createDatabase(String database) throws DatabaseException, IOException, SQLException {
        boolean exists = Workspace.listDatabases().stream().anyMatch(d -> d.equals(database));
        if(exists) throw new DatabaseException("Database already exists");

        Workspace.setCurrentWorkspace(database);
        SQLiteDataSource.getForCurrentWorkspace().close(); // Create the new database by connecting to it once
    }
}
