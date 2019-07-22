package com.taotao.rest.service;

import com.taotao.result.TaotaoResult;

/**
 * 商品信息管理
 * <p>Title: ItemService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午10:34:47
 * @version:1.0
 */
public interface ItemService {
	/**
	 * 根据商品id查询商品基本信息
	 * <p>Title: getItemBaseInfo</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	TaotaoResult getItemBaseInfo(long itemId);
	/**
	 * 根据商品id取商品描述
	 * <p>Title: getItemDesc</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	TaotaoResult getItemDesc(long itemId);
	/**
	 * 根据商品id取商品规格参数
	 * <p>Title: getItemParam</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	TaotaoResult getItemParam(long itemId);
}
