package nl.ssischaefer.savaragerow.query.ddl;

import nl.ssischaefer.savaragerow.dto.SQLColumn;
import nl.ssischaefer.savaragerow.query.AbstractUpdateQuery;
import nl.ssischaefer.savaragerow.util.sql.SQLDDLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CreateTableQuery extends AbstractUpdateQuery {
    private String tableName;
    private List<SQLColumn> columns;

    public CreateTableQuery setColumns(List<SQLColumn> columns) {
        this.columns = columns;
        return this;
    }

    public CreateTableQuery setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        String sql = SQLDDLGenerator.generateCreateTable(tableName, columns);
        return sqlConnection.prepareStatement(sql);
    }


}
