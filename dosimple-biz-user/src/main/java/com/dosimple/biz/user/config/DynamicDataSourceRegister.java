package com.dosimple.biz.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dosimple.biz.user.util.DynamicDataSourceContextHolder;
import com.dosimple.common.util.GsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dosimple
 */

@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    /**
     * 配置上下文（也可以理解为配置文件的获取工具）
     */
    private Environment env;
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
    /**
     * 存储我们注册的数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();
    static {
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }
    /**
     * 参数绑定工具 springboot2.0新推出
     */
    private Binder binder;

    @Override
    public void setEnvironment(Environment environment) {
        log.info("开始注册数据源");
        this.env = environment;
        // 绑定配置器
        binder = Binder.get(env);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata
            , BeanDefinitionRegistry beanDefinitionRegistry) {
        // 获取所有数据源配置
        Map config, defauleDataSourceProperties;
        defauleDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        // 获取数据源类型
        String typeStr = env.getProperty("spring.datasource.master.type");
        // 获取数据源类型
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        // 绑定默认数据源参数 也就是主数据源
        DataSource consumerDatasource;
        DataSource defaultDatasource = bind(clazz, defauleDataSourceProperties);
        DynamicDataSourceContextHolder.dataSourceIds.add("master");
        log.info("注册默认数据源成功");
        // 获取其他数据源配置
        List<String> configs = binder.bind("spring.datasource.cluster", Bindable.listOf(String.class)).get();
        // 遍历从数据源
        for (int i = 0; i < configs.size(); i++) {
            config = GsonHelper.toMap(configs.get(i));
            clazz = getDataSourceType((String) config.get("type"));
            defauleDataSourceProperties = config;
            // 绑定参数
            consumerDatasource = bind(clazz, defauleDataSourceProperties);
            configDataSource(consumerDatasource);
            // 获取数据源的key，以便通过该key可以定位到数据源
            String key = config.get("key").toString();
            customDataSources.put(key, consumerDatasource);
            // 数据源上下文，用于管理数据源与记录已经注册的数据源key
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
            log.info("注册数据源{}成功", key);
        }
        // bean定义类
        GenericBeanDefinition define = new GenericBeanDefinition();
        // 设置bean的类型，此处DynamicRoutingDataSource是继承AbstractRoutingDataSource的实现类
        define.setBeanClass(DynamicRoutingDataSource.class);
        // 需要注入的参数
        MutablePropertyValues mpv = define.getPropertyValues();
        // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("defaultTargetDataSource", defaultDatasource);
        // 添加其他数据源
        mpv.add("targetDataSources", customDataSources);
        // 将该bean注册为datasource，不使用springboot自动生成的datasource
        beanDefinitionRegistry.registerBeanDefinition("datasource", define);
        log.info("注册数据源成功，一共注册{}个数据源", customDataSources.keySet().size() + 1);

    }
    private void configDataSource(DataSource dataSource) {
        if (dataSource instanceof DruidDataSource) {
            Integer maxActive= env.getProperty("spring.datasource.max-active", Integer.class);
            Integer minIdle = env.getProperty("spring.datasource.min-idle", Integer.class);
            Integer initialSize = env.getProperty("spring.datasource.initial-size", Integer.class);
            Long maxWait = env.getProperty("spring.datasource.max-wait", Long.class);
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            druidDataSource.setMaxActive(maxActive);
            druidDataSource.setInitialSize(initialSize);
            druidDataSource.setMaxWait(maxWait);
            druidDataSource.setMinIdle(minIdle);
            druidDataSource.setValidationQuery("select 1");
            druidDataSource.setTestWhileIdle(true);
            druidDataSource.setTestOnBorrow(false);
            druidDataSource.setTestOnReturn(false);
            try {
                druidDataSource.setFilters("stat,wall,slf4j");
            } catch (SQLException e) {
                log.error("druid configuration initialization filter", e);
            }
        }
    }
    /**
     * 通过字符串获取数据源class对象
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (StringUtils.hasLength(typeStr)) {
                // 字符串不为空则通过反射获取class对象
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                // 默认为druidDataSource数据源，与springboot默认数据源保持一致
                type = DruidDataSource.class;
            }
            return type;
        } catch (Exception e) {
            //无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
            throw new IllegalArgumentException("can not resolve class with type: " + typeStr);
        }
    }

    /**
     * 绑定参数，以下三个方法都是参考DataSourceBuilder的bind方法实现的
     * ，目的是尽量保证我们自己添加的数据源构造过程与springboot保持一致
     *
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(source.withAliases(aliases));
        // 将参数绑定到对象
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(source.withAliases(aliases));
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     * @param clazz
     * @param sourcePath 参数路径，对应配置文件中的值，如: spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }
}
