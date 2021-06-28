package nl.ssischaefer.savaragerow.query.metadata;

import nl.ssischaefer.savaragerow.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetTablesQuery {
    private List<String> result;

    public GetTablesQuery execute() throws SQLException {
        Connection connection = SQLiteDataSource.getForCurrentWorkspace();
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
