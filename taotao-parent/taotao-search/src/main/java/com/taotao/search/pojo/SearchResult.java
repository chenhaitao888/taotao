package com.taotao.search.pojo;

import java.util.List;

/**
 * 查询商品结果pojo
 * <p>Title: SearchResult</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午4:34:26
 * @version:1.0
 */
public class SearchResult {
	// 商品列表
	private List<ItemSolr> itemSolrList;
	// 总记录数
	private long recordCount;
	// 总页数
	private long pageCount;
	// 当前页
	private long curPage;
	
	public List<ItemSolr> getItemSolrList() {
		return itemSolrList;
	}
	public void setItemSolrList(List<ItemSolr> itemSolrList) {
		this.itemSolrList = itemSolrList;
	}
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getCurPage() {
		return curPage;
	}
	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}
	
}
