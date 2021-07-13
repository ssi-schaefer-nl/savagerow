package nl.ssischaefer.savaragerow.data.common;

import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractQuery {
    private List<Map<String, String>> result;
    private Connection sqlConnection;
    private PreparedStatement preparedStatement;
    private Long generatedKey;

    public AbstractQuery executeQuery() throws Exception {
        setup();

        ResultSet rs = preparedStatement.executeQuery();
        retrieveResult(rs);

        rs.close();
        breakdown();
        return this;
    }

    public AbstractQuery executeUpdate() throws Exception {
        setup();

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        if (generatedKeys.next()) generatedKey = generatedKeys.getLong(1);

        generatedKeys.close();

        breakdown();
        return this;
    }

    private void breakdown() throws SQLException {
        preparedStatement.close();
        sqlConnection.close();
    }

    private void setup() throws Exception {
        sqlConnection = SQLiteDataSource.getForCurrentWorkspace();
        preparedStatement = generate(sqlConnection);
    }



    protected abstract PreparedStatement generate(Connection sqlConnection) throws SQLException, Exception;

    private void retrieveResult(ResultSet rs) throws SQLException {
        List<Map<String, String>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> cols = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                cols.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            rows.add(cols);
        }
        this.result = rows;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }


    public Long getGeneratedKey() {
        return generatedKey;
    }
}
