package com.taotao.controller.work;

import java.util.concurrent.CountDownLatch;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.taotao.pojo.TbUser;
import com.taotao.service.DistributedLockService;

public class DistributedLockWork implements Runnable {
	
	private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
	private final DistributedLockService service;
	private RedissonClient redissonClient;
	
	public DistributedLockWork(CountDownLatch startSignal, CountDownLatch doneSignal, DistributedLockService service,
			RedissonClient redissonClient) {
		super();
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
		this.service = service;
		this.redissonClient = redissonClient;
	}


	@Override
	public void run() {
		try {
			startSignal.await();
			System.out.println(Thread.currentThread().getName() + " start");
			int count = service.distributedLockTest(new TbUser(), () -> business());
			System.out.println(Thread.currentThread().getName() + ":count" + count);
			doneSignal.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private int business(){
		RMap<String, Integer> map = redissonClient.getMap("distributedTest");
		Integer count = map.get("count");
		if(count > 0){
			count = count - 1;
			map.put("count", count);
		}
		return count;
	}

}
