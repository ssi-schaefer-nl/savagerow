package nl.ssischaefer.savaragerow.v2.util.sql;

import nl.ssischaefer.savaragerow.v2.dto.SQLColumn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLDDLGenerator {

    public static String generateCreateTable(String tableName, List<SQLColumn> columns) throws SQLException {
        String columnsDefinition = generateColumnsDefinition(columns);
        String pkDefinition = generatePkClause(columns.stream().filter(SQLColumn::isPk).map(SQLColumn::getName).collect(Collectors.toList()));
        String fkDefinition = generateFkClause(columns.stream().filter(c -> c.getFk() != null).collect(Collectors.toList()));

        String tableDefinition = columnsDefinition;
        if(pkDefinition.length() > 0) tableDefinition = tableDefinition.concat(", " + pkDefinition);
        if(fkDefinition.length() > 0) tableDefinition = tableDefinition.concat(", " + fkDefinition);

        return String.format("CREATE TABLE %s ( %s )", tableName, tableDefinition);
    }

    private static String generateTableColumnDefinition(SQLColumn columnSchema) throws SQLException {
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

    private static String generateFkClause(List<SQLColumn> fkColumns) {
        List<String> references = new ArrayList<>();
        for (SQLColumn col : fkColumns) {
            String[] fkDefinition = col.getFk().split("\\.");
            if (fkDefinition.length != 2) continue;

            String fkTable = fkDefinition[0];
            String fkColumn = fkDefinition[1];

            references.add(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)", col.getName(), fkTable, fkColumn));
        }
        if (references.isEmpty()) return "";
        return String.format("%s", String.join(", ", references));
    }

    private static String generatePkClause(List<String> columnNames) {
        if (!columnNames.isEmpty())
            return String.format("PRIMARY KEY(%s)", String.join(", ", columnNames));
        return "";
    }

    private static String generateColumnsDefinition(List<SQLColumn> columns) throws SQLException {
        List<String> columnsDefinition = new ArrayList<>();
        for (SQLColumn columnSchema : columns) {
            columnsDefinition.add(generateTableColumnDefinition(columnSchema));
        }
        return String.join(", ", columnsDefinition);
    }

    public static String generateRenameTableQuery(String from, String to) {
        return String.format("ALTER TABLE %s RENAME TO %s", from, to);

    }

    public static String generateDeleteTableQuery(String table) {
        return String.format("DROP TABLE %s", table);
    }

    public static String generateRenameColumnQuery(String table, String from, String to) {
        return String.format("ALTER TABLE %s RENAME COLUMN %s TO %s", table, from, to);
    }
}
