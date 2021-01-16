package com.example.renting.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    public DatabaseConfiguration() {

    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource(Environment environment) {
        return new HikariDataSource(config(environment));
    }

    private static HikariConfig config(Environment environment) {

        String databaseName = environment.getProperty("datasource.databaseName");
        String serverName = environment.getProperty("datasource.serverName");
        String url = environment.getProperty("datasource.url");
        boolean isUrlBasedConfig = url != null;
        String dataSourceClassName = environment.getProperty("datasource.dataSourceClassName");
        String driverClassName = environment.getProperty("datasource.driverClassName");
        String username = environment.getProperty("datasource.username");
        String password = environment.getProperty("datasource.password");
        Integer maximumPoolSize = (Integer)environment.getProperty("datasource.maximumPoolSize", Integer.class);
        HikariConfig config = new HikariConfig();
        if (isUrlBasedConfig) {
            config.setJdbcUrl(url);
            config.setDriverClassName(driverClassName);
            log.info("jdbcUrl for database: {}", url);
        } else {
            config.addDataSourceProperty("databaseName", databaseName);
            config.addDataSourceProperty("serverName", serverName);
            config.setDataSourceClassName(dataSourceClassName);
            log.info("Data source: {}:{}", serverName, databaseName);
        }

        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        if (maximumPoolSize != null) {
            config.setMaximumPoolSize(maximumPoolSize);
        }

        return config;
    }
}
