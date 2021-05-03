package io.aero.integration.sqlite;

import io.aero.exceptions.NoDatabaseConnectionException;
import io.aero.service.QueryService;
import io.aero.dto.*;
import io.aero.integration.sqlite.preparedstatements.PreparedDeleteStatementBuilder;
import io.aero.integration.sqlite.preparedstatements.PreparedInsertStatementBuilder;
import io.aero.integration.sqlite.preparedstatements.PreparedSelectStatementBuilder;
import io.aero.integration.sqlite.preparedstatements.PreparedUpdateStatementBuilder;
import io.aero.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCQueryService implements QueryService {
    @Autowired
    private JDBCMetaDataManager jdbcManager;

    @Autowired
    WorkspaceService workspaceService;


    @Override
    public RowSetDTO findAll(String table) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        List<Map<String, String>> rows = new PreparedSelectStatementBuilder()
                .setTable(table)
                .setConnection(SQLiteDataSource.getConnection())
                .execute();

        return new RowSetDTO().setRows(rows);
    }

    @Override
    public TableSchemaDTO getSchema(String table) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        List<ColumnSchemaDTO> columns = jdbcManager.getTableMetaData(table).getColumns().stream()
                .map(columnMetaData -> new ColumnSchemaDTO()
                        .setColumn(columnMetaData.getName())
                        .setDatatype(JDBCDatatypeConverter.convertTypeToStringName(columnMetaData.getDatatype()))
                        .setEditable(!columnMetaData.getAutoIncrement().equals("YES"))
                        .setNullable(columnMetaData.getNullable().equals("YES")))
                .collect(Collectors.toList());

        return new TableSchemaDTO().setTable(table).setColumns(columns);
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
                .setConnection(SQLiteDataSource.getConnection())
                .execute();
    }

    @Override
    public RowDTO addRow(String table, RowDTO newRow) throws Exception {
        if (dataIsNotWhitelisted(table, newRow.getRow())) {
            throw new IllegalArgumentException("Invalid table or column names");
        }

        int rowId = new PreparedInsertStatementBuilder()
                .setTable(table)
                .setNewRow(newRow.getRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(table))
                .setConnection(SQLiteDataSource.getConnection())
                .execute();

        return find(table, rowId);
    }


    @Override
    public void deleteRow(String tableName, RowDTO row) throws Exception {
        if (dataIsNotWhitelisted(tableName, row.getRow())) {
            throw new IllegalArgumentException("Invalid table or column names");
        }

        new PreparedDeleteStatementBuilder()
                .setTable(tableName)
                .setRow(row.getRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(tableName))
                .setConnection(SQLiteDataSource.getConnection())
                .execute();
    }

    public RowDTO find(String table, int rowId) throws SQLException, NoDatabaseConnectionException {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        return new RowDTO().setRow(new PreparedSelectStatementBuilder()
                .setTable(table)
                .setDesiredRow(rowId)
                .setConnection(SQLiteDataSource.getConnection())
                .execute().get(0));

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

    private boolean tableDoesNotHaveAllColumns(String table, List<String> expectedColumns) throws SQLException, NoDatabaseConnectionException {
        return !jdbcManager.getColumnsForTable(table)
                .stream()
                .map(ColumnMetaData::getName)
                .collect(Collectors.toList())
                .containsAll(expectedColumns);
    }

}
