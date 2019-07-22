package com.taotao.rest.service;

import com.taotao.result.TaotaoResult;

/**
 * redis服务
 * <p>Title: RedisService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月3日下午8:12:15
 * @version:1.0
 */
public interface RedisService {
	/**
	 * 同步CMS系统内容管理
	 * <p>Title: sysnContent</p>
	 * <p>Description: </p>
	 * @param contentCid
	 * @return: TaotaoResult
	 */
	TaotaoResult syncContent(Long contentCid);
}
