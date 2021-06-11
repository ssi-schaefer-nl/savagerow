package io.aero.v2.query;


import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeleteRowByCriteriaQuery {
    private String table;
    private Map<String, String> criteria;
    private String sql;
    private PreparedStatement preparedStatement;

    public DeleteRowByCriteriaQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowByCriteriaQuery setCriteria(Map<String, String> criteria) {
        this.criteria = criteria;
        return this;
    }

    public DeleteRowByCriteriaQuery generate() throws SQLException {
        List<String> criteriaColumns = this.criteria.keySet().stream().filter(k -> !criteria.get(k).isEmpty()).collect(Collectors.toList());
        String whereClause = criteriaColumns.stream().map(c -> String.format("%s = ?", c)).collect(Collectors.joining(" AND "));
        sql = String.format("DELETE FROM %s WHERE %s", this.table, whereClause);
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        int nextParamIndex = 0;

        for (String criteriaColumn : criteriaColumns) {
            nextParamIndex++;
            preparedStatement.setString(nextParamIndex, criteria.get(criteriaColumn));
        }

        return this;

    }

    public void execute() throws SQLException {
        preparedStatement.executeUpdate();
    }
}
