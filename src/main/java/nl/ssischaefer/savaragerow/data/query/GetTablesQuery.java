package nl.ssischaefer.savaragerow.data.management.query;

import nl.ssischaefer.savaragerow.WorkspaceConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetTablesQuery {
    private List<String> result;

    public GetTablesQuery execute() throws SQLException {
        Connection connection = DriverManager.getConnection(WorkspaceConfiguration.getDatabaseUrl());
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString(3);
            tables.add(tableName);
        }
        this.result = tables;
        rs.close();
        connection.close();
        return this;
    }

    public List<String> getResult() {
        return result;
    }
}
