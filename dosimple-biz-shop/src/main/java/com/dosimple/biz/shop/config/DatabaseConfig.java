package com.dosimple.biz.shop.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author baolw
 */
public class DatabaseConfig {
    @Data
    @ConfigurationProperties(prefix = "sharding.jdbc.datasource.db0")
    @Component
    public static class Database0Config{
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String databaseName;

        public DruidDataSource createDataSource() {
            DruidDataSource result = new DruidDataSource();
            result.setDriverClassName(getDriverClassName());
            result.setUrl(getUrl());
            result.setUsername(getUsername());
            result.setPassword(getPassword());
            return result;
        }
    }

    @Data
    @ConfigurationProperties(prefix = "sharding.jdbc.datasource.db1")
    @Component
    public static class Database1Config{
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String databaseName;

        public DruidDataSource createDataSource() {
            DruidDataSource result = new DruidDataSource();
            result.setDriverClassName(getDriverClassName());
            result.setUrl(getUrl());
            result.setUsername(getUsername());
            result.setPassword(getPassword());
            return result;
        }
    }
}
