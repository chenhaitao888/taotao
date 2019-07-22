package com.taotao.service;

import java.util.List;

import com.taotao.pojo.TreeNode;

public interface ItemCatService {
	/**
	 * 取商品分类树
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<TreeNode>
	 */
	List<TreeNode> getItemCatList(long parentId);
}
