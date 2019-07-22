package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.result.TaotaoResult;
import com.taotao.search.service.ItemSolrService;

/**
 * 将商品信息写入到solr服务器索引库中
 * <p>Title: ItemSolrController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午2:49:16
 * @version:1.0
 */
@Controller
@RequestMapping("/manager")
public class ItemSolrController {
	@Autowired
	private ItemSolrService itemSolrService;
	/**
	 * 将所有商品添加到索引库中
	 * <p>Title: importAllItems</p>
	 * <p>Description: </p>
	 * @return: TaotaoResult
	 */
	@RequestMapping("/importall")
	@ResponseBody
	public TaotaoResult importAllItems(){
		TaotaoResult result = itemSolrService.importAllItems();
		return result;
	}
	
	/**
	 * 添加新的商品到solr索引库中
	 * <p>Title: importNewItem</p>
	 * <p>Description: </p>
	 * @param item
	 * @param desc
	 * @return: TaotaoResult
	 */
	@RequestMapping(value="/importNew", method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public TaotaoResult importNewItem(String item,String desc){
		TaotaoResult result = itemSolrService.importNewItem(item,desc);
		return result;
	}
}
