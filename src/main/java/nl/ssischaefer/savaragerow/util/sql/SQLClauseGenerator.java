package nl.ssischaefer.savaragerow.util.sql;

import nl.ssischaefer.savaragerow.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.workflow.RowCriterion;

import java.util.List;
import java.util.stream.Collectors;

public class SQLClauseGenerator {

    public static String generateWhereClause(List<RowCriterion> criteria) {
        return String.format("WHERE %s", criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), c.getComparator().getComparator()))
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
            case SUBTRACT:
                return String.format("%s - ?", fieldUpdate.getColumn());
            case ADD:
                return String.format("%s + ?", fieldUpdate.getColumn());
            case MULTIPLY:
                return String.format("%s * ?", fieldUpdate.getColumn());
            default:
                return "?";
        }
    }

}
