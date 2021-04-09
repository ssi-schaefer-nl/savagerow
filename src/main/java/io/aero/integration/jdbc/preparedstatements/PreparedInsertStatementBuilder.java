package io.aero.integration.jdbc.preparedstatements;

import io.aero.integration.jdbc.ColumnMetaData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreparedInsertStatementBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private Map<String, String> newRow;
    private List<ColumnMetaData> columnMetaData;

    public Connection getConnection() {
        return connection;
    }

    public PreparedInsertStatementBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTable() {
        return table;
    }

    public PreparedInsertStatementBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public Map<String, String> getNewRow() {
        return newRow;
    }

    public PreparedInsertStatementBuilder setNewRow(Map<String, String> newRow) {
        this.newRow = newRow;
        return this;
    }

    public List<ColumnMetaData> getColumnMetaData() {
        return columnMetaData;
    }

    public PreparedInsertStatementBuilder setColumnMetaData(List<ColumnMetaData> columnMetaData) {
        this.columnMetaData = columnMetaData;
        return this;
    }


    public void execute() throws SQLException {
        build();
        statement.executeUpdate();
    }

    private void build() throws SQLException {
        List<String> columnsNewRowSorted = newRow.keySet().stream().sorted().collect(Collectors.toList());
        String sql = createSqlString(columnsNewRowSorted);
        statement = connection.prepareStatement(sql);

        ParameterSetter parameterSetter = new ParameterSetter(statement);
        parameterSetter.setParameters(columnsNewRowSorted, newRow, columnMetaData);
    }

    private String createSqlString(List<String> sortedColumns) {
        String columns = String.join(", ", sortedColumns);
        String values = sortedColumns.stream().map(col -> "?").collect(Collectors.joining(", "));

        return "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
    }
}
