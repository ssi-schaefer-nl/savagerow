package nl.ssischaefer.savagerow.integration.sqlite.service;

import nl.ssischaefer.savagerow.dto.ColumnSchemaDTO;
import nl.ssischaefer.savagerow.dto.RowDTO;
import nl.ssischaefer.savagerow.dto.RowSetDTO;
import nl.ssischaefer.savagerow.dto.TableSchemaDTO;
import nl.ssischaefer.savagerow.exceptions.NoDatabaseConnectionException;
import nl.ssischaefer.savagerow.integration.sqlite.metadata.JDBCMetaDataManager;
import nl.ssischaefer.savagerow.integration.sqlite.preparedstatements.PreparedSelectStatementBuilder;
import nl.ssischaefer.savagerow.integration.sqlite.utils.SQLiteDataSource;
import nl.ssischaefer.savagerow.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SQLiteQueryService implements QueryService {
    @Autowired
    private JDBCMetaDataManager jdbcManager;

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
    public TableSchemaDTO getSchema(String table) throws SQLException, NoDatabaseConnectionException {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        List<ColumnSchemaDTO> columns = jdbcManager.getTableMetaData(table)
                .getColumns()
                .stream()
                .map(columnMetaData -> new ColumnSchemaDTO()
                        .setColumn(columnMetaData.getName())
                        .setEditable(!columnMetaData.getAutoIncrement().equals("YES"))
                        .setNullable(columnMetaData.getNullable().equals("YES")))
                .collect(Collectors.toList());

        return new TableSchemaDTO().setTable(table).setColumns(columns);
    }

    @Override
    public List<String> listTables() throws Exception {
        return jdbcManager.getTables();
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


}
