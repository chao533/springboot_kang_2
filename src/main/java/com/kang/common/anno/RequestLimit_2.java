package com.kang.common.anno;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 限流
 * @author Administrator
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface RequestLimit_2 {
	/**
	 * 描述
	 */
	String description()  default "";

	/**
	 * key
	 */
	String key() default "";

	/**
	 * 类型
	 */
	LimitType limitType() default LimitType.CUSTOMER;

	enum LimitType {
		/**
		 * 自定义key
		 */
		CUSTOMER,
		/**
		 * 根据请求者IP
		 */
		IP
	}
}
