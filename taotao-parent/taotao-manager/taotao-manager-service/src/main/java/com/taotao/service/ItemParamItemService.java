package com.taotao.service;
/**
 * 商品规格参数
 * <p>Title: ItemParamItemService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月24日下午4:40:46
 * @version:1.0
 */
public interface ItemParamItemService {
	/**
	 * 根据itemId取商品规格参数
	 * <p>Title: getItemParamByItemId</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: String
	 */
	String getItemParamByItemId(Long itemId);
}
