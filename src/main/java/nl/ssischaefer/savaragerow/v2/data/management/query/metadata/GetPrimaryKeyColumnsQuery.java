package nl.ssischaefer.savaragerow.v2.data.management.query.metadata;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        Connection connection = SQLiteDataSource.getForCurrentWorkspace();
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
