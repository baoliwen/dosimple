package com.dosimple.task.config.elasticjob.dynamic.bean;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @date: 2019/4/11 0011 下午 3:44
 * @author dosimple
 */
@Configuration
public class JobDataSourceConfig {
    /**
     * 任务执行事件数据源
     * @return
     */
    @Bean("jobDataSource")
    @ConfigurationProperties("spring.datasource.druid.log")
    public DataSource dataSourceTwo(){
        return DruidDataSourceBuilder.create().build();
    }
}
