package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TbItem;
import com.taotao.result.TaotaoResult;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.pojo.ItemSolr;
import com.taotao.search.service.ItemSolrService;
import com.taotao.utils.ExceptionUtil;
import com.taotao.utils.JsonUtils;

/**
 * 用于将数据库商品数据存在solr域中
 * <p>Title: ItemSolrService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月4日下午2:18:34
 * @version:1.0
 */
@Service
public class ItemSolrServiceImpl implements ItemSolrService {
	@Autowired
	private SolrServer solrServer;

	@Autowired
	private ItemMapper itemMapper;
	/**
	 * 导入所有产品信息到solr索引库
	 * <p>Title: importAllItems</p>
	 * <p>Description: </p>
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult importAllItems() {
		try {
			// 查询商品列表
			List<ItemSolr> itemSolrList = itemMapper.getItemSolrList();
			// 把商品信息写入索引库
			for (ItemSolr itemSolr : itemSolrList) {
				// 创建一个SolrInputDocument对象
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", itemSolr.getId());
				document.setField("item_title", itemSolr.getTitle());
				document.setField("item_sell_point", itemSolr.getSell_point());
				document.setField("item_price", itemSolr.getPrice());
				document.setField("item_image", itemSolr.getImage());
				document.setField("item_category_name", itemSolr.getCategory_name());
				document.setField("item_desc", itemSolr.getItem_des());
				// 写入索引库
				solrServer.add(document);
			}
			//提交修改
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		} 
		return TaotaoResult.ok();
	}
	
	/**
	 * 将后台平台新增的商品添加的solr索引库中
	 * <p>Title: importNewItem</p>
	 * <p>Description: </p>
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult importNewItem(String item,String desc) {
		try {
			TbItem tbItem = JsonUtils.jsonToPojo(item, TbItem.class);
			String categoryName = itemMapper.getCategoryName(tbItem.getCid());
			// 创建一个SolrInputDocument对象
			SolrInputDocument document = new SolrInputDocument();
			document.setField("id", tbItem.getId());
			document.setField("item_title", tbItem.getTitle());
			document.setField("item_sell_point", tbItem.getSellPoint()); 
			document.setField("item_price", tbItem.getPrice());
			document.setField("item_image", tbItem.getImage());
			document.setField("item_category_name", categoryName);
			document.setField("item_desc", desc);
			// 写入索引库
			solrServer.add(document);
			// 提交修改
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}
}
