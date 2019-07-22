package com.taotao.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbContent;
import com.taotao.rest.api.ContentService1;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.ExceptionUtil;

/**
 * 内容发布controller
 * <p>Title: ContentController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日下午4:21:02
 * @version:1.0
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService1 contentService;
	@RequestMapping("/list/{contentCategoryId}")
	@ResponseBody
	public TaotaoResult getContentList(@PathVariable Long contentCategoryId){
		try {
			List<TbContent> contenList = contentService.getContenList(contentCategoryId);
			//使用TaotaoResult将返回的contentList进行包装并返回
			return TaotaoResult.ok(contenList);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
