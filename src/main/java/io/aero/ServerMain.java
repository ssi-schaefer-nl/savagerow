package io.aero;

import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) throws SQLException {
        QueryService queryService = new JDBCQueryServiceImpl();
        Controller controller = new Controller(queryService);
        controller.setup();
    }
}
