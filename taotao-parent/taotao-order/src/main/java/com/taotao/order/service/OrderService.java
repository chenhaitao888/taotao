package com.taotao.order.service;

import java.util.List;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.result.TaotaoResult;

/**
 * 订单service
 * <p>Title: OrderService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日上午10:06:20
 * @version:1.0
 */
public interface OrderService {
	/**
	 * 创建订单
	 * <p>Title: orderCreate</p>
	 * <p>Description: </p>
	 * @param order
	 * @param orderItems
	 * @param orderShipping
	 * @return: TaotaoResult
	 */
	TaotaoResult orderCreate(TbOrder order, List<TbOrderItem> orderItems,
			TbOrderShipping orderShipping);
}
