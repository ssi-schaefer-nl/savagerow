package io.aero.v1.sqlite.preparedstatements;

import io.aero.v1.dto.AddColumnDTO;
import io.aero.v1.sqlite.utils.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PreparedAddColumnStatementBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private AddColumnDTO column;


    public Connection getConnection() {
        return connection;
    }

    public PreparedAddColumnStatementBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTable() {
        return table;
    }

    public PreparedAddColumnStatementBuilder setTable(String table) {
        this.table = table;
        return this;
    }


    public void execute() throws Exception {
        build();
        statement.executeUpdate();
    }

    private void build() throws Exception {
        String sql = createSqlString();
        statement = SQLiteDataSource.getConnection().prepareStatement(sql);

    }

    private String createSqlString() {
        //todo query is not protected against injection. PreparedStatements do not work for required places
        String sql = "ALTER TABLE " + table + " ADD COLUMN " + column.getColumn();
        if(!column.isNullable()) sql = sql + " NOT NULL DEFAULT [" + column.getDefaultValue() + "]";
        return sql;
    }

    public PreparedAddColumnStatementBuilder setColumn(AddColumnDTO column) {
        this.column = column;
        return this;
    }
}
