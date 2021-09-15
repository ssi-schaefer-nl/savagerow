package nl.ssischaefer.savaragerow.data.query;

import nl.ssischaefer.savaragerow.WorkspaceConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetPrimaryKeyColumnsQuery {
    private String table;

    public GetPrimaryKeyColumnsQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public List<String> execute() throws SQLException {
        List<String> pkColumns = new ArrayList<>();

        Connection connection = DriverManager.getConnection(WorkspaceConfiguration.getDatabaseUrl());
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, table);

        while(pkResultSet.next()) {
            pkColumns.add(pkResultSet.getString(4));
        }

        pkResultSet.close();
        connection.close();
        return pkColumns;
    }

}
