package nl.ssischaefer.savaragerow.query.dql;

import nl.ssischaefer.savaragerow.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.query.AbstractQuery;
import nl.ssischaefer.savaragerow.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.util.sql.SQLDQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GetRowQuery extends AbstractQuery {
    private String table;
    private Long rowId;
    private List<RowCriterion> criteria;

    public GetRowQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public GetRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public GetRowQuery setCriteria(List<RowCriterion> criteria) {
        this.criteria = criteria;
        return this;
    }


    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        PreparedStatement preparedStatement = null;

        if (rowId != null) {
            String sql = SQLDQLGenerator.generateSelectQuery(table, rowId);
            preparedStatement = sqlConnection.prepareStatement(sql);
        } else if (criteria != null) {
            String sql = SQLDQLGenerator.generateSelectQuery(table, criteria);
            preparedStatement = sqlConnection.prepareStatement(sql);
            PreparedStatementParameterHelper.setForRowCriteria(1, preparedStatement, criteria);
        } else {
            String sql = SQLDQLGenerator.generateSelectQuery(table);
            preparedStatement = sqlConnection.prepareStatement(sql);
        }

        return preparedStatement;

    }
}
