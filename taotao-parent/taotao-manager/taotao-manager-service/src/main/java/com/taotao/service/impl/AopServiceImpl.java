package com.taotao.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.taotao.annotation.MaskAnnotation;
import com.taotao.pojo.TbUser;
import com.taotao.service.AopService;
@Service
public class AopServiceImpl implements AopService{

	@Override
	@MaskAnnotation
	public Map<String, Object> aopTest() {
		TbUser u = new TbUser();
		u.setUsername("陈海涛");
		u.setPhone("17712922485");
		Map<String, Object> map = new HashMap<>();
		map.put("data", u);
		return map;
	}

}
