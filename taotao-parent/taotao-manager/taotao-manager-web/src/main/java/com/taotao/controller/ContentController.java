package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ContentService;

/**
 * 内容管理
 * <p>Title: ContentController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日上午10:43:21
 * @version:1.0
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	/**
	 * 显示内容管理
	 * <p>Title: showContent</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return: EUDataGridResult
	 */
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataGridResult showContent(int page, int rows, Long categoryId){
		EUDataGridResult result = contentService.showContent(page, rows, categoryId);
		return result;
	}
	
	/**
	 * 增加内容管理
	 * <p>Title: addContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent tbContent){
		if(tbContent  != null){
			TaotaoResult result = contentService.addContent(tbContent);
			return result;
		}
		return TaotaoResult.build(500, "表单对象为空");
	}
	
	/**
	 * 更新内容管理
	 * <p>Title: editContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public TaotaoResult editContent(TbContent tbContent){
		if(tbContent  != null){
			TaotaoResult result = contentService.editContent(tbContent);
			return result;
		}
		return TaotaoResult.build(500, "表单对象为空");
	}
}
