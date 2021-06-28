package nl.ssischaefer.savaragerow.query.dml;

import nl.ssischaefer.savaragerow.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.query.AbstractUpdateQuery;
import nl.ssischaefer.savaragerow.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.util.sql.SQLDMLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteRowQuery extends AbstractUpdateQuery {
    private String table;
    private List<RowCriterion> criteria;
    private long row;

    public DeleteRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowQuery setRow(long row) {
        this.row = row;
        return this;
    }

    public DeleteRowQuery setCriteria(List<RowCriterion> criteria) {
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
