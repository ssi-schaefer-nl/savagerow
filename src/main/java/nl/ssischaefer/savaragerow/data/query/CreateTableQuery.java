package nl.ssischaefer.savaragerow.data.query;

import nl.ssischaefer.savaragerow.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.data.common.sql.SQLDDLGenerator;
import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CreateTableQuery extends AbstractQuery {
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
