package nl.ssischaefer.savaragerow.v2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    private static Connection conn = null;

    public static Connection get() throws SQLException {
        if(conn != null) {
            conn.close();
        }

        conn = DriverManager.getConnection(Workspace.getCurrentDatabaseUrl());
        return conn;
    }

    public static void disconnect() throws SQLException {
        if(conn != null) {
            conn.close();
            conn = null;
        }
    }

    private SQLiteDataSource(){}
}
