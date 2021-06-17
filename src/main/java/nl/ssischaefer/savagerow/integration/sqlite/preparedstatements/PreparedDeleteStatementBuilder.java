package nl.ssischaefer.savagerow.integration.sqlite.preparedstatements;

import nl.ssischaefer.savagerow.integration.sqlite.metadata.ColumnMetaData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreparedDeleteStatementBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private Map<String, String> row;
    private List<ColumnMetaData> columnMetaData;

    public Connection getConnection() {
        return connection;
    }

    public PreparedDeleteStatementBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTable() {
        return table;
    }

    public PreparedDeleteStatementBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public PreparedDeleteStatementBuilder setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public List<ColumnMetaData> getColumnMetaData() {
        return columnMetaData;
    }

    public PreparedDeleteStatementBuilder setColumnMetaData(List<ColumnMetaData> columnMetaData) {
        this.columnMetaData = columnMetaData;
        return this;
    }


    public void execute() throws Exception {
        boolean succeeded = build();
        if(succeeded) statement.executeUpdate();
    }

    private boolean build() throws Exception {
        List<String> columnsNewRowSorted = row.keySet().stream().filter(r -> row.get(r) != null && !row.get(r).isEmpty()).sorted().collect(Collectors.toList());
        if(columnsNewRowSorted.isEmpty()) return false;

        String sql = createSqlString(columnsNewRowSorted);

        statement = connection.prepareStatement(sql);

        ParameterSetter parameterSetter = new ParameterSetter(statement);
        parameterSetter.setParameters(columnsNewRowSorted, row, columnMetaData);
        return true;
    }

    private String createSqlString(List<String> sortedColumns) {
        String where = sortedColumns.stream().map(col -> col + " = ?").collect(Collectors.joining(" AND "));

        return "DELETE FROM " + table + " WHERE " + where;
    }
}
