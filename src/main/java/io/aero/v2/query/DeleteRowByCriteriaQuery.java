package io.aero.v2.query;


import io.aero.v2.model.action.RowCriteria;
import io.aero.v2.util.OperatorToSQLComparator;
import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeleteRowByCriteriaQuery {
    private String table;
    private List<RowCriteria> criteria;
    private String sql;
    private PreparedStatement preparedStatement;

    public DeleteRowByCriteriaQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowByCriteriaQuery setCriteria(List<RowCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    public DeleteRowByCriteriaQuery generate() throws SQLException {
        String whereClause = criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), OperatorToSQLComparator.convert(c.getOperator())))
                .collect(Collectors.joining(" AND "));


        sql = String.format("DELETE FROM %s WHERE %s", this.table, whereClause);
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        int nextParamIndex = 0;

        for (RowCriteria c : criteria) {
            nextParamIndex++;
            preparedStatement.setString(nextParamIndex, c.getRequired());
        }

        return this;

    }

    public void execute() throws SQLException {
        preparedStatement.executeUpdate();
    }
}
