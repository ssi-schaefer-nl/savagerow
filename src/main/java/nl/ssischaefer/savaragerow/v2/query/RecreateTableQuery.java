package nl.ssischaefer.savaragerow.v2.query;

import nl.ssischaefer.savaragerow.v2.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RecreateTableQuery {
    private String table;
    private List<ColumnSchemaDTO> columns;
    private String copyRowsSql;
    private String renameTableSql;
    private String deleteTableSql;

    public String getTable() {
        return table;
    }

    public RecreateTableQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public List<ColumnSchemaDTO> getColumns() {
        return columns;
    }

    public RecreateTableQuery setColumns(List<ColumnSchemaDTO> columns) {
        this.columns = columns;
        return this;
    }

    public RecreateTableQuery generate() throws SQLException {
        List<String> newColumns = columns.stream().map(ColumnSchemaDTO::getName).collect(Collectors.toList());
        String oldColumns = new GetTableSchema().setTable(table).execute().getResult().getColumns().stream().map(ColumnSchemaDTO::getName).filter(newColumns::contains).collect(Collectors.joining(", "));
        copyRowsSql = String.format("INSERT into %s (%s) SELECT %s from %s", table + "_temp", oldColumns,  oldColumns, table);
        renameTableSql = String.format("ALTER TABLE %s RENAME TO %s", table+"_temp", table);
        deleteTableSql = String.format("DROP TABLE %s", table);

        return this;
    }

    public void execute() throws SQLException {
        new CreateTableQuery().setTableName(table+"_temp").setColumns(columns).generate().execute();
        execute(copyRowsSql);
        execute(deleteTableSql);
        execute(renameTableSql);
    }

    private void execute(String sql) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = SQLiteDataSource.get().prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}

