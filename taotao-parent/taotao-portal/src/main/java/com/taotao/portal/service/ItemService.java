package com.taotao.portal.service;

import com.taotao.portal.pojo.ItemInfo;

/**
 * 显示商品信息service
 * <p>Title: ItemService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午11:18:10
 * @version:1.0
 */
public interface ItemService {
	/**
	 * 根据商品id取商品基本信息
	 * <p>Title: getItemById</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TbItem
	 */
	ItemInfo getItemById(Long itemId);
	/**
	 * 根据商品id取商品详细信息
	 * <p>Title: getItemDescById</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: String
	 */
	String getItemDescById(Long itemId);
	/**
	 * 根据商品id显示商品规格参数
	 * <p>Title: getItemParam</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: String
	 */
	String getItemParam(Long itemId);
}
