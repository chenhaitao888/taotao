package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

/**
 * 购物车service
 * <p>Title: CartService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月14日上午9:02:45
 * @version:1.0
 */
@Service
public class CartServiceImpl implements CartService {
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	/**
	 * 新增商品到购物车
	 * <p>Title: addCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param num
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult addCartItem(Long itemId, int num,
			HttpServletRequest request, HttpServletResponse response) {
		CartItem cartItem = null;
		List<CartItem> cartItemList = null;
		//从cookie中取购物车商品
		cartItemList = getCartItemList(request);
		if(cartItemList != null && cartItemList.size() > 0){
			//判断cookie中是否有此商品,如果有的话,数量加一
			for (CartItem item : cartItemList) {
				if(item.getId() == itemId){
					item.setNum(item.getNum() + num);
					cartItem = item;
					break;
				}
			}
		}
		//cartItem为空,说明购物车无此商品
		if(cartItem == null){
			cartItemList = new ArrayList<CartItem>();
			cartItem = new CartItem();
			//取商品信息
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItem.class);
			if(taotaoResult.getStatus() == 200){
				TbItem item = (TbItem) taotaoResult.getData();
				//将商品信息赋值给购物车商品
				cartItem.setId(item.getId());
				cartItem.setNum(num);
				cartItem.setPrice(item.getPrice());
				cartItem.setTitle(item.getTitle());
				cartItem.setImage(item.getImage() == null ?
						"":item.getImage().split(",")[0]);
				//把商品添加到购物车
				cartItemList.add(cartItem);
			}
		}
		//把购物车商品列表写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", 
				JsonUtils.objectToJson(cartItemList), true);
		return TaotaoResult.ok();
	}
	
	/**
	 * 从cookie中取购物车商品信息
	 * <p>Title: getCartItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return: List<CartItem>
	 */
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request) {
		//从cookie中取商品列表
		String json = CookieUtils.getCookieValue(request, "TT_CART", true);
		List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
		return list;
	}

	/**
	 * 展示购物车列表
	 * <p>Title: getCartItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @param response
	 * @return: List<CartItem>
	 */
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		List<CartItem> list = getCartItemList(request);
		return list;
	}
	
	/**
	 * 删除购物车商品
	 * <p>Title: deleteCartItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param request
	 * @param response
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		// 从列表中找到此商品
		for (CartItem cartItem : itemList) {
			if (cartItem.getId() == itemId) {
				itemList.remove(cartItem);
				break;
			}
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);
		return TaotaoResult.ok();
	}
}
