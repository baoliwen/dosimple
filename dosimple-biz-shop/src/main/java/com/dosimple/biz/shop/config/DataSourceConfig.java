package com.dosimple.biz.shop.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author baolw
 */
@Configuration
@Slf4j
public class DataSourceConfig {
    @Autowired
    private DatabaseConfig.Database0Config database0Config;
    @Autowired
    private DatabaseConfig.Database1Config database1Config;

    Map<String, DataSource> dataSourceMap = new HashMap<>(2);
    public DataSource getDataSource() throws SQLException {
        return buildDataSource();
    }
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.dosimple.biz.shop.entity");
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(getDataSource());
    }
    @Bean
    public  DataSource buildDataSource() throws SQLException {
        //添加两个数据库database0和database1
        if (dataSourceMap.size() == 0) {
            dataSourceMap.put(database0Config.getDatabaseName(), database0Config.createDataSource());
            dataSourceMap.put(database1Config.getDatabaseName(), database1Config.createDataSource());
            dataSourceMap.forEach((k,v) ->configDataSource((DruidDataSource)v));
        }
        //设置默认数据库
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, database0Config.getDatabaseName());

        //分表设置，大致思想就是将查询虚拟表Goods根据一定规则映射到真实表中去
        TableRule orderTableRule = TableRule.builder("t_order")
                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
                .dataSourceRule(dataSourceRule).build();

        //分库分表策略
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule))
                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
                .tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm())).build();
        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("db${0..1}.t_order_${0..1}");
        // 配置分库策略（Groovy表达式配置db规则）
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id", "db${user_id % 2}"));
        // 配置分表策略（Groovy表达式配置表路由规则）
        orderTableRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2}"));
        orderTableRuleConfig.setKeyGeneratorColumnName("id");
        orderTableRuleConfig.setKeyGeneratorClass("com.dangdang.ddframe.rdb.sharding.id.generator.self");
        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }

    private void configDataSource(DruidDataSource druidDataSource) {
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(1);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
        druidDataSource.setUseGlobalDataSourceStat(true);
        try {
            druidDataSource.setFilters("stat,wall,slf4j");
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
    }

    @Bean(value = "commonSelfIdGenerator")
    public CommonSelfIdGenerator keyGenerator() {
        return new CommonSelfIdGenerator();
    }
}
