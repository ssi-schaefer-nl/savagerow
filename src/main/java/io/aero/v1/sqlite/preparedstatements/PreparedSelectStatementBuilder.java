package io.aero.v1.sqlite.preparedstatements;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreparedSelectStatementBuilder {
    private Connection connection;
    private String table;
    private String sql;
    private int desiredRow = -1;


    public Connection getConnection() {
        return connection;
    }

    public PreparedSelectStatementBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTable() {
        return table;
    }

    public int getDesiredRow() {
        return desiredRow;
    }

    public PreparedSelectStatementBuilder setDesiredRow(int desiredRow) {
        this.desiredRow = desiredRow;
        return this;
    }

    public PreparedSelectStatementBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Map<String, String>> execute() throws SQLException {
        List<Map<String, String>> rows = new ArrayList<>();

        PreparedStatement statement = build();

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Map<String, String> columns = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columns.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            rows.add(columns);
        }
        return rows;
    }

    private PreparedStatement build() throws SQLException {

        sql = "select * from " + table;
        if(desiredRow >= 0) {
            sql = sql + " where rowid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, desiredRow);
            return statement;
        }
        return connection.prepareStatement(sql);
    }

}
