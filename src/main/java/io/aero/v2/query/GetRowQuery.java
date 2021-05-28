package io.aero.v2.query;

import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRowQuery {
    private String table;
    private Integer rowId;
    private PreparedStatement preparedStatement;
    private List<Map<String, String>> result;

    public String getTable() {
        return table;
    }

    public GetRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public Integer getRowId() {
        return rowId;
    }

    public GetRowQuery setRowId(Integer rowId) {
        this.rowId = rowId;
        return this;
    }

    public GetRowQuery generate() throws SQLException {
        String sql = String.format("select rowid as rowid, * from %s", table);
        if (rowId != null) {
            sql = sql.concat(" where rowid=?");
            preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, rowId);
        }
        else {
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
}
