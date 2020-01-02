package com.taotao.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class CacheSafeTest {

	public final static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
	public static volatile AtomicBoolean b = new AtomicBoolean(false);
	private static ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	public static void synchronizeHandle() throws InterruptedException {
		synchronized (map) {
			int value = map.get("key") + 1;
			map.put("key", value);
			Thread.sleep(5);
		}

	}

	public static void casHandle() throws InterruptedException {

		try {
			int value = map.get("key") + 1;
			map.put("key", value);
			//Thread.sleep(5);
			//System.out.println("加完了");
		} finally {
			b.compareAndSet(true, false);
		}

	}

	public static void loadHandle() throws InterruptedException{
		
		if (ClassUtils.isCacheSafe(CacheSafeTest.class, beanClassLoader)){
			int value = map.get("key") + 1;
			map.put("key", value);
			Thread.sleep(5);
		}
	}
	public static void main(String[] args) throws InterruptedException {

		ExecutorService exe = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(10);
		map.put("key", 1);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			exe.submit(new Runnable() {

				@Override
				public void run() {
					try {
						while(!b.compareAndSet(false, true)) {
							
						}
						casHandle();
						//synchronizeHandle();
						//loadHandle();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						latch.countDown();
					}

				}
			});
		}
		latch.await();
		System.out.println("耗时:" + (System.currentTimeMillis() - start));
		System.out.println(map.get("key"));
		exe.shutdown();
	}
}
