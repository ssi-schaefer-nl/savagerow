package io.aero.integration.sqlite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQLiteConfiguration {
    @Bean
    public JDBCMetaDataManager getJdbcMetadataManager() {
        return new JDBCMetaDataManager();
    }
}
