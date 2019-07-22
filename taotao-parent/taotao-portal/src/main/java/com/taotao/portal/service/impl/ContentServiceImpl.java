package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import com.taotao.rest.api.ContentService1;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;
/**
 * 广告内容service
 * <p>Title: ContentService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日下午5:32:39
 * @version:1.0
 */
@Service
public class ContentServiceImpl implements ContentService {
	
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_INDEX_AD_URL}")
	private String REST_INDEX_AD_URL;
	/*@Reference(interfaceName="com.taotao.rest.api.ContentService")
	private com.taotao.rest.api.ContentService contentService;*/
	@Resource
	private ContentService1 contentService;
	/*public void setContentService(com.taotao.rest.api.ContentService contentService) {
		this.contentService = contentService;
	}*/

	/**
	 * 通过httpclient接口获取rest服务发布的根据categoryId得到的内容分类
	 * <p>Title: getContentList</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getContentList() {
		//调用服务层的服务
		//String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL);
		Long categoryId = (long) 89;
		// 把字符串转换成TaotaoResult
		try {
			//TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
			// 取内容列表
			//List<TbContent> list = (List<TbContent>) taotaoResult.getData();
			List<TbContent> list = contentService.getContenList(categoryId);
			List<Map> resultList = new ArrayList<>();
			// 创建一个jsp页码要求的pojo列表
			for (TbContent tbContent : list) {
				Map map = new HashMap<>();
				map.put("src", tbContent.getPic());
				map.put("height", 240);
				map.put("width", 670);
				map.put("srcB", tbContent.getPic2());
				map.put("widthB", 550);
				map.put("heightB", 240);
				map.put("href", tbContent.getUrl());
				map.put("alt", tbContent.getSubTitle());
				resultList.add(map);
			}
			return JsonUtils.objectToJson(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
