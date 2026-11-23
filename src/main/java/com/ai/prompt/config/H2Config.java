package com.ai.prompt.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class H2Config {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties h2Properties() {
        return new DataSourceProperties();
    }

    @Bean("h2DataSource")
    @Primary
    public DataSource h2DataSource(
            @Qualifier("h2Properties")
            DataSourceProperties properties) {

        return properties.initializeDataSourceBuilder().build();
    }

    @Bean("jdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(
            @Qualifier("h2DataSource")
            DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }
}