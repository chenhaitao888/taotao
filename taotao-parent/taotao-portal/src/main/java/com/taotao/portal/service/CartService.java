package com.taotao.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.portal.pojo.CartItem;
import com.taotao.result.TaotaoResult;

/**
 * 购物车service
 * <p>Title: CartService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月14日上午9:02:45
 * @version:1.0
 */
public interface CartService {
	/**
	 * 新增商品到购物车
	 * <p>Title: addCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param num
	 * @return: TaotaoResult
	 */
	TaotaoResult addCartItem(Long itemId, int num,
			HttpServletRequest request, HttpServletResponse response);
	/**
	 * 从cookie中取购物车商品信息
	 * <p>Title: getCartItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return: List<CartItem>
	 */
	List<CartItem> getCartItemList(HttpServletRequest request);
	/**
	 * 展示购物车列表
	 * <p>Title: getCartItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @param response
	 * @return: List<CartItem>
	 */
	List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 删除购物车商品
	 * <p>Title: deleteCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param request
	 * @param response
	 * @return: TaotaoResult
	 */
	TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response);
}
