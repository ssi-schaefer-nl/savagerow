package nl.ssischaefer.savaragerow.v2.query.dql;

import nl.ssischaefer.savaragerow.v2.workflow.RowCriteria;
import nl.ssischaefer.savaragerow.v2.query.AbstractQuery;
import nl.ssischaefer.savaragerow.v2.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v2.util.sql.SQLDQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GetRowQuery extends AbstractQuery {
    private String table;
    private Long rowId;
    private List<RowCriteria> criteria;

    public GetRowQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public GetRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public GetRowQuery setCriteria(List<RowCriteria> criteria) {
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
