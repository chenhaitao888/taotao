package com.taotao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryAnnotation {
	/**
	 * 时间单位
	 * <p>Title: timeUnit</p>
	 * <p>Description: </p>
	 * @return: TimeUnit
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;
	/**
	 * 重试次数
	 * <p>Title: retryCount</p>
	 * <p>Description: </p>
	 * @return: int
	 */
	int retryCount() default 0;
	/**
	 * 重试间隔
	 * <p>Title: waitTime</p>
	 * <p>Description: </p>
	 * @return: long
	 */
	long waitTime() default 0;
	
	/**
	 * 重试实现者
	 * <p>Title: retryAdapter</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String retryAdapter() default "com.taotao.retry.DefaultRetryAdapter";
	boolean result() default false;
	Class<?> clazz() default Boolean.class;
	String param() default "";
	int argNum() default 0;
}
