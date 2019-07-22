package com.taotao.rest.service.ipml;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.api.ContentService1;
import com.taotao.rest.dao.CodisManager;
import com.taotao.utils.JsonUtils;
/**
 * 广告服务service
 * <p>Title: ContentServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日下午4:07:43
 * @version:1.0
 */
//@Service(interfaceName="com.taotao.rest.api.ContentService",timeout=10000)
public class ContentServiceImpl implements ContentService1 {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private CodisManager codisManager;
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	/**
	 * 根据分类ID取内容
	 * <p>Title: getContenList</p>
	 * <p>Description: </p>
	 * @param categoryId
	 * @return: List<TbContent>
	 */
	@Override
	public List<TbContent> getContenList(Long categoryId) {
		/**
		 * 从缓存中取内容
		 */
		try {
			String reString = codisManager.hget(INDEX_CONTENT_REDIS_KEY, categoryId + "");
			if(StringUtils.isNoneBlank(reString)){
				List<TbContent> resultList = JsonUtils.jsonToList(reString, TbContent.class);
				return resultList;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		/**
		 * 向缓存中添加内容
		 */
		//将list转换成json字符串
		try {
			String cacheString = JsonUtils.objectToJson(list);
			codisManager.hset(INDEX_CONTENT_REDIS_KEY, categoryId + "", cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
	}
	
}
