package io.aero.v2.query;

import io.aero.v2.util.SQLiteDataSource;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetTablesQuery {
    private List<String> result;

    public GetTablesQuery execute() throws SQLException {
        DatabaseMetaData md = SQLiteDataSource.getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString(3);
            tables.add(tableName);
        }
        this.result = tables;
        return this;
    }

    public List<String> getResult() {
        return result;
    }
}