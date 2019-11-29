package com.taotao.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.annotation.RetryAnnotation;
import com.taotao.exception.TaoTaoException;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.service.RetryService;
@Service
public class RetryServiceImpl implements RetryService{
	AtomicBoolean released = new AtomicBoolean(false);
	private static final ThreadLocal<Integer> loacl = new ThreadLocal<>();
	private String mm = new String("dd");
	@Autowired
	private RetryService retryService;
	@Autowired
	private TbUserMapper user;
	@Override
	@RetryAnnotation(retryAdapter="com.taotao.retry.MapRetryAdapter", retryCount = 3, timeUnit = TimeUnit.SECONDS, waitTime = 3)
	public Map<String, Object> retryTest(List<String> list) throws Exception{
		Map<String, Object> map = new HashMap<>();
		if(loacl.get() == null){
			loacl.set(0);
		}
		Integer integer = loacl.get();
		/*map.put("responseCode", "001");
		map.put("responseMsg", "系统异常");*/
		System.out.println(Thread.currentThread().getName() + "--" + integer);
		if(integer == 3){
			loacl.remove();
			
		}else{
			map.put("responseCode", "001");
			map.put("responseMsg", "系统异常");
			integer = integer + 1;
			loacl.set(integer);
		}
		list.add(integer + "");
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
			mm = "aa";
			System.out.println(mm);
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
		((RetryServiceImpl)AopContext.currentProxy()).testRe();
	}
	@RetryAnnotation(retryCount = 3)
	public Boolean testRe() {
		try {
			if(loacl.get() == null){
				loacl.set(1);
			}
			Integer i = loacl.get();
			i = i + 1;
			loacl.set(i);
			System.out.println(loacl.get());
			int j = i / (4-i);
			System.out.println(mm);
			return false;
		} catch(Exception e){
			loacl.remove();
			return true;
		}
	}
	@Override
	public void test5() {
		TbUser usrs = user.selectByPrimaryKey(22L);
		PageHelper.startPage(2, 4);
		//List<TbUser> usrs = user.qryUsr();
		System.out.println(usrs.getUsername());
		int i = 1/0;
	}
}
