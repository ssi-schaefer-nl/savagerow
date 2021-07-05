package nl.ssischaefer.savaragerow.v1.util.sql;

import nl.ssischaefer.savaragerow.v1.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.v1.workflow.RowCriterion;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PreparedStatementParameterHelper {
    public static int setForRowCriteria(int startIndex, PreparedStatement preparedStatement, List<RowCriterion> criteria) throws SQLException {
        int nextParamIndex = startIndex;
        for (RowCriterion c : criteria) {

            SQLComparator op = c.getComparator();
            String val = c.getRequired();

            if (op.equals(SQLComparator.GREATER) || op.equals(SQLComparator.SMALLER))
                preparedStatement.setLong(nextParamIndex, Long.parseLong(val));
            else
                preparedStatement.setString(nextParamIndex, val);

            nextParamIndex++;
        }
        return nextParamIndex;
    }

    public static int setForRow(PreparedStatement preparedStatement, Map<String, String> row, List<String> columnsForOrder) throws SQLException {
        int i;
        for (i = 0; i < columnsForOrder.size(); i++) {
            String fieldValue = row.get(columnsForOrder.get(i));
            preparedStatement.setString(i + 1, fieldValue.isEmpty() ? null : fieldValue);
        }
        return i;
    }

    public static int setForFieldUpdate(int startIndex, PreparedStatement preparedStatement, List<FieldUpdate> fieldUpdates) throws SQLException {
        int nextParamIndex = startIndex;
        for (FieldUpdate update : fieldUpdates) {
            if (update.getAction().equals(SQLSetAction.SET))
                preparedStatement.setString(nextParamIndex, update.getValue());
            else
                preparedStatement.setLong(nextParamIndex, Long.parseLong(update.getValue()));
            nextParamIndex++;

        }
        return nextParamIndex;
    }
}
