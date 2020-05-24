package com.kang.common.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
　 * <p>Title: RequestLimit</p> 
　 * <p>Description: 限流注解</p> 
　 * @author CK 
　 * @date 2020年4月26日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit_1 {
    /**
     * 允许访问的最大次数
     */
    int count() default Integer.MAX_VALUE;
 
    /**
     * 时间段，单位为毫秒，默认值一分钟
     */
    long time() default 60000;
}
