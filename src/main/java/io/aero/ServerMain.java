package io.aero;

import io.aero.integration.jdbc.JDBCQueryService;

import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) throws SQLException {
        QueryService queryService = new JDBCQueryService();
        Controller controller = new Controller(queryService);
        controller.setup();
    }
}
