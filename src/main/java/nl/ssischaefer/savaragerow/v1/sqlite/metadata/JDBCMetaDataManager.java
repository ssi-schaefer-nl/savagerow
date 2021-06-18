package nl.ssischaefer.savaragerow.v1.sqlite.metadata;


import nl.ssischaefer.savaragerow.v1.exceptions.NoDatabaseConnectionException;
import nl.ssischaefer.savaragerow.v1.sqlite.utils.SQLiteDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JDBCMetaDataManager {
    private final Map<String, TableMetaDataCacheEntry> tableMetaDataCache;

    @Autowired
    @Qualifier("cacheExpirationSeconds")
    private Long cacheExpirationSeconds;

    public JDBCMetaDataManager() {
        this.tableMetaDataCache = new HashMap<>();
    }


    public List<String> getTables() throws SQLException, NoDatabaseConnectionException {
        DatabaseMetaData md = SQLiteDataSource.getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString(3);
            updateTableMetaDataForTable(tableName);
            tables.add(tableName);
        }

        return tables;
    }

    public boolean tableNotExistsInDb(String table) throws SQLException, NoDatabaseConnectionException {
        if (tableMetaDataCache.containsKey(table)) {
            return false;
        }
        return !getTables().contains(table);
    }

    public List<ColumnMetaData> getColumnsForTable(String table) throws SQLException, NoDatabaseConnectionException {
        TableMetaDataCacheEntry cacheEntry = getTableMetaData(table);
        return cacheEntry.getColumns();
    }

    public TableMetaDataCacheEntry getTableMetaData(String table) throws SQLException, NoDatabaseConnectionException {
        TableMetaDataCacheEntry cacheEntry = tableMetaDataCache.get(table);

        if (cacheEntry != null && !cacheEntry.isExpired(cacheExpirationSeconds)) {
            return cacheEntry;
        } else {
            return updateTableMetaDataForTable(table);
        }
    }

    public TableMetaDataCacheEntry updateTableMetaDataForTable(String table) throws SQLException, NoDatabaseConnectionException {

        DatabaseMetaData databaseMetaData = SQLiteDataSource.getConnection().getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table, null);

        List<ColumnMetaData> columns = new ArrayList<>();
        while (resultSet.next()) {
            columns.add(new ColumnMetaData()
                    .setDatatype(Integer.parseInt(resultSet.getString("DATA_TYPE")))
                    .setName(resultSet.getString("COLUMN_NAME"))
                    .setAutoIncrement(resultSet.getString("IS_AUTOINCREMENT"))
                    .setNullable(resultSet.getString("IS_NULLABLE"))
            );
        }
        TableMetaDataCacheEntry cacheEntry = new TableMetaDataCacheEntry().setName(table).setColumns(columns);
        tableMetaDataCache.put(table, cacheEntry);
        return cacheEntry;
    }

    public boolean notAllColumnsExistInTable(String table, List<String> columns) throws SQLException, NoDatabaseConnectionException {
        return !getColumnsForTable(table)
                .stream()
                .map(ColumnMetaData::getName)
                .collect(Collectors.toList())
                .containsAll(columns);
    }


}
