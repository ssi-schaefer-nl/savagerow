package nl.ssischaefer.savaragerow.data.operations.query;

import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.common.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.data.common.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.data.common.sql.SQLDMLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateRowQuery extends AbstractQuery {
    private String table;
    private Map<String, String> row;
    private List<RowSelectionCriterion> criteria;
    private List<UpdateInstruction> updateInstructions;
    private Long rowId;

    public UpdateRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public UpdateRowQuery setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public UpdateRowQuery setCriteria(List<RowSelectionCriterion> criteria) {
        this.criteria = criteria;
        return this;
    }

    public UpdateRowQuery setUpdateInstructions(List<UpdateInstruction> updateInstructions) {
        this.updateInstructions = updateInstructions;
        return this;
    }

    public UpdateRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws Exception {
        if(criteria != null && updateInstructions != null ) {
            String sql = SQLDMLGenerator.generateUpdateQuery(table, updateInstructions, criteria);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);

            int nextIndex = PreparedStatementParameterHelper.setForUpdateInstructions( 1, preparedStatement, updateInstructions);
            PreparedStatementParameterHelper.setForRowCriteria(nextIndex, preparedStatement, criteria);
            return preparedStatement;
        }
        else {

            List<String> columns = row.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            String sql = SQLDMLGenerator.generateUpdateQuery(table, columns, rowId);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            PreparedStatementParameterHelper.setForRow(preparedStatement, row, columns);
            return preparedStatement;
        }
    }




}
