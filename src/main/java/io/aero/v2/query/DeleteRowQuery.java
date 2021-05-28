package io.aero.v2.query;


import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteRowQuery {
    private String table;
    private long row;
    private String sql;
    private PreparedStatement preparedStatement;

    public DeleteRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowQuery setRow(long row) {
        this.row = row;
        return this;
    }

    public DeleteRowQuery generate() throws SQLException {
        sql = String.format("DELETE FROM %s WHERE rowid = %s", this.table, this.row);
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);
        return this;
    }

    public void execute() throws SQLException {
        preparedStatement.executeUpdate();
    }
}
