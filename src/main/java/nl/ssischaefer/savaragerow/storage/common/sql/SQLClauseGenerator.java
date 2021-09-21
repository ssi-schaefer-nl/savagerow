package nl.ssischaefer.savaragerow.storage.common.sql;

import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.storage.common.util.OperatorTransformer;

import java.util.List;
import java.util.stream.Collectors;

public class SQLClauseGenerator {
    public static String generateWhereClause(List<RowSelectionCriterion> criteria) {
        return String.format("WHERE %s", criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), OperatorTransformer.convertToSql(c.getComparator())))
                .collect(Collectors.joining(" AND ")));
    }

    public static String generateSetClause(List<UpdateInstruction> fieldUpdates) {
        return String.format("SET %s", fieldUpdates
                .stream()
                .map(f -> String.format("%s = %s", f.getField(), generateSetAction(f)))
                .collect(Collectors.joining(", ")));
    }


    private static String generateSetAction(UpdateInstruction fieldUpdate) {
        switch (fieldUpdate.getOperation()) {
            case "subtract":
                return String.format("%s - ?", fieldUpdate.getField());
            case "add":
                return String.format("%s + ?", fieldUpdate.getField());
            case "multiply":
                return String.format("%s * ?", fieldUpdate.getField());
            default:
                return "?";
        }
    }

}
