package nl.ssischaefer.savaragerow.v2.query;

import nl.ssischaefer.savaragerow.v2.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v2.dto.TableSchemaDTO;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDatatype;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTableSchema {
    private String table;
    private TableSchemaDTO tableSchema;

    public String getTable() {
        return table;
    }

    public GetTableSchema setTable(String table) {
        this.table = table;
        return this;
    }

    public GetTableSchema execute() throws SQLException {
        tableSchema = new TableSchemaDTO();
        tableSchema.setName(table);
        tableSchema.setColumns(getColumnSchema());
        return this;
    }

    public TableSchemaDTO getResult() {
        return this.tableSchema;
    }

    private List<ColumnSchemaDTO> getColumnSchema() throws SQLException {
        Map<String, String> foreignkeys = getForeignKeys();
        List<String> primarykeys = getPrimaryKeys();

        List<ColumnSchemaDTO> columnSchemaList = new ArrayList<>();
        DatabaseMetaData metaData = SQLiteDataSource.getConnection().getMetaData();
        ResultSet columns = metaData.getColumns(null, null, table, null);

        while (columns.next()) {
            String name = columns.getString("COLUMN_NAME");

            ColumnSchemaDTO columnSchema = new ColumnSchemaDTO()
                    .setName(name)
                    .setNullable(columns.getString("IS_NULLABLE").equalsIgnoreCase("yes"))
                    .setFk(foreignkeys.get(name))
                    .setDatatype(SQLiteDatatype.fromString(columns.getString("TYPE_NAME")))
                    .setPk(primarykeys.contains(name));
            columnSchemaList.add(columnSchema);
        }
        return columnSchemaList;
    }

    private List<String> getPrimaryKeys() throws SQLException {
        DatabaseMetaData metaData = SQLiteDataSource.getConnection().getMetaData();
        ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, table);
        List<String> pkColumns = new ArrayList<>();
        while(pkResultSet.next()) {
            pkColumns.add(pkResultSet.getString(4));
        }
        return pkColumns;
    }

    private Map<String, String> getForeignKeys() throws SQLException {
        DatabaseMetaData metaData = SQLiteDataSource.getConnection().getMetaData();
        ResultSet foreignKeys = metaData.getImportedKeys(SQLiteDataSource.getConnection().getCatalog(), null, table);
        Map<String, String> fkColumns = new HashMap<>();
        while (foreignKeys.next()) {
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            String pkTableName = foreignKeys.getString("PKTABLE_NAME");
            String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
            fkColumns.put(fkColumnName, String.format("%s.%s", pkTableName, pkColumnName));
        }
        return fkColumns;
    }

}
