package com.taotao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {
	/**
	 * 如果lockName有值,则使用lockName作为锁名
	 * <p>Title: lockName</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String lockName() default "";
	/**
	 * 锁名的后缀
	 * <p>Title: lockSuffix</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String lockSuffix() default "lock";
	/**
	 * 拼接锁名分隔符
	 * <p>Title: separator</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String separator() default ".";
	/**
	 * 将方法参数列表的某个参数对象的某个属性值作为锁名
	 * 当param不为空时,可通过argNum参数来设置具体是参数列表的第几个参数，不设置则默认取第一个。
	 * <p>Title: param</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String param() default "";
	/**
	 * 将方法第几个参数作为锁
	 * <p>Title: argNum</p>
	 * <p>Description: </p>
	 * @return: int
	 */
	int argNum() default 0;
	/**
	 * 是否使用公平锁
	 * <p>Title: fairLock</p>
	 * <p>Description: </p>
	 * @return: boolean
	 */
	boolean fairLock() default false;
	/**
	 * 是否使用尝试锁
	 * <p>Title: tryLock</p>
	 * <p>Description: </p>
	 * @return: boolean
	 */
	boolean tryLock() default false;
	/**
	 * 最长等待时间
	 * <p>Title: waitTime</p>
	 * <p>Description: </p>
	 * @return: long
	 */
	long waitTime() default 30L;
	
	/**
	 * 锁超时时间
	 * <p>Title: leaseTime</p>
	 * <p>Description: </p>
	 * @return: long
	 */
	long leaseTime() default 5L;
	/**
	 * 时间单位
	 * <p>Title: timeUnit</p>
	 * <p>Description: </p>
	 * @return: TimeUnit
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
