package io.aero;

import io.aero.integration.sqlite.service.SQLiteDataDefinitionService;
import io.aero.integration.sqlite.service.SQLiteDataManipulationService;
import io.aero.integration.sqlite.service.SQLiteQueryService;
import io.aero.integration.sqlite.service.SQLiteWorkspaceService;
import io.aero.service.DataDefinitionService;
import io.aero.service.DataManipulationService;
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
    public DataDefinitionService getDataDefinitionService() throws SQLException {
        return new SQLiteDataDefinitionService();
    }

    @Bean
    @Scope("singleton")
    public DataManipulationService getDataManipulationService() throws SQLException {
        return new SQLiteDataManipulationService();
    }


    @Bean
    @Scope("singleton")
    public QueryService getQueryService() throws SQLException {
            return new SQLiteQueryService();
    }

    @Bean
    @Scope("singleton")
    public WorkspaceService getWorkspaceService() throws Exception {
        return new SQLiteWorkspaceService();
    }
}
