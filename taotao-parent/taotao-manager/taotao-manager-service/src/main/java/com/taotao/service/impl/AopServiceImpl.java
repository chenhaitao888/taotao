package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.taotao.annotation.MaskAnnotation;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbUser;
import com.taotao.service.AopService;
@Service
public class AopServiceImpl implements AopService{

	@Override
	@MaskAnnotation
	public Map<String, Object> aopTest() {
		TbUser u = new TbUser();
		TbUser u1 = new TbUser();
		TbUser u2 = new TbUser();
		TbOrder o = new TbOrder();
		List<TbOrder> list = new ArrayList<>();
		List<TbUser> users = new ArrayList<>();
		u.setUsername("陈海涛");
		u.setPhone("17712922485");
		u1.setPhone("13333333333");
		u2.setPhone("14444444444");
		users.add(u1);
		users.add(u2);
		o.setUsers(users);
		o.setOrderId("adadaad");
		o.setPayment("woshisds");
		list.add(o);
		u.setOrder(o);
		u.setList(list);
		Map<String, Object> map = new HashMap<>();
		map.put("data", u);
		return map;
	}

}
