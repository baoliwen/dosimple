package com.dosimple.biz.user.aspect;

import com.dosimple.biz.user.annotation.DataSourceTarget;
import com.dosimple.biz.user.enums.DataSourceEnum;
import com.dosimple.biz.user.util.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author dosimple
 */
@Aspect
@Component
@Slf4j
@Order(-100)
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void before(JoinPoint point, DataSourceTarget ds) throws Throwable {
        DataSourceEnum dsId = ds.value();
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId.getValue())) {
            log.info("Use DataSource :{} >", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId.getValue());
        } else {
            log.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId.getValue());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSourceTarget ds) {
        log.debug("Revert DataSource : " + ds.value().getValue() + " > " + point.getSignature());
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
    }
}
