package com.taotao.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.taotao.exception.TaoTaoException;
import com.taotao.service.RetryService;
import com.taotao.service.impl.ThreadWork;
import com.taotao.utils.RedisClusterUtil;

import redis.clients.jedis.JedisCluster;

@Controller
public class RetryController {
	@Autowired
	private RetryService retryService;
	@Autowired
	private ThreadWork work;
	@Autowired
	private JedisCluster cluster;
	private static ExecutorService exe = Executors.newFixedThreadPool(10);
	@RequestMapping("/retry/test")
	public void retryTest(){
		ExecutorService exec = Executors.newFixedThreadPool(4);
		List<String> list = new ArrayList<>();
		try {
			exec.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						retryService.retryTest(list);
						System.out.println(list);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			exec.submit(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("ceshiia");
				}
			});
		}finally {
			System.out.println("done");
			exec.shutdown();
		}
		
	}
	
	
	@RequestMapping("/retry/test1")
	public void test1(){
		retryService.Test1();
	}
	
	@RequestMapping("/retry/test2")
	public void test2(){
		retryService.Test2();
	}
	
	@RequestMapping("/retry/test3")
	public void test3(){
		/*try{
			
		}finally{
			exe.shutdown();
		}*/
		exe.submit(() -> {
			try {
				int i = 1/ 0;
				System.out.println("hah");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@RequestMapping("/retry/test4")
	public void test4() {
		//retryService.test3();
		long start = System.currentTimeMillis();
		work.threadTest();
		System.out.println(System.currentTimeMillis() - start);
	}
	@RequestMapping("/retry/test5")
	public void test5(){
		retryService.test5();
	}
	@RequestMapping("/retry/test6")
	public void test6(){
		long start = System.currentTimeMillis();
		work.threadTest1();
		System.out.println("耗时: " + (System.currentTimeMillis() - start));
	}
	/**
	 * redis lpush 并发测试
	 * <p>Title: test7</p>
	 * <p>Description: </p>: void
	 */
	@RequestMapping("/retry/test7")
	public void test7(){
		CountDownLatch lacth = new CountDownLatch(1000);
		try {
			for(int i = 0; i< 1000; i++){
				int m = i;
				exe.submit(() -> {
					try {
						Long lpush = RedisClusterUtil.lpush(cluster, "list_test", m + "");
						System.out.println("结果: " + lpush);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}finally{
						lacth.countDown();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			lacth.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("ada");
	}
	
	
	@RequestMapping("/retry/test8")
	public void test8(){
		List<String> lrange = RedisClusterUtil.lrange(cluster, "list_test", 0, -1);
		Set<String> set = new HashSet<>();
		lrange.forEach( (e) -> set.add(e));
		System.out.println(set.size());
		set.forEach((e) -> System.out.println(e));
	}
	
	@RequestMapping("/retry/test9")
	public void test9(){
		int llen = RedisClusterUtil.llen(cluster, "list_test");
		Set<String> set = new HashSet<>();
		if(llen <=0){
			return;
		}
		if(llen % 2 == 0){
			llen = llen / 2;
		}else{
			llen = llen + 1;
		}
		CountDownLatch latch = new CountDownLatch(llen);
		for(int i = 0; i < llen; i++){
			int m = i;
			exe.submit(() -> {
				try {
					List<String> result = RedisClusterUtil.lrange(cluster, "list_test", m * 2 + m, m * 2 + m + 2);
					result.forEach((e) -> set.add(e));
				} finally {
					latch.countDown();
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println(set.size());
		set.forEach((e) -> System.out.println(e));
	}
	
	@RequestMapping("/retry/test10")
	public void test10(){
		RedisClusterUtil.delete("list_test", cluster);
		List<String> lrange = RedisClusterUtil.lrange(cluster, "list_test", 0, -1);
		System.out.println(lrange.size());
	}
	
	
	@RequestMapping("/retry/test11")
	public void test11(){
		work.threadTest2();
	}
	
}
