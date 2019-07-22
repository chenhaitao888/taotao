package com.taotao.redisson;

import java.util.concurrent.TimeUnit;

public interface DistributedFactory {
	/**
	 * 获取锁,默认锁超时时间
	 * <p>Title: lock</p>
	 * <p>Description: </p>
	 * @param callBack
	 * @param fairLock  是否公平锁
	 * @return: T
	 */
	<T> T lock(DistributedLockCallBack<T> callBack, boolean fairLock);
	/**
	 * 获取锁,使用自定义超时时间
	 * <p>Title: lock</p>
	 * <p>Description: </p>
	 * @param callBack
	 * @param releaseTime
	 * @param timeUnit
	 * @param fairLock 是否公平锁
	 * @return: T
	 */
	<T> T lock(DistributedLockCallBack<T> callBack, long releaseTime, TimeUnit timeUnit, boolean fairLock);
	/**
	 * 尝试获取分布式锁,默认锁超时时间,等待时间
	 * <p>Title: tryLock</p>
	 * <p>Description: </p>
	 * @param callBack
	 * @param fairLock 是否公平锁
	 * @return: T
	 */
	<T> T tryLock(DistributedLockCallBack<T> callBack, boolean fairLock);
	
	/**
	 * 尝试获取分布式锁,自定义锁超时时间,等待时间
	 * <p>Title: tryLock</p>
	 * <p>Description: </p>
	 * @param callBack
	 * @param releaseTime
	 * @param waitTime
	 * @param timeUnit
	 * @param fairLock 是否公平锁
	 * @return: T
	 */
	<T> T tryLock(DistributedLockCallBack<T> callBack, long releaseTime, long waitTime, TimeUnit timeUnit, boolean fairLock);
	
}
