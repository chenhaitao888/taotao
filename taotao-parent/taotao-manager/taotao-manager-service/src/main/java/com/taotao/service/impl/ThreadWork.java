package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbUserMapper;
import com.taotao.service.RetryService;

@Component
public class ThreadWork {
	@Autowired
	private RetryService retryService;
	private static final ExecutorService exe = Executors.newFixedThreadPool(3);
	@Autowired
	private TbUserMapper user;
	public void threadTest(){
		CountDownLatch latch = new CountDownLatch(3);
		int qryCount = user.qryCount();
		int[] evNum = new int[3];
		if((qryCount / 3) % 2 == 0){
			evNum[0] = qryCount / 3;
			evNum[1] = qryCount / 3;
		}else{
			evNum[0] = qryCount / 3 - (qryCount / 3) % 2;
			evNum[1] = qryCount / 3 - (qryCount / 3) % 2;
		}
		
		if((qryCount % 3) == 0){
			evNum[2] = qryCount / 3 + 2 * ((qryCount / 3) % 2);
		}else{
			evNum[2] = qryCount / 3 + qryCount % 3 + 2 * ((qryCount / 3) % 2);
		}
		int[] qcount = new int[3];
		for(int i = 0; i <= 2; i++){
			if((evNum[i] % 2) == 0){
				qcount[i] = evNum[i] / 2;
			}else{
				qcount[i] = evNum[i] / 2 + 1;
			}
		}
		for(int i = 0; i <= 2; i++){
			int j = i;
			int begin;
			if(i < 2){
				begin = qcount[j] * j + 1;
			}else{
				begin = qcount[j - 1] * j + 1;
			}
			System.out.println("查询量: " + evNum[j] + "---" + "查询次数: " + qcount[j] + "起始页: " + "---" + begin);
			exe.submit(() -> {
				try {
					retryService.test3(begin, qcount[j], evNum[j]);
				} catch (Exception e){
					e.printStackTrace();
				}finally {
					latch.countDown();
				}
			});
			
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("latch值: " + latch.getCount());
	}

	
	public void threadTest1(){
		CountDownLatch latch = new CountDownLatch(3);
		int qryCount = user.qryCount();
		if(qryCount <=0){
			return;
		}
		if(qryCount % 2 == 0){
			qryCount = qryCount / 2;
		}else{
			qryCount = qryCount + 1;
		}
		for(int i = 1; i <= qryCount; i++){
			int page = i;
			exe.submit(() -> {
				try {
					PageHelper.startPage(page, 2);
					retryService.test6(page);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					PageHelper.clearPage();
					latch.countDown();
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void threadTest2() {
		int qryCount = user.qryCount();
		if(qryCount % 2 == 0){
			qryCount = qryCount / 2;
		}else{
			qryCount = qryCount / 2 + 1;
		}
		 
	}
	
	public static int indexFor(int p, int length){
		return p & (length - 1);
	}
	
	public static <K, V> void main(String[] args) {
		int size = 4;
		Map<Integer, List> map = new HashMap<>();
		for(int i = 0; i< size;i++){
			List<Integer> name = new ArrayList<>();
			map.put(i, name);
		}
		int count = 2239730;
		int page = 100000;
		int newpage = 0;
		if(count % page == 0){
			newpage = count / page;
		}else{
			newpage = count / page + 1;
		}
		for(int i = 1; i <= newpage; i++){
			int index = indexFor(i, size);
			List list = map.get(index);
			list.add(i);
		}
		
		for(Map.Entry entry: map.entrySet()){
			List list = (List) entry.getValue();
			System.out.print(entry.getKey() + ":");
			list.forEach((e) -> {
				System.out.print(e + "--");});
			System.out.println("");
			
		}
	}
	static class Node<K,V> {
		private K key;
		private V v;
		 Node(K key, V value) {
	            this.key = key;
	            this.v = value;
	        }
	}
}
