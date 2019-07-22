package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 商品Controller
 * <p>Title:ItemController</p>
 * <P>Description:</P>
 * <p>Company:www.chenhaitao.com</p>
 * @author chenhaitao
 * @date:2017.02.17 15:08:32
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ItemService;
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	/**
	 * 商品查询
	 * <p>Title: getItemList</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EUDataGridResult getItemList(int page,int rows){
		EUDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	/**
	 * 商品添加controller
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param item
	 * @param desc
	 * @return: TaotaoResult
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem item,String desc,String itemParams){
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setParamData(itemParams);
		TaotaoResult taotaoResult = itemService.addItem(item, itemDesc,tbItemParamItem);
		return taotaoResult;
		
	}
}
