package nl.ssischaefer.savaragerow.v3.data.operations.query;

import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.v3.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.v3.data.common.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v3.data.common.sql.SQLDMLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteRowQuery extends AbstractQuery {
    private String table;
    private List<RowCriteria> criteria;
    private long row;

    public DeleteRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowQuery setRow(long row) {
        this.row = row;
        return this;
    }

    public DeleteRowQuery setCriteria(List<RowCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        if(criteria != null) {
            String sql = SQLDMLGenerator.generateDeleteQuery(table, criteria);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            PreparedStatementParameterHelper.setForRowCriteria(1, preparedStatement, criteria);
            return preparedStatement;
        }
        else {
            String sql = SQLDMLGenerator.generateDeleteQuery(table, row);
            return sqlConnection.prepareStatement(sql);
        }
    }
}
