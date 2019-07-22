package com.taotao.portal.service;

import com.taotao.portal.pojo.SearchResult;

/**
 * 门户搜索服务
 * <p>Title: SearchService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午9:55:16
 * @version:1.0
 */
public interface SearchService {
	/**
	 * 根据查询条件和当前页进行查询
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param queryString
	 * @param page
	 * @return: SearchResult
	 */
	SearchResult search(String queryString, int page);
}
