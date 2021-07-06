package nl.ssischaefer.savaragerow.v3.data.management.query;

import nl.ssischaefer.savaragerow.v3.data.common.AbstractQuery;
import nl.ssischaefer.savaragerow.v3.data.common.sql.SQLDDLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteTableQuery extends AbstractQuery {
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
