package io.aero.v2.query;


import io.aero.v2.dto.RowDTO;
import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InsertRowQuery {
    private String table;
    private RowDTO data;
    private String sql;
    private PreparedStatement preparedStatement;
    private long generatedKey;

    public String getTable() {
        return table;
    }

    public InsertRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public RowDTO getData() {
        return data;
    }

    public InsertRowQuery setData(RowDTO data) {
        this.data = data;
        return this;
    }

    public InsertRowQuery generate() throws SQLException {
        List<String> columns = new ArrayList<>(data.getRow().keySet());
        sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, String.join(",", columns), String.join(",", Collections.nCopies(columns.size(), "?")));
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        for (int i = 0; i < columns.size(); i++) {
            preparedStatement.setString(i + 1, data.getRow().get(columns.get(i)));
        }
        return this;
    }

    public InsertRowQuery execute() throws SQLException {
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if(generatedKeys.next()) this.generatedKey = generatedKeys.getLong(1);
        return this;
    }

    public List<Map<String, String>> getResult() throws SQLException {
        return new GetRowQuery().setTable(table).setRowId(Long.valueOf(generatedKey).intValue()).generate().execute().getResult();
    }
}
