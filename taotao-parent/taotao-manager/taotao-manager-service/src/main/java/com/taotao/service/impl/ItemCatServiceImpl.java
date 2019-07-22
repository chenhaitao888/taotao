package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TreeNode;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

/**
 * 商品分类树形展示servie层
 * <p>Title: ItemCatServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月17日下午7:03:21
 * @version:1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	/**
	 * 取商品分类树
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<TreeNode>
	 */
	@Override
	public List<TreeNode> getItemCatList(long parentId) {
		//根据parentId查询分类列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询条件
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		//创建TreeNode格式的列表
		List<TreeNode> list = new ArrayList<TreeNode>();
		if(itemCatList != null && itemCatList.size() > 0){
			for (TbItemCat tbItemCat : itemCatList) {
				TreeNode treeNode = new TreeNode(tbItemCat.getId(), tbItemCat.getName(), 
						tbItemCat.getIsParent()?"closed":"open");
				list.add(treeNode);
			}
			return list;
		}
		return null;
	}

}
