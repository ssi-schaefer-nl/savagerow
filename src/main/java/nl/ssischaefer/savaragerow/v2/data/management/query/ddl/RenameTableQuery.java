package nl.ssischaefer.savaragerow.v2.data.management.query.ddl;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLDDLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RenameTableQuery extends AbstractDDLQuery {
    private String from;
    private String to;

    public RenameTableQuery setFrom(String from) {
        this.from = from;
        return this;
    }

    public RenameTableQuery setTo(String to) {
        this.to = to;
        return this;
    }

    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws SQLException {
        String sql = SQLDDLGenerator.generateRenameTableQuery(from, to);
        return sqlConnection.prepareStatement(sql);
    }
}
