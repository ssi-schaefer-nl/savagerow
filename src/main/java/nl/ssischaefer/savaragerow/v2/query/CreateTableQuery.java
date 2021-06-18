package nl.ssischaefer.savaragerow.v2.query;

import nl.ssischaefer.savaragerow.v2.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDatatype;

import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CreateTableQuery {
    private String tableName;
    private List<ColumnSchemaDTO> columns;
    private String sql;

    public List<ColumnSchemaDTO> getColumns() {
        return columns;
    }

    public CreateTableQuery setColumns(List<ColumnSchemaDTO> columns) {
        this.columns = columns;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public CreateTableQuery setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public void execute() throws SQLException {
        SQLiteDataSource.getConnection().createStatement().executeUpdate(sql);
    }

    public CreateTableQuery generate() throws SQLException {
        StringJoiner joiner = new StringJoiner(",");
        for (ColumnSchemaDTO columnSchema : columns) {
            String s = generateTableColumnDefinition(columnSchema);
            joiner.add(s);
        }
        String definition = joiner.toString();
        List<String> primaryKeyColumns = columns.stream().filter(ColumnSchemaDTO::isPk).map(ColumnSchemaDTO::getName).collect(Collectors.toList());
        if (!primaryKeyColumns.isEmpty())
            definition = definition.concat(String.format(", PRIMARY KEY(%s)", String.join(",", primaryKeyColumns)));

        sql = String.format("CREATE TABLE %s ( %s )", this.tableName, definition);
        return this;
    }

    private String generateTableColumnDefinition(ColumnSchemaDTO columnSchema) throws SQLException {
        String name = columnSchema.getName();
        SQLiteDatatype datatype = columnSchema.getDatatype();
        if (datatype == null) throw new SQLException("Invalid datatype " + columnSchema.getDatatype());

        String constraints = "";
        if ((columnSchema.isPk() && datatype != SQLiteDatatype.Integer) || !columnSchema.isNullable())
            constraints = constraints.concat("NOT NULL ");
        if (!columnSchema.isPk() && columnSchema.getDefaultValue() != null)
            constraints = constraints.concat("DEFAULT [" + columnSchema.getDefaultValue() + "] ");

        return String.format("%s %s %s", name, datatype.datatype, constraints);
    }

}
