package nl.ssischaefer.savaragerow.v3.data.common.sql;

import nl.ssischaefer.savaragerow.v3.workflow.model.FieldUpdate;
import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SQLDMLGenerator {
    public static String generateDeleteQuery(String table) {
        return String.format("DELETE FROM %s", table);
    }
    public static String generateDeleteQuery(String table, Long rowid) {
        String sql = generateDeleteQuery(table);
        return sql.concat(" WHERE rowid = " + rowid);
    }

    public static String generateDeleteQuery(String table, List<RowCriteria> criteria) {
        String whereClause = SQLClauseGenerator.generateWhereClause(criteria);
        String sql = generateDeleteQuery(table);
        return sql.concat(" " + whereClause);
    }

    public static String generateInsertIntoQuery(String table, List<String> columns) {
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                table, String.join(",", columns), String.join(",", Collections.nCopies(columns.size(), "?"))
        );
    }

    public static String generateInsertIntoQuery(String toTable, String fromTable, List<String> columns) {
        return String.format("INSERT into %s (%s) SELECT %s from %s", toTable, String.join(", ", columns),  String.join(", ", columns), fromTable);

    }

    public static String generateUpdateQuery(String table, List<String> columns, Long rowid) {
        String setColumns = columns.stream().map(col -> String.format("%s = ?", col)).collect(Collectors.joining(", "));
        return String.format("UPDATE %s SET %s WHERE rowid = %s", table, setColumns, rowid);
    }

    public static String generateUpdateQuery(String table, List<FieldUpdate> fieldUpdates, List<RowCriteria> criteria) {
        String setClause = SQLClauseGenerator.generateSetClause(fieldUpdates);
        String whereClause = SQLClauseGenerator.generateWhereClause(criteria);
        return String.format("UPDATE %s %s %s", table, setClause, whereClause);
    }

}
