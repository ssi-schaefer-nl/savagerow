package io.aero.integration.jdbc;

import io.aero.QueryService;
import io.aero.dto.*;
import io.aero.integration.jdbc.preparedstatements.PreparedInsertStatementBuilder;
import io.aero.integration.jdbc.preparedstatements.PreparedSelectStatementBuilder;
import io.aero.integration.jdbc.preparedstatements.PreparedUpdateStatementBuilder;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCQueryService implements QueryService {
    private final JDBCManager jdbcManager;

    public JDBCQueryService() throws SQLException {
        this.jdbcManager = new JDBCManager();
    }

    @Override
    public TableDataDTO findAll(String table) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        List<ColumnSchemaDTO> columns = jdbcManager.getTableMetaData(table).getColumns().stream()
                .map(columnMetaData -> new ColumnSchemaDTO()
                        .setColumn(columnMetaData.getName())
                        .setDatatype(JDBCDatatypeConverter.convertTypeToStringName(columnMetaData.getDatatype()))
                        .setEditable(!columnMetaData.getAutoIncrement().equals("YES")))
                .collect(Collectors.toList());

        return new TableDataDTO()
                .setData(new PreparedSelectStatementBuilder().setTable(table).setConnection(jdbcManager.getConnection()).execute())
                .setTableSchema(new TableSchemaDTO().setTable(table).setColumns(columns));
    }

    @Override
    public List<String> listTables() throws Exception {
        return jdbcManager.getTables();
    }

    @Override
    public void updateRow(String table, RowUpdateDTO update) throws Exception {
        if (dataIsNotWhitelisted(table, update)) {
            throw new IllegalArgumentException("Invalid table or column names");
        }
        new PreparedUpdateStatementBuilder()
                .setTable(table)
                .setOriginalRow(update.getOldRow())
                .setNewRow(update.getNewRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(table))
                .setConnection(jdbcManager.getConnection())
                .execute();
    }

    @Override
    public void addRow(String table, RowInsertDTO newRow) throws Exception {
        if (dataIsNotWhitelisted(table, newRow.getNewRow())) {
            throw new IllegalArgumentException("Invalid table or column names");
        }

        new PreparedInsertStatementBuilder()
                .setTable(table)
                .setNewRow(newRow.getNewRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(table))
                .setConnection(jdbcManager.getConnection())
                .execute();
    }

    @Override
    public void switchDatabase(String database) {
        try {
            jdbcManager.connectToDb(database);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean dataIsNotWhitelisted(String table, RowUpdateDTO update) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) return true;
        if (tableDoesNotHaveAllColumns(table, new ArrayList<>(update.getOldRow().keySet()))) return true;
        if (tableDoesNotHaveAllColumns(table, new ArrayList<>(update.getNewRow().keySet()))) return true;
        return false;
    }

    private boolean dataIsNotWhitelisted(String table, Map<String, String> newRow) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) return true;
        if (tableDoesNotHaveAllColumns(table, new ArrayList<>(newRow.keySet()))) return true;
        return false;
    }

    private boolean tableDoesNotHaveAllColumns(String table, List<String> expectedColumns) throws SQLException {
        return !jdbcManager.getColumnsForTable(table)
                .stream()
                .map(ColumnMetaData::getName)
                .collect(Collectors.toList())
                .containsAll(expectedColumns);
    }

}
