package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.exception.TaoTaoException;
import com.taotao.service.RetryService;

@Controller
public class RetryController {
	@Autowired
	private RetryService retryService;
	
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
			System.out.println("hah");
		});
	}
	
	@RequestMapping("/retry/test4")
	public void test4() {
		retryService.Test3();
	}
	@RequestMapping("/retry/test5")
	public void test5(){
		retryService.test5();
	}
}
