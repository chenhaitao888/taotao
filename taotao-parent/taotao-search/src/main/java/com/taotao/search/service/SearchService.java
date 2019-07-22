package com.taotao.search.service;

import com.taotao.search.pojo.SearchResult;

/**
 * 商品搜索服务service
 * <p>Title: SearchService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午5:16:31
 * @version:1.0
 */
public interface SearchService {
	/**
	 * 根据查询条件,已经分页条件查询
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param queryString
	 * @param page
	 * @param rows
	 * @return: SearchResult
	 */
	SearchResult search(String queryString, int page, int rows);
}
