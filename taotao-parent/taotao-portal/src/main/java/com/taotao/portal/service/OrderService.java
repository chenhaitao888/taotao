package com.taotao.portal.service;

import com.taotao.portal.pojo.OrderPojo;

/**
 * 订单service
 * <p>Title: OrderService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日下午4:29:37
 * @version:1.0
 */
public interface OrderService {
	/**
	 * 创建订单
	 * <p>Title: orderCreate</p>
	 * <p>Description: </p>
	 * @param order
	 * @return: String
	 */
	String orderCreate(OrderPojo order);
}
