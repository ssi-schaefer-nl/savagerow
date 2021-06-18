package nl.ssischaefer.savaragerow.v2.query;


import nl.ssischaefer.savaragerow.v2.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddColumnQuery {
    private PreparedStatement statement;
    private String table;
    private ColumnSchemaDTO column;

    public String getTable() {
        return table;
    }

    public AddColumnQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public ColumnSchemaDTO getColumn() {
        return column;
    }

    public AddColumnQuery setColumn(ColumnSchemaDTO column) {
        this.column = column;
        return this;
    }

    public AddColumnQuery generate() throws SQLException {
        String constraints = column.isNullable() ? "" : String.format("NOT NULL DEFAULT [%s]", column.getDefaultValue());
        String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s %s", table, column.getName(), column.getDatatype().datatype, constraints);
        statement = SQLiteDataSource.getConnection().prepareStatement(sql);
        return this;
    }

    public void execute() throws SQLException {
        statement.executeUpdate();
    }
}
