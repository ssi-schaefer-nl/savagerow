package io.aero.integration.sqlite.configuration;

import io.aero.integration.sqlite.metadata.JDBCMetaDataManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQLiteConfiguration {
    @Bean
    public JDBCMetaDataManager getJdbcMetadataManager() {
        return new JDBCMetaDataManager();
    }

    @Bean(name = "cacheExpirationSeconds")
    public Long getCacheExpirationSeconds() { return 60L; }
}
