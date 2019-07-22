package com.taotao.service;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.result.TaotaoResult;

public interface ItemService {
	/**
	 * 通过商品id查询商品
	 * <p>Title: getItemById</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TbItem
	 */
	TbItem getItemById(long itemId);
	/**
	 * 商品列表分页查询
	 * <p>Title: getItemList</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	EUDataGridResult getItemList(int page, int rows);
	/**
	 * 商品添加
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param tbItem
	 * @return: TaotaoResult
	 */
	TaotaoResult addItem(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem);
}
