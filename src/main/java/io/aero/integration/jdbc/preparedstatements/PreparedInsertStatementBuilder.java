package io.aero.integration.jdbc.preparedstatements;

import io.aero.integration.jdbc.ColumnMetaData;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreparedInsertStatementBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private Map<String, String> newRow;
    private String sql;
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
        String columns = String.join(", ", columnsNewRowSorted);
        String values = columnsNewRowSorted.stream().map(col -> "?").collect(Collectors.joining(", "));

        sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
        statement = connection.prepareStatement(sql);

        int paramIndex = 1;
        setParametersBulk(columnsNewRowSorted, newRow, paramIndex);
    }

    private int setParametersBulk(List<String> sortedColumns, Map<String, String> colValMap, int paramIndex) throws SQLException {
        for (String col : sortedColumns) {
            String type = columnMetaData.stream().filter(c -> c.getName().equals(col)).findFirst().map(ColumnMetaData::getDatatype).orElse("");
            setParameterWithAppropriateType(paramIndex, colValMap.get(col), type);
            paramIndex++;
        }
        return paramIndex;
    }

    private void setParameterWithAppropriateType(int paramIndex, String colVal, String type) throws SQLException {
        if(colVal == null || colVal.isEmpty()) {
            statement.setNull(paramIndex, Integer.parseInt(type));
            return;
        }

        switch (type) {
            case "12":
            case "-1":
                statement.setString(paramIndex, colVal);
                break;
            case "2":
                statement.setBigDecimal(paramIndex, new BigDecimal(colVal));
                break;
            case "-7":
                statement.setBoolean(paramIndex, Boolean.parseBoolean(colVal));
                break;
            case "-6":
                statement.setByte(paramIndex, Byte.parseByte(colVal));
                break;
            case "5":
                statement.setShort(paramIndex, Short.parseShort(colVal));
                break;
            case "4":
                statement.setInt(paramIndex, Integer.parseInt(colVal));
                break;
            case "-5":
                statement.setLong(paramIndex, Long.parseLong(colVal));
                break;
            case "7":
                statement.setFloat(paramIndex, Float.parseFloat(colVal));

                break;
            case "8":
                statement.setDouble(paramIndex, Double.parseDouble(colVal));
                break;
            case "-3":
            case "-4":
                statement.setBytes(paramIndex, colVal.getBytes());
                break;
            case "91":
                statement.setDate(paramIndex, Date.valueOf(colVal));
                break;
            case "92":
                statement.setTime(paramIndex, Time.valueOf(colVal));
                break;
            case "93":
                statement.setTimestamp(paramIndex, Timestamp.valueOf(colVal));
                break;
            default:
                statement.setString(paramIndex, colVal);
                break;

        }


    }

}
