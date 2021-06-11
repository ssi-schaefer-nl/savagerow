package io.aero.v2.query;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateRowByCriteriaQuery {
    private String table;
    private RowDTO row;
    private Map<String, String> criteria;
    private String sql;
    private PreparedStatement preparedStatement;

    public UpdateRowByCriteriaQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public UpdateRowByCriteriaQuery setRow(RowDTO row) {
        this.row = row;
        return this;
    }


    public UpdateRowByCriteriaQuery setCriteria(Map<String, String> criteria) {
        this.criteria = criteria;
        return this;
    }

    public UpdateRowByCriteriaQuery generate() throws SQLException {
        List<String> columns = this.row.getRow().keySet().stream().filter(k -> !row.getRow().get(k).isEmpty()).collect(Collectors.toList());
        List<String> criteriaColumns = this.criteria.keySet().stream().filter(k -> !criteria.get(k).isEmpty()).collect(Collectors.toList());
        String whereClause = criteriaColumns.stream().map(c -> String.format("%s = ?", c)).collect(Collectors.joining(" AND "));

        sql = String.format("UPDATE %s SET %s WHERE %s", this.table, columns.stream().map(col -> String.format("%s = ?", col)).collect(Collectors.joining(", ")), whereClause);
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        int nextParamIndex = 0;

        for (String column : columns) {
            nextParamIndex++;
            preparedStatement.setString(nextParamIndex, row.getRow().get(column));

        }

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
