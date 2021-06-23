package nl.ssischaefer.savaragerow.v2.query;

import nl.ssischaefer.savaragerow.v2.util.sql.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class AbstractUpdateQuery {
    private Long generatedKey;

    public AbstractUpdateQuery execute() throws Exception {
        Connection sqlConnection = SQLiteDataSource.get();
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
