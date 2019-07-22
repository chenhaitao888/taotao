package com.taotao.controller;

import java.util.concurrent.CountDownLatch;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.controller.work.DistributedLockWork;
import com.taotao.service.DistributedLockService;

@Controller
public class DistributedLockController {
	@Autowired
	private DistributedLockService service;
	@Autowired
	private RedissonClient redissonClient;
	private static final int count = 10;
	@RequestMapping("/distributedLock/test")
	public String distributedLockTest() throws Exception{
		RMap<String, Integer> map = redissonClient.getMap("distributedTest");
		map.put("count", 8);
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);
		
		for(int i = 0; i < count; i++){
			new Thread(new DistributedLockWork(startSignal, doneSignal, service, redissonClient)).start();
		}
		startSignal.countDown();
		doneSignal.await();
		return "finish";
	}
	
}
