package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 商品Controller
 * <p>Title:ItemController</p>
 * <P>Description:</P>
 * <p>Company:www.chenhaitao.com</p>
 * @author chenhaitao
 * @date:2017.02.17 15:08:32
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbUser;
import com.taotao.redisson.DistributedLockCallBack;
import com.taotao.redisson.SimpleDistributedLock;
import com.taotao.result.TaotaoResult;
import com.taotao.service.AopService;
import com.taotao.service.ItemService;
import com.taotao.utils.JsonUtils;
@Controller
public class AopTestController {
	@Autowired
	private AopService aopService;
	
	@Autowired
	private SimpleDistributedLock lock;
	@RequestMapping("/aop/test")
	@ResponseBody
	public String aopTest(){
		Map<String, Object> aopTest = aopService.aopTest();
		Object object = aopTest.get("data");
		String result = JsonUtils.objectToJson(object);
		return result;
	}
	
	@RequestMapping("/aop/redisson")
	@ResponseBody
	public void redissonTest(){
		lock.lock(new DistributedLockCallBack<String>() {

			@Override
			public String process() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String lockName() {
				// TODO Auto-generated method stub
				return null;
			}
		}, false);
	}
}
