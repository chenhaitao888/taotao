package com.taotao.portal.service;
/**
 * 广告内容service
 * <p>Title: ContentService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日下午5:32:39
 * @version:1.0
 */
public interface ContentService {
	/**
	 * 通过httpclient接口获取rest服务发布的根据categoryId得到的内容分类
	 * <p>Title: getContentList</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	String getContentList();
}
