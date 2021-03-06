package nl.ssischaefer.savaragerow.data.common.sql;

import nl.ssischaefer.savaragerow.workflow.model.RowCriteria;

import java.util.List;

public class SQLDQLGenerator {
    public static String generateSelectQuery(String table, Long rowid) {
        String sql = generateSelectQuery(table);
        return sql.concat(" where rowid=" + rowid);
    }

    public static String generateSelectQuery(String table) {
        return String.format("select rowid as rowid, * from %s", table);
    }

    public static String generateSelectQuery(String table, List<RowCriteria> criteria) {
        String sql = generateSelectQuery(table);
        String whereClause = SQLClauseGenerator.generateWhereClause(criteria);
        return sql.concat(" " + whereClause);
    }



}
