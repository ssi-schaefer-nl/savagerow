package nl.ssischaefer.savaragerow.data.common.sql;

import nl.ssischaefer.savaragerow.workspace.WorkspaceService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    public static Connection getForCurrentWorkspace() throws SQLException {
        return DriverManager.getConnection(WorkspaceService.getCurrentDatabaseUrl());
    }

    private SQLiteDataSource(){}
}
