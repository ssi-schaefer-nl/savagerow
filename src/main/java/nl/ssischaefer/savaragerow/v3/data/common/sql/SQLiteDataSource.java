package nl.ssischaefer.savaragerow.v3.data.common.sql;

import nl.ssischaefer.savaragerow.v3.util.Workspace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    public static Connection getForCurrentWorkspace() throws SQLException {
        return DriverManager.getConnection(Workspace.getCurrentDatabaseUrl());
    }

    private SQLiteDataSource(){}
}
