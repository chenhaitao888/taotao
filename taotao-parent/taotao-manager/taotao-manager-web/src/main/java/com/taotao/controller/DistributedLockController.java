package com.taotao.controller;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.controller.work.DistributedLockWork;
import com.taotao.service.DistributedLockService;
import com.taotao.utils.RedisClusterUtil;

import redis.clients.jedis.JedisCluster;

@Controller
public class DistributedLockController {
	@Autowired
	private DistributedLockService service;
	@Autowired
	private RedissonClient redissonClient;
	@Autowired
	private JedisCluster cluster;
	private static final int count = 100;
	private ExecutorService exce = Executors.newFixedThreadPool(20);
	@RequestMapping("/distributedLock/test")
	public String distributedLockTest() throws Exception{
		long start = System.currentTimeMillis();
		RMap<String, Integer> map = redissonClient.getMap("distributedTest");
		map.put("count", 100);
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);
		
		for(int i = 0; i < count; i++){
			exce.submit(new DistributedLockWork(startSignal, doneSignal, service, redissonClient));
		}
		startSignal.countDown();
		doneSignal.await();
		System.out.println(System.currentTimeMillis() - start);
		return "finish";
	}
	
	@RequestMapping("/distributedLock/test1")
	public String distributedLockTest1() throws Exception{
		long start = System.currentTimeMillis();
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);
		HashMap<String, Integer> map = new HashMap<>();
		map.put("count", 100);
		for(int i = 0; i < count; i++){
			String requestId = i + "";
			exce.submit(()->{
				try {
					startSignal.await();
					while(!RedisClusterUtil.tryGetDistributedLock(cluster, "distributedLockTest", requestId, 10)){
						
					}
					Integer count = map.get("count");
					if(count > 0){
						count = count - 1;
						map.put("count", count);
						System.out.println(count);
					}
					/*if (RedisClusterUtil.tryGetDistributedLock(cluster, "distributedLockTest", requestId, 10)) {
						Integer count = map.get("count");
						if(count > 0){
							count = count - 1;
							map.put("count", count);
							System.out.println(count);
						}
					} */
				} catch(Exception e){
					
				}finally {
					if(RedisClusterUtil.releaseDistributedLock(cluster, "distributedLockTest", requestId)){
					}
					doneSignal.countDown();
				}
			});
		}
		startSignal.countDown();
		doneSignal.await();
		System.out.println(System.currentTimeMillis() - start);
		return "finish";
		
	}
	/**
	 * easy
	 * <p>Title: distributedLockTest2</p>
	 * <p>Description: </p>
	 * @return
	 * @throws Exception: String
	 */
	@RequestMapping("/distributedLock/test2")
	public String distributedLockTest2() throws Exception{
		long start = System.currentTimeMillis();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("count", 100);
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);
		
		for(int i = 0; i < count; i++){
			exce.submit(()->{
				RLock lock = null;
				try {
					startSignal.await();
					lock = redissonClient.getLock("distributedLockTest");
					lock.lock(-1L, TimeUnit.SECONDS);
					Integer count = map.get("count");
					if(count > 0){
						count = count - 1;
						map.put("count", count);
						System.out.println(count);
					}
				} catch(Exception e){
					
				}finally {
					if (lock != null && lock.isLocked()) {
		                lock.unlock();
		            }
					doneSignal.countDown();
				}
			});
		}
		startSignal.countDown();
		doneSignal.await();
		System.out.println(System.currentTimeMillis() - start);
		return "finish";
	}
	
}
