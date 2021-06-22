package nl.ssischaefer.savaragerow.v2.query;

import nl.ssischaefer.savaragerow.v2.dto.RowDTO;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateRowQuery {
    private String table;
    private RowDTO row;
    private long rowId;
    private PreparedStatement preparedStatement;

    public UpdateRowQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public UpdateRowQuery setRow(RowDTO row) {
        this.row = row;
        return this;
    }


    public UpdateRowQuery setRowId(long rowId) {
        this.rowId = rowId;
        return this;
    }


    public UpdateRowQuery generate() throws SQLException {
        List<String> columns = new ArrayList<>(this.row.getRow().keySet());
        String sql = String.format("UPDATE %s SET %s WHERE rowid = %s", this.table, columns.stream().map(col -> String.format("%s = ?", col)).collect(Collectors.joining(", ")), this.rowId);
        this.preparedStatement = SQLiteDataSource.get().prepareStatement(sql);
        for (int i = 0; i < columns.size(); i++) {
            preparedStatement.setString(i + 1, row.getRow().get(columns.get(i)));
        }
        return this;
    }

    public void execute() throws SQLException {
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
