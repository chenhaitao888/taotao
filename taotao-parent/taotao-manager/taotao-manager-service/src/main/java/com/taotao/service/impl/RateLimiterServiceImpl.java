package com.taotao.service.impl;

import org.springframework.stereotype.Service;

import com.taotao.service.RateLimiterService;
@Service
public class RateLimiterServiceImpl implements RateLimiterService {

	@Override
	public void rateLimiterTest() {
		System.out.println("成功");
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
