package com.taotao.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.HttpClientUtil;
/**
 * 门户搜索服务
 * <p>Title: SearchService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午9:55:16
 * @version:1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;
	
	/**
	 * 根据查询条件和当前页进行查询
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param queryString
	 * @param page
	 * @return: SearchResult
	 */
	@Override
	public SearchResult search(String queryString, int page) {
		// 调用taotao-search的服务
		// 查询参数
		Map<String, String> param = new HashMap<>();
		param.put("q", queryString);
		param.put("page", page + "");
		try {
			//调用服务
			String json = HttpClientUtil.doGet(SEARCH_BASE_URL,param);
			//把字符串转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
			if (taotaoResult.getStatus() == 200) {
				SearchResult result = (SearchResult) taotaoResult.getData();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
