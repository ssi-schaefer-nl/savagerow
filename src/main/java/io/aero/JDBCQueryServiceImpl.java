package io.aero;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JDBCQueryServiceImpl implements QueryService {
    private final Connection connection;

    public JDBCQueryServiceImpl() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:chinook.db");
    }

    @Override
    public List<Map<String, String>> findAll(String table) throws Exception {
        List<Map<String, String>> rows = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + table);
            while (rs.next()) {
                Map<String, String> columns = new HashMap<>();
                // read the result set
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    columns.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                rows.add(columns);
            }
        } catch (Exception E) {
            throw new Exception("Unable to fetch table data");
        }
        return rows;
    }

    @Override
    public List<String> listTables() throws Exception {
        List<String> columns = new ArrayList<>();
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                columns.add(rs.getString(3));
            }
        } catch (Exception e) {
            throw new Exception("Unable to list tables");
        }

        return columns;
    }

    @Override
    public void updateRow(String table, RowUpdateDTO update) throws Exception {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String updates = update.getNewRow().entrySet().stream().map(v -> v.getKey() + " = " + (v.getValue().contains(" ") ? "'" + v.getValue() + "'" : v.getValue())).collect(Collectors.joining(", "));
            String criteria = update.getOldRow().entrySet().stream().map(v -> v.getKey() + " = " + (v.getValue().contains(" ") ? "'" + v.getValue() + "'" : v.getValue())).collect(Collectors.joining(" AND "));

            String query = "UPDATE  " + table +
                    " SET " + updates +
                    " WHERE " + criteria;
            statement.executeUpdate(query);

        } catch (SQLException throwables) {
            throw new Exception("Unable to update table: " + throwables.getMessage());
        }
    }
}
