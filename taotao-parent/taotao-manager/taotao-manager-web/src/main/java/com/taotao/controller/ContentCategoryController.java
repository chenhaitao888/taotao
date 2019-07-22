package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TreeNode;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理Controller
 * <p>Title: ContentCategoryController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月27日下午3:05:29
 * @version:1.0
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	/**
	 * 返回内容分类树
	 * <p>Title: getContentCatList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<TreeNode>
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<TreeNode> getContentCatList(@RequestParam(value="id",defaultValue="0")Long parentId){
		List<TreeNode> result = contentCategoryService.getCategoryList(parentId);
		return result;
	}
	
	/**
	 * 内容管理节点增加
	 * <p>Title: createContentCategory</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @param name
	 * @return: TaotaoResult
	 */
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult createContentCategory(Long parentId,String name){
		TaotaoResult result = contentCategoryService.insertContentCategory(parentId, name);
		return result;
	}
	/**
	 * 更新内容管理节点
	 * <p>Title: renameContentCategory</p>
	 * <p>Description: </p>
	 * @param id
	 * @param name: void
	 */
	@RequestMapping("/update")
	@ResponseBody
	public void renameContentCategory(Long id,String name){
		contentCategoryService.renameContentCategory(id, name);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public void updateContentCategory(Long id,Long parentId){
		contentCategoryService.delContentCategory(id, parentId);
	}
	
}
