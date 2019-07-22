package com.taotao.service;

import java.util.function.Supplier;

import com.taotao.pojo.TbUser;

/**
 * 测试分布式锁
 * <p>Title: DistributedLockService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年5月16日上午10:36:14
 * @version:1.0
 */
public interface DistributedLockService {
	int distributedLockTest(TbUser user, Supplier<Integer> supplier);
}
