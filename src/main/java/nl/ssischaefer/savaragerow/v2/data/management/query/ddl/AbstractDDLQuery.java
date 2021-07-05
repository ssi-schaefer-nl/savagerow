package nl.ssischaefer.savaragerow.v2.data.management.query.ddl;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDDLQuery {
    private Long generatedKey;
    private List<Map<String, String>> result;

    public AbstractDDLQuery executeUpdate() throws Exception {
        Connection sqlConnection = SQLiteDataSource.getForCurrentWorkspace();
        PreparedStatement preparedStatement = generate(sqlConnection);

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        if (generatedKeys.next()) generatedKey = generatedKeys.getLong(1);

        generatedKeys.close();
        preparedStatement.close();
        sqlConnection.close();


        return this;
    }

    public AbstractDDLQuery executeQuery() throws Exception {
        Connection sqlConnection = SQLiteDataSource.getForCurrentWorkspace();
        PreparedStatement preparedStatement = generate(sqlConnection);

        ResultSet rs = preparedStatement.executeQuery();
        retrieveResult(rs);

        rs.close();
        preparedStatement.close();
        sqlConnection.close();
        return this;
    }

    protected abstract PreparedStatement generate(Connection sqlConnection) throws Exception;

    public Long getGeneratedKey() {
        return generatedKey;
    }


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
}
