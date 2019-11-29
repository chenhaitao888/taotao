package com.taotao.service.impl;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.taotao.annotation.DistributedLock;
import com.taotao.pojo.TbUser;
import com.taotao.service.DistributedLockService;
@Service
public class DistributedLockServiceImpl implements DistributedLockService {
	@Override
	@DistributedLock(param = "id", lockSuffix = "lock", leaseTime = -1L)
	public int distributedLockTest(TbUser user, Supplier<Integer> supplier) {
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		return supplier.get();
	}
	
	public static void main(String[] args) {
		DistributedLockServiceImpl s1 = new DistributedLockServiceImpl();
		TbUser user = new TbUser();
		int distributedLockTest = s1.distributedLockTest(user, () -> {
			return 1;
		});
		System.out.println(distributedLockTest);
		
	}

}
