package io.aero.v1.sqlite.utils;

import io.aero.v1.exceptions.NoDatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    private static Connection conn = null;

    public static Connection getConnection() throws NoDatabaseConnectionException {
        if(conn == null) {
            throw new NoDatabaseConnectionException("Database connection has not been setup");
        }

        return conn;
    }

    public static void connect(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    private SQLiteDataSource(){}
}
