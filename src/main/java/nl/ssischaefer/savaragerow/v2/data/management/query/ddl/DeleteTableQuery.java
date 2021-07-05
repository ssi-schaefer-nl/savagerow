package nl.ssischaefer.savaragerow.v2.data.management.query.ddl;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLDDLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteTableQuery extends AbstractDDLQuery {
    private String table;

    public DeleteTableQuery setTable(String table) {
        this.table = table;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        String sql = SQLDDLGenerator.generateDeleteTableQuery(table);
        return sqlConnection.prepareStatement(sql);
    }
}
