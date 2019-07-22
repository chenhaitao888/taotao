package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.ItemSolr;
import com.taotao.search.pojo.SearchResult;

/**
 * 搜索服务dao
 * <p>Title: SearchDaoImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午4:41:27
 * @version:1.0
 */
@Repository
public class SearchDaoImpl implements SearchDao {
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据query搜索
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param query
	 * @return: SearchResult
	 */
	@Override
	public SearchResult search(SolrQuery query) {
		try {
			//返回值对象
			SearchResult searchResult = new SearchResult();
			//根据查询条件查询索引库
			QueryResponse response = solrServer.query(query);
			//取查询结果
			SolrDocumentList solrDocumentList = response.getResults();
			//取查询结果总数量
			searchResult.setRecordCount(solrDocumentList.getNumFound());
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			//商品列表
			List<ItemSolr> itemSolrList = new ArrayList<ItemSolr>();
			for (SolrDocument solrDocument : solrDocumentList) {
				//创建一商品对象
				ItemSolr item = new ItemSolr();
				item.setId((String) solrDocument.get("id"));
				item.setImage((String) solrDocument.get("item_image"));
				item.setPrice((long) solrDocument.get("item_price"));
				item.setSell_point((String) solrDocument.get("item_sell_point"));
				item.setCategory_name((String) solrDocument.get("item_category_name"));
				//取高亮显示的结果
				List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
				if(list != null && list.size() > 0){
					item.setTitle(list.get(0));
				}else{
					item.setTitle((String) solrDocument.get("item_title"));
				}
				//添加的商品列表
				itemSolrList.add(item);
				searchResult.setItemSolrList(itemSolrList);
				return searchResult;
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}

}
