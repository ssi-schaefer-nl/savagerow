package nl.ssischaefer.savaragerow.v2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if(conn == null) {
            throw new SQLException("No connection is setup");
        }

        return conn;
    }

    public static void connect(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    private SQLiteDataSource(){}
}
