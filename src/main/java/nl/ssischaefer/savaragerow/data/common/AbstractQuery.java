package nl.ssischaefer.savaragerow.data.common;

import nl.ssischaefer.savaragerow.WorkspaceConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractQuery {
    private List<Map<String, String>> result;
    private Connection sqlConnection;
    private PreparedStatement preparedStatement;
    private int affectedRows;
    private Long generatedKey;

    public AbstractQuery executeQuery() throws Exception {
        setup();
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
        retrieveResult(rs);

        rs.close();
        breakdown();
        return this;
    }

    public AbstractQuery executeUpdate() throws Exception {
        setup();
        System.out.println(preparedStatement);
        affectedRows = preparedStatement.executeUpdate();
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
        sqlConnection = DriverManager.getConnection(WorkspaceConfiguration.getDatabaseUrl());
        preparedStatement = generate(sqlConnection);
    }



    protected abstract PreparedStatement generate(Connection sqlConnection) throws Exception;

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

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }
}
