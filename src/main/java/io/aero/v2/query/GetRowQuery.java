package io.aero.v2.query;

import io.aero.v2.model.RowCriteria;
import io.aero.v2.util.OperatorTransformer;
import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetRowQuery {
    private String table;
    private Long rowId;
    private PreparedStatement preparedStatement;
    private List<Map<String, String>> result;
    private List<RowCriteria> criteria;

    public String getTable() {
        return table;
    }

    public GetRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public Long getRowId() {
        return rowId;
    }

    public GetRowQuery setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public GetRowQuery generate() throws SQLException {
        String sql = String.format("select rowid as rowid, * from %s", table);
        if (rowId != null) {
            sql = sql.concat(" where rowid=?");
            preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
            preparedStatement.setLong(1, rowId);
        } else if (criteria != null) {
            String whereClause = criteria.stream()
                    .map(c -> String.format("%s %s ?", c.getColumn(), OperatorTransformer.convertToSql(c.getOperator())))
                    .collect(Collectors.joining(" AND "));
            sql = sql.concat(String.format(" WHERE %s", whereClause));
            int nextParamIndex = 0;
            preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
            for (RowCriteria c : criteria) {
                nextParamIndex++;
                String op = c.getOperator();
                if (op.equals(">") || op.equals("<"))
                    preparedStatement.setLong(nextParamIndex, Long.parseLong(c.getRequired()));
                else
                    preparedStatement.setString(nextParamIndex, c.getRequired());
            }
        } else {
            preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        }

        return this;
    }

    public GetRowQuery execute() throws SQLException {
        List<Map<String, String>> rows = new ArrayList<>();
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Map<String, String> cols = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                cols.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            rows.add(cols);
        }

        this.result = rows;
        return this;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }

    public GetRowQuery setCriteria(List<RowCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }
}
