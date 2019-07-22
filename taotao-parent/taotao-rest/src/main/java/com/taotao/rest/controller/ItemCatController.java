package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import com.taotao.utils.JsonUtils;

/**
 * 前台商品分类Controller
 * <p>
 * Title: ItemCatController
 * </p>
 * <p>
 * @Description:TODO
 * </p>
 * <p>
 * Company: www.chenhaitao.com
 * </p>
 * 
 * @author chenhaitao
 * @date:2017年2月26日下午10:17:49
 * @version:1.0
 */
@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 返回符合前台商品分类的json格式串(方法一)
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @param callback
	 * @return: String
	 */
//	@RequestMapping(value="/itemcat/list", 
//			produces=MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
//	@ResponseBody
//	public String getItemCatList(String callback) {
//		CatResult catResult = itemCatService.getItemCatList();
//		//把pojo转换成字符串
//		String json = JsonUtils.objectToJson(catResult);
//		//拼装返回值
//		String result = callback + "(" + json + ");";
//		return result;
//	}
	/**
	 *  返回符合前台商品分类的json格式串(方法二)
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @param callback
	 * @return: String
	 */
	@RequestMapping("/itemcat/list")
	@ResponseBody
	public Object getItemCatList(String callback){
		CatResult catResult = itemCatService.getItemCatList();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catResult);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
}
