package nl.ssischaefer.savaragerow.v2.query.dml;

import nl.ssischaefer.savaragerow.v2.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.v2.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.v2.query.AbstractUpdateQuery;
import nl.ssischaefer.savaragerow.v2.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v2.util.sql.SQLDMLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateRowQuery extends AbstractUpdateQuery {
    private String table;
    private Map<String, String> row;
    private List<RowCriterion> criteria;
    private List<FieldUpdate> fieldUpdates;
    private Long rowId;

    public UpdateRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public UpdateRowQuery setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public UpdateRowQuery setCriteria(List<RowCriterion> criteria) {
        this.criteria = criteria;
        return this;
    }

    public UpdateRowQuery setFieldUpdates(List<FieldUpdate> fieldUpdates) {
        this.fieldUpdates = fieldUpdates;
        return this;
    }

    public UpdateRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws Exception {
        if(criteria != null && fieldUpdates != null ) {
            String sql = SQLDMLGenerator.generateUpdateQuery(table, fieldUpdates, criteria);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);

            int nextIndex = PreparedStatementParameterHelper.setForFieldUpdate( 1, preparedStatement, fieldUpdates);
            PreparedStatementParameterHelper.setForRowCriteria(nextIndex, preparedStatement, criteria);
            return preparedStatement;
        }
        else {
            List<String> columns = new ArrayList<>(row.keySet());
            String sql = SQLDMLGenerator.generateUpdateQuery(table, columns, rowId);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            PreparedStatementParameterHelper.setForRow(preparedStatement, row, columns);
            return preparedStatement;
        }
    }




}
