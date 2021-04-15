package io.aero.integration.sqlite;


import io.aero.exceptions.NoDatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCMetaDataManager {
    private final Map<String, TableMetaDataCacheEntry> tableMetaDataCache;

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
        if (cacheEntry != null && !cacheEntry.isExpired()) {
            return cacheEntry;
        } else {
            return updateTableMetaDataForTable(table);
        }
    }

    private TableMetaDataCacheEntry updateTableMetaDataForTable(String table) throws SQLException, NoDatabaseConnectionException {
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


}
