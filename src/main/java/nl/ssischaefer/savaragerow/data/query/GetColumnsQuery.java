package nl.ssischaefer.savaragerow.data.query;

import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDatatype;
import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;
import nl.ssischaefer.savaragerow.WorkspaceConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetColumnsQuery {
    private String table;

    public GetColumnsQuery setTable(String table) {
        this.table = table;
        return this;
    }


    public List<SQLColumn> execute() throws SQLException {
        List<SQLColumn> columnSchemaList = new ArrayList<>();

        Connection connection = DriverManager.getConnection(WorkspaceConfiguration.getDatabaseUrl());
        ResultSet columnsResultSet = connection.getMetaData().getColumns(null, null, table, null);

        while (columnsResultSet.next()) {
            String name = columnsResultSet.getString("COLUMN_NAME");
            SQLColumn columnSchema = new SQLColumn()
                    .setName(name)
                    .setNullable(columnsResultSet.getString("IS_NULLABLE").equalsIgnoreCase("yes"))
                    .setDatatype(SQLiteDatatype.fromString(columnsResultSet.getString("TYPE_NAME")))
                    .setFk(null)
                    .setPk(false);

            columnSchemaList.add(columnSchema);
        }

        columnsResultSet.close();
        connection.close();
        return columnSchemaList;
    }

}
