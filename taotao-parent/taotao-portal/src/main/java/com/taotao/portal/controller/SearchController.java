package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 门户搜索服务controller
 * <p>Title: SearchController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午10:05:33
 * @version:1.0
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	/**
	 * 根据查询条件queryString,当前页page
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param queryString
	 * @param page
	 * @return: String
	 */
	@RequestMapping("/search")
	public String search(@RequestParam("q")String queryString, @RequestParam(defaultValue="1")int page,Model model){
		/**
		 * 解决乱码
		 */
		if (queryString != null) {
			try {
				queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SearchResult result = searchService.search(queryString, page);
		//向页面传递参数
				model.addAttribute("query", queryString);
				model.addAttribute("totalPages", result.getPageCount());
				model.addAttribute("itemList", result.getItemSolrList());
				model.addAttribute("page", page);
		return "search";
	}
}
