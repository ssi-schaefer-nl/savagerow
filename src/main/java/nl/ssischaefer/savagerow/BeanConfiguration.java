package nl.ssischaefer.savagerow;

import nl.ssischaefer.savagerow.integration.sqlite.service.SQLiteDataDefinitionService;
import nl.ssischaefer.savagerow.integration.sqlite.service.SQLiteDataManipulationService;
import nl.ssischaefer.savagerow.integration.sqlite.service.SQLiteQueryService;
import nl.ssischaefer.savagerow.integration.sqlite.service.SQLiteWorkspaceService;
import nl.ssischaefer.savagerow.service.DataDefinitionService;
import nl.ssischaefer.savagerow.service.DataManipulationService;
import nl.ssischaefer.savagerow.service.QueryService;
import nl.ssischaefer.savagerow.service.WorkspaceService;
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
