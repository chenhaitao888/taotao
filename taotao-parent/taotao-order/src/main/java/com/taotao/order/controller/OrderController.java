package com.taotao.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.order.pojo.OrderPojo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.ExceptionUtil;

/**
 * 订单Controller
 * <p>Title: OrderController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日下午2:25:18
 * @version:1.0
 */
@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	/**
	 * 订单创建
	 * <p>Title: orderCreate</p>
	 * <p>Description: </p>
	 * @param orderPojo
	 * @return: TaotaoResult
	 */
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult orderCreate(@RequestBody OrderPojo order){
		try {
			List<TbOrderItem> orderItems = order.getOrderItems();
			TbOrderShipping orderShipping = order.getOrderShipping();
			TaotaoResult result = orderService.orderCreate(order, orderItems, orderShipping);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
}
