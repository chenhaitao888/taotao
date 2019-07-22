package com.taotao.search.mapper;

import java.util.List;

import com.taotao.search.pojo.ItemSolr;


public interface ItemMapper {
	List<ItemSolr> getItemSolrList();
	/*根据商品cid取出商品分类名称*/
	String getCategoryName(Long cid);
}
