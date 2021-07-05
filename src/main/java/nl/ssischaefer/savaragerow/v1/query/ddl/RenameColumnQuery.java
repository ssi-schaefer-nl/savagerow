package nl.ssischaefer.savaragerow.v1.query.ddl;

import nl.ssischaefer.savaragerow.v1.query.AbstractUpdateQuery;
import nl.ssischaefer.savaragerow.v1.util.sql.SQLDDLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RenameColumnQuery extends AbstractUpdateQuery {
    private String table;
    private String from;
    private String to;

    public RenameColumnQuery setFrom(String from) {
        this.from = from;
        return this;
    }

    public RenameColumnQuery setTo(String to) {
        this.to = to;
        return this;
    }

    public RenameColumnQuery setTable(String table) {
        this.table = table;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        String sql = SQLDDLGenerator.generateRenameColumnQuery(table, from, to);
        return sqlConnection.prepareStatement(sql);
    }
}
