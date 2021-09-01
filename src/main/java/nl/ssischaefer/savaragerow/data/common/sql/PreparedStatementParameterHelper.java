package nl.ssischaefer.savaragerow.data.common.sql;

import nl.ssischaefer.savaragerow.common.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class PreparedStatementParameterHelper {
    public static int setForRowCriteria(int startIndex, PreparedStatement preparedStatement, List<RowSelectionCriterion> criteria) throws SQLException {
        int nextParamIndex = startIndex;
        for (RowSelectionCriterion c : criteria) {

            String op = c.getComparator();
            String val = c.getValue();

            if (op.equals(">") || op.equals("<"))
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
            if(fieldValue == null) preparedStatement.setNull(i + 1, Types.VARCHAR);
            else
                preparedStatement.setString(i + 1, fieldValue.isEmpty() ? null : fieldValue);
        }
        return i;
    }

    public static int setForUpdateInstructions(int startIndex, PreparedStatement preparedStatement, List<UpdateInstruction> fieldUpdates) throws SQLException {
        int nextParamIndex = startIndex;
        for (UpdateInstruction update : fieldUpdates) {
            if (update.getOperation().equals("set"))
                preparedStatement.setString(nextParamIndex, update.getValue());
            else
                preparedStatement.setLong(nextParamIndex, Long.parseLong(update.getValue()));
            nextParamIndex++;

        }
        return nextParamIndex;
    }
}
