package nl.ssischaefer.savaragerow.v3.data.operations.query;

import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.v3.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.v3.data.common.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v3.data.common.sql.SQLDQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FindRowQuery extends AbstractQuery {
    private String table;
    private Long rowId;
    private List<RowCriteria> criteria;

    public FindRowQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public FindRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public FindRowQuery setCriteria(List<RowCriteria> criteria) {
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
