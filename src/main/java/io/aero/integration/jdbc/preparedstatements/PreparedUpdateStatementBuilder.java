package io.aero.integration.jdbc.preparedstatements;

import io.aero.integration.jdbc.ColumnMetaData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreparedUpdateStatementBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private Map<String, String> originalRow;
    private Map<String, String> newRow;
    private List<ColumnMetaData> columnMetaData;


    public String getTable() {
        return table;
    }

    public PreparedUpdateStatementBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public Map<String, String> getOriginalRow() {
        return originalRow;
    }

    public PreparedUpdateStatementBuilder setOriginalRow(Map<String, String> originalRow) {
        this.originalRow = originalRow;
        return this;
    }

    public Map<String, String> getNewRow() {
        return newRow;
    }

    public PreparedUpdateStatementBuilder setNewRow(Map<String, String> newRow) {
        this.newRow = newRow;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedUpdateStatementBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public List<ColumnMetaData> getColumnMetaData() {
        return columnMetaData;
    }

    public PreparedUpdateStatementBuilder setColumnMetaData(List<ColumnMetaData> columnMetaData) {
        this.columnMetaData = columnMetaData;
        return this;
    }

    public void execute() throws SQLException {
        build();
        statement.executeUpdate();
    }

    private void build() throws SQLException {
        List<String> columnsNewRowSorted = newRow.keySet().stream().sorted().collect(Collectors.toList());
        List<String> columnsOldRowSorted = originalRow.keySet().stream().sorted().filter(row -> originalRow.get(row)!=null).collect(Collectors.toList());

        String sql = createSqlString(columnsNewRowSorted, columnsOldRowSorted);
        statement = connection.prepareStatement(sql);

        ParameterSetter parameterSetter = new ParameterSetter(statement);
        parameterSetter.setParameters(columnsNewRowSorted, newRow, columnMetaData);
        parameterSetter.setParameters(columnsOldRowSorted, originalRow, columnMetaData);

        statement.executeUpdate();
    }

    private String createSqlString(List<String> sortedSetColumns, List<String> sortedWhereColumns) {
        String set = sortedSetColumns.stream().map(col -> col + " = ?").collect(Collectors.joining(", "));
        String where = sortedWhereColumns.stream().map(col -> col + " = ?").collect(Collectors.joining(" AND "));
        return "UPDATE " + table + " SET " + set + " WHERE " + where;
    }


}
