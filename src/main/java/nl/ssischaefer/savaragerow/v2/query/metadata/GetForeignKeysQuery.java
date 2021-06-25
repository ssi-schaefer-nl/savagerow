package nl.ssischaefer.savaragerow.v2.query.metadata;

import nl.ssischaefer.savaragerow.v2.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetForeignKeysQuery {
    private String table;

    public GetForeignKeysQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public Map<String, String> execute() throws SQLException {
        Connection connection = SQLiteDataSource.getForCurrentWorkspace();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet fkResultSet = metaData.getImportedKeys(connection.getCatalog(), null, table);

        Map<String, String> fkColumns = new HashMap<>();
        while (fkResultSet.next()) {
            String fkColumnName = fkResultSet.getString("FKCOLUMN_NAME");
            String pkTableName = fkResultSet.getString("PKTABLE_NAME");
            String pkColumnName = fkResultSet.getString("PKCOLUMN_NAME");
            fkColumns.put(fkColumnName, String.format("%s.%s", pkTableName, pkColumnName));
        }

        fkResultSet.close();
        connection.close();

        return fkColumns;
    }

}

