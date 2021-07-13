package nl.ssischaefer.savaragerow.data.management.query;

import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDataSource;
import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDatatype;
import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;

import java.sql.Connection;
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

        Connection connection = SQLiteDataSource.getForCurrentWorkspace();
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
