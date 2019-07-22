package com.taotao.rest.service;

import com.taotao.rest.pojo.CatResult;

/**
 * 前台商品分类服务
 * <p>Title: ItemCatService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月26日下午9:57:06
 * @version:1.0
 */
public interface ItemCatService {
	/**
	 * 取商品分类
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @return: CatResult
	 */
	CatResult getItemCatList();
}
