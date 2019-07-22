package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.aop.RetryAspect;
import com.taotao.factory.BeanFactory;
@Controller
public class DynamticController {
	@RequestMapping("/dynamic/test")
	public void testDy(){
		RetryAspect bean = BeanFactory.getBean(RetryAspect.class);
		bean.setA(12L);
	}
}
