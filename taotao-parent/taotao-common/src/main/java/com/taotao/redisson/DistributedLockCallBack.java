package com.taotao.redisson;

/**
 * 分布式锁回调接口
 * <p>Title: DistributedLoacCallBack</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年5月10日下午5:53:16
 * @version:1.0
 */
public interface DistributedLockCallBack<T> {
	/**
	 * 用来实现具体的业务逻辑
	 * <p>Title: process</p>
	 * <p>Description: </p>
	 * @return: T
	 */
	T process();
	
	/**
	 * 获取锁名称
	 * <p>Title: loackName</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String lockName();
}
