package com.dosimple.task.config.elasticjob.annotation;

import com.dosimple.task.config.elasticjob.autoconfigure.JobParserAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ElasticJob 开启注解
 * 
 * <p>在Spring Boot 启动类上加此注解开启自动配置<p>
 * 
 * @author yinjihuan
 * 
 * @about http://cxytiandi.com/about
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JobParserAutoConfiguration.class})
public @interface EnableElasticJob {

}