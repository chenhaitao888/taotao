package com.taotao.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.taotao.search.pojo.SearchResult;

/**
 * 搜索服务dao
 * <p>Title: SearchDao</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午4:38:29
 * @version:1.0
 */
public interface SearchDao {
	/**
	 * 根据query搜索
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param query
	 * @return: SearchResult
	 */
	SearchResult search(SolrQuery query);
}
