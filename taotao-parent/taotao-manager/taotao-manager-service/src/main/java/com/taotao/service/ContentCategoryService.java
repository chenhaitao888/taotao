package com.taotao.service;

import java.util.List;

import com.taotao.pojo.TreeNode;
import com.taotao.result.TaotaoResult;

public interface ContentCategoryService {
	/**
	 * 取内容分类树
	 * <p>Title: getCategoryList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<TreeNode>
	 */
	List<TreeNode> getCategoryList(Long parentId);
	
	/**
	 * 新增内容管理分类节点
	 * <p>Title: insertContentCategory</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @param name
	 * @return: TaotaoResult
	 */
	TaotaoResult insertContentCategory(long parentId, String name);
	/**
	 * 重新命名内容管理节点
	 * <p>Title: renameContentCategory</p>
	 * <p>Description: </p>
	 * @param id
	 * @param name: void
	 */
	void renameContentCategory(Long id, String name);
	/**
	 * 删除内容管理节点
	 * <p>Title: delContentCategory</p>
	 * <p>Description: </p>
	 * @param id
	 * @param parentId: void
	 */
	void delContentCategory(Long id,Long parentId);
}
