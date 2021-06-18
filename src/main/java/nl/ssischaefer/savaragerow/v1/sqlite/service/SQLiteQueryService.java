package nl.ssischaefer.savaragerow.v1.sqlite.service;

import nl.ssischaefer.savaragerow.v1.exceptions.NoDatabaseConnectionException;
import nl.ssischaefer.savaragerow.v1.sqlite.metadata.JDBCMetaDataManager;
import nl.ssischaefer.savaragerow.v1.sqlite.utils.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v1.service.QueryService;
import nl.ssischaefer.savaragerow.v1.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v1.dto.RowDTO;
import nl.ssischaefer.savaragerow.v1.dto.RowSetDTO;
import nl.ssischaefer.savaragerow.v1.dto.TableSchemaDTO;
import nl.ssischaefer.savaragerow.v1.sqlite.preparedstatements.PreparedSelectStatementBuilder;
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
