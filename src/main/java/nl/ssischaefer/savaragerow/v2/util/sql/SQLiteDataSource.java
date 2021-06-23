package nl.ssischaefer.savaragerow.v2.util.sql;

import nl.ssischaefer.savaragerow.v2.util.Workspace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataSource {
    public static Connection get() throws SQLException {
        return DriverManager.getConnection(Workspace.getCurrentDatabaseUrl());
    }

    private SQLiteDataSource(){}
}
