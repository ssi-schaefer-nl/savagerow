package nl.ssischaefer.savaragerow.data.common.sql;

import nl.ssischaefer.savaragerow.workflow.model.FieldUpdate;
import nl.ssischaefer.savaragerow.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.data.common.util.OperatorTransformer;

import java.util.List;
import java.util.stream.Collectors;

public class SQLClauseGenerator {
    public static String generateWhereClause(List<RowCriteria> criteria) {
        return String.format("WHERE %s", criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), OperatorTransformer.convertToSql(c.getComparator())))
                .collect(Collectors.joining(" AND ")));
    }

    public static String generateSetClause(List<FieldUpdate> fieldUpdates) {
        return String.format("SET %s", fieldUpdates
                .stream()
                .map(f -> String.format("%s = %s", f.getColumn(), generateSetAction(f)))
                .collect(Collectors.joining(", ")));
    }


    private static String generateSetAction(FieldUpdate fieldUpdate) {
        switch (fieldUpdate.getAction()) {
            case "subtract":
                return String.format("%s - ?", fieldUpdate.getColumn());
            case "add":
                return String.format("%s + ?", fieldUpdate.getColumn());
            case "multiply":
                return String.format("%s * ?", fieldUpdate.getColumn());
            default:
                return "?";
        }
    }

}
