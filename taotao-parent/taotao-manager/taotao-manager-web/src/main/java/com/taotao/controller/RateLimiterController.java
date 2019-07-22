package com.taotao.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.util.concurrent.RateLimiter;
import com.taotao.service.RateLimiterService;

@Controller
public class RateLimiterController {
	@Autowired
	private RateLimiterService rateLimiterService;
	private static RateLimiter rateLimiter = RateLimiter.create(30.0);
	Semaphore se = new Semaphore(100);
	static volatile boolean stop = false;
	@RequestMapping("/semaphore/test")
	public void semaphoreTest() throws InterruptedException{
		CountDownLatch doneSignal = new CountDownLatch(100);
		CountDownLatch startSignal = new CountDownLatch(1);
		AtomicBoolean released = new AtomicBoolean(false);
		final AtomicLong okNum = new AtomicLong(0);
	    final AtomicLong blockNum = new AtomicLong(0);
	    final AtomicLong failNum = new AtomicLong(0);
		//se.release(50);
		long start = System.currentTimeMillis();
		for(int i = 0; i< 500; i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						startSignal.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					/*try {
						if(!se.tryAcquire(1, TimeUnit.MILLISECONDS)){
							failNum.incrementAndGet();
							return;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					if(!rateLimiter.tryAcquire(50, TimeUnit.MILLISECONDS)){
						blockNum.incrementAndGet();
						return;
					}
					//try {
						//se.acquire();
						//rateLimiterService.rateLimiterTest();
						okNum.incrementAndGet();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					//} 
					/*finally{
						//if (released.compareAndSet(false, true)) {
							System.out.println("释放释放");
			                se.release();
			                System.out.println(se.availablePermits());
			            //}
					}*/
				}
			}).start();
			doneSignal.countDown();
		}
		startSignal.countDown();
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println(System.currentTimeMillis() - start);
		Thread.sleep(5 * 1000L);
		System.out.println("ok=" + okNum.get());
		System.out.println("block=" + blockNum.get());
		System.out.println("fail=" + failNum.get());
	}
	
	@RequestMapping("/rateLimiter/test")
	public void rateLimiterTest() throws InterruptedException{
	}
	
	public static void main(String[] args) throws InterruptedException {
/*		Semaphore se = new Semaphore(10);
		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						if(!se.tryAcquire(1000, TimeUnit.MILLISECONDS)){
							System.out.println("失败");
							return;
						}
						System.out.println("成功");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						se.release();
					}
				}
			}).start();
		}
		System.out.println("jies");*/
		final RateLimiter rateLimiter = RateLimiter.create(10.0);
		 final AtomicLong okNum = new AtomicLong(0);
	     final AtomicLong blockNum = new AtomicLong(0);
		for(int i = 0; i<5; i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(!stop){
						if(!rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS)){
							blockNum.incrementAndGet();
						}else{
							okNum.incrementAndGet();
						}
					}
				}
			}){
				
			}.start();
			
		}
		 Thread.sleep(5 * 1000L);
		 stop = true;
		 System.out.println("ok=" + okNum.get());
	     System.out.println("block=" + blockNum.get());
	}
}
