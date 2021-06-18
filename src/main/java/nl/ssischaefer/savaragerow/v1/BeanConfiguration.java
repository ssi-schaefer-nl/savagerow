package nl.ssischaefer.savaragerow.v1;

import nl.ssischaefer.savaragerow.v1.sqlite.service.SQLiteDataDefinitionService;
import nl.ssischaefer.savaragerow.v1.sqlite.service.SQLiteDataManipulationService;
import nl.ssischaefer.savaragerow.v1.sqlite.service.SQLiteQueryService;
import nl.ssischaefer.savaragerow.v1.sqlite.service.SQLiteWorkspaceService;
import nl.ssischaefer.savaragerow.v1.service.DataDefinitionService;
import nl.ssischaefer.savaragerow.v1.service.DataManipulationService;
import nl.ssischaefer.savaragerow.v1.service.QueryService;
import nl.ssischaefer.savaragerow.v1.service.WorkspaceService;
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
