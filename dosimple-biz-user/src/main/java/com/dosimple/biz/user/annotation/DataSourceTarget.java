package com.dosimple.biz.user.annotation;

import com.dosimple.biz.user.enums.DataSourceEnum;

import java.lang.annotation.*;

/**
 * 切换数据注解 可以用于类或者方法级别 方法级别优先级 > 类级别
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceTarget {
    DataSourceEnum value() default DataSourceEnum.MASTER; //该值即key值
}
