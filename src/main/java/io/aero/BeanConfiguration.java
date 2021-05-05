package io.aero;

import io.aero.integration.sqlite.JDBCQueryService;
import io.aero.integration.sqlite.SQLiteWorkspaceService;
import io.aero.service.QueryService;
import io.aero.service.WorkspaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.sql.SQLException;

@Configuration
public class BeanConfiguration {

    @Bean
    @Scope("singleton")
    public QueryService getQueryService() throws SQLException {
            return new JDBCQueryService();
    }

    @Bean
    @Scope("singleton")
    public WorkspaceService getWorkspaceService() throws Exception {
        return new SQLiteWorkspaceService();
    }
}
