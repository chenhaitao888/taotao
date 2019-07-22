package com.taotao.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.annotation.RetryAnnotation;
import com.taotao.service.RetryService;
@Service
public class RetryServiceImpl implements RetryService{
	AtomicBoolean released = new AtomicBoolean(false);
	@Override
	@RetryAnnotation(retryAdapter="com.taotao.retry.MapRetryAdapter", retryCount = 3, timeUnit = TimeUnit.SECONDS, waitTime = 3)
	public Map<String, Object> retryTest() throws Exception{
		Map<String, Object> map = new HashMap<>();
		/*map.put("responseCode", "001");
		map.put("responseMsg", "系统异常");*/
		System.out.println("测试着玩玩>....<");
		int i = 1/0;
		return map;
	}
	@Override
	public void Test1() {
		if(released.compareAndSet(false, true)){
			System.out.println("test1");
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		released.compareAndSet(true, false);
	}
	@Override
	public void Test2() {
		if(released.compareAndSet(false, true)){
			System.out.println("test2..");
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		released.compareAndSet(true, false);
	}
	@Override
	public void Test3() {
		
	}
	
	
}
