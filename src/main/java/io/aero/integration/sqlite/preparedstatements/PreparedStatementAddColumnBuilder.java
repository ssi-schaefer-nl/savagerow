package io.aero.integration.sqlite.preparedstatements;

import io.aero.dto.AddColumnDTO;
import io.aero.integration.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PreparedStatementAddColumnBuilder {
    private PreparedStatement statement;
    private Connection connection;
    private String table;
    private AddColumnDTO column;


    public Connection getConnection() {
        return connection;
    }

    public PreparedStatementAddColumnBuilder setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTable() {
        return table;
    }

    public PreparedStatementAddColumnBuilder setTable(String table) {
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
        if(!column.isNullable()) sql = sql + "NOT NULL DEFAULT [" + column.getDefaultValue() + "]";
        return sql;
    }

    public PreparedStatementAddColumnBuilder setColumn(AddColumnDTO column) {
        this.column = column;
        return this;
    }
}
