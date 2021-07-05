package nl.ssischaefer.savaragerow.v2.data.operations.query.dql;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLDQLGenerator;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.RowCriterion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FindRowQuery extends AbstractFindQuery {
    private String table;
    private Long rowId;
    private List<RowCriterion> criteria;

    public FindRowQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public FindRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public FindRowQuery setCriteria(List<RowCriterion> criteria) {
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
