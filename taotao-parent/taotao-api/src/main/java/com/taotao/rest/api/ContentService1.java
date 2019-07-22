package com.taotao.rest.api;

import java.util.List;

import com.taotao.pojo.TbContent;

/**
 * 页面广告服务
 * <p>Title: ContentService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日下午3:58:51
 * @version:1.0
 */
public interface ContentService1 {
	/**
	 * 根据分类ID取内容
	 * <p>Title: getContenList</p>
	 * <p>Description: </p>
	 * @param categoryId
	 * @return: List<TbContent>
	 */
	List<TbContent> getContenList(Long categoryId);
}
