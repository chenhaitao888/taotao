package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.OrderPojo;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;

/**
 * 订单controller
 * <p>Title: OrderController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日下午2:56:12
 * @version:1.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	/**
	 * 把购物车商品展示到订单页面
	 * <p>Title: showOrderCart</p>
	 * <p>Description: </p>
	 * @param request
	 * @param response
	 * @param model
	 * @return: String
	 */
	@RequestMapping("/order-cart")
	public String showOrderCart(HttpServletRequest request, HttpServletResponse response,
			Model model){
		//从cookie中取去购物车商品列表
		List<CartItem> cartItemList = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", cartItemList);
		return "order-cart";
	}
	
	@RequestMapping("/create")
	public String orderCreate(OrderPojo order, Model model){
		String orderId = orderService.orderCreate(order);
		model.addAttribute("orderId", orderId);
		model.addAttribute("payment", order.getPayment());
		model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
		return "success";
	}
}
