package nl.ssischaefer.savaragerow.v2.data.operations.query.dml;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class AbstractDMLQuery {
    private Long generatedKey;

    public AbstractDMLQuery execute() throws Exception {
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

    protected abstract PreparedStatement generate(Connection sqlConnection) throws Exception;

    public Long getGeneratedKey() {
        return generatedKey;
    }
}
