package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.portal.pojo.OrderPojo;
import com.taotao.portal.service.OrderService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

/**
 * 订单service
 * <p>Title: OrderService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日下午4:29:37
 * @version:1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	/**
	 * 创建订单
	 * <p>Title: orderCreate</p>
	 * <p>Description: </p>
	 * @param order
	 * @return: String
	 */
	@Override
	public String orderCreate(OrderPojo order) {
		//调用order系统的创建订单接口
		String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
		TaotaoResult result = TaotaoResult.formatToPojo(json,String.class);
		if(result.getStatus() == 200){
			return (String) result.getData();
		}
		return "";
	}

}
