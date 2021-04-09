package io.aero.integration.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCManager {
    private Connection connection;
    private final Map<String, TableMetaDataCacheEntry> tableMetaDataCache;

    public JDBCManager() throws SQLException {
        this.tableMetaDataCache = new HashMap<>();
        connectToDb("chinook.db");
    }


    public void connectToDb(String db) throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + db);

    }

    public List<String> getTables() throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString(3);
            updateTableMetaDataForTable(tableName);
            tables.add(tableName);
        }

        return tables;
    }

    public boolean tableNotExistsInDb(String table) throws SQLException {
        if (tableMetaDataCache.containsKey(table)) {
            return false;
        }
        return !getTables().contains(table);
    }

    public List<ColumnMetaData> getColumnsForTable(String table) throws SQLException {
        TableMetaDataCacheEntry cacheEntry = getTableMetaData(table);
        return cacheEntry.getColumns();
    }

    public Connection getConnection() {
        return connection;
    }

    public TableMetaDataCacheEntry getTableMetaData(String table) throws SQLException {
        TableMetaDataCacheEntry cacheEntry = tableMetaDataCache.get(table);
        if (cacheEntry != null && !cacheEntry.isExpired()) {
            return cacheEntry;
        } else {
            return updateTableMetaDataForTable(table);
        }
    }

    private TableMetaDataCacheEntry updateTableMetaDataForTable(String table) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table, null);

        List<ColumnMetaData> columns = new ArrayList<>();
        while (resultSet.next()) {
            columns.add(new ColumnMetaData()
                    .setDatatype(Integer.parseInt(resultSet.getString("DATA_TYPE")))
                    .setName(resultSet.getString("COLUMN_NAME"))
                    .setAutoIncrement(resultSet.getString("IS_AUTOINCREMENT"))
            );
        }
        TableMetaDataCacheEntry cacheEntry = new TableMetaDataCacheEntry().setName(table).setColumns(columns);
        tableMetaDataCache.put(table, cacheEntry);
        return cacheEntry;
    }


}
