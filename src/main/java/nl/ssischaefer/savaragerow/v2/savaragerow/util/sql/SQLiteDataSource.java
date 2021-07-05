package nl.ssischaefer.savaragerow.v2.savaragerow.util.sql;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.Workspace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    public static Connection getForCurrentWorkspace() throws SQLException {
        return DriverManager.getConnection(Workspace.getCurrentDatabaseUrl());
    }

    private SQLiteDataSource(){}
}
