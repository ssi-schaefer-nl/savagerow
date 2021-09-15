package nl.ssischaefer.savaragerow.data.operations.query;

import nl.ssischaefer.savaragerow.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.data.common.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.data.common.sql.SQLDQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FindRowQuery extends AbstractQuery {
    private String table;
    private Long rowId;
    private Integer top;
    private List<RowSelectionCriterion> criteria;

    public FindRowQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public FindRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public FindRowQuery setCriteria(List<RowSelectionCriterion> criteria) {
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
        } else if (top != null) {
            String sql = SQLDQLGenerator.generateSelectQuery(table, top);
            preparedStatement = sqlConnection.prepareStatement(sql);
        } else {
            String sql = SQLDQLGenerator.generateSelectQuery(table);
            preparedStatement = sqlConnection.prepareStatement(sql);
        }

        return preparedStatement;

    }

    public FindRowQuery setTop(Integer top) {
        this.top = top;
        return this;
    }
}
