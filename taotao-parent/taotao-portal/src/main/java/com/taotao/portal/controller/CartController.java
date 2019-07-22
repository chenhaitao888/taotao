package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.result.TaotaoResult;

/**
 * 购物车controller
 * <p>Title: CartController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月14日下午7:17:41
 * @version:1.0
 */
@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	/**
	 * 添加商品到购物车
	 * <p>Title: addCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return: String
	 */
	@RequestMapping("/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId,
			@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request, HttpServletResponse response){
		TaotaoResult result = cartService.addCartItem(itemId, num, request, response);
		if(result.getStatus() == 200){
			//解决刷新添加到购物车成功页面,自动添加商品数量到购物车的bug,添加redirect
			return "redirect:/cart/success.html";
		}
		return "redirect:/cart/fail.html";
	}
	@RequestMapping("/success")
	public String success(){
		return "cartSuccess";
	}
	@RequestMapping("/fail")
	public String fail(){
		return "cartFail";
	}
	@RequestMapping("/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response, Model model){
		List<CartItem> cartItemList = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", cartItemList);
		return "cart";
	}
	/**
	 * 删除购物车商品
	 * <p>Title: deleteCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param request
	 * @param response
	 * @return: String
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		cartService.deleteCartItem(itemId, request, response);
		//跳转到购物车页面
		return "redirect:/cart/cart.html";
	}
}
