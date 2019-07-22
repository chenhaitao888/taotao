package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.rest.service.RedisService;
import com.taotao.result.TaotaoResult;

/**
 * 缓存同步controller
 * <p>Title: RedisController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月3日下午8:25:57
 * @version:1.0
 */
@Controller
@RequestMapping("/cache/sync")
public class RedisController {
	@Autowired
	private RedisService redisService;
	/**
	 * 根据contentCid同步缓存
	 * <p>Title: contentCacheSync</p>
	 * <p>Description: </p>
	 * @param contentCid
	 * @return: TaotaoResult
	 */
	@RequestMapping("/content/{contentCid}")
	public TaotaoResult contentCacheSync(@PathVariable Long contentCid) {
		TaotaoResult result = redisService.syncContent(contentCid);
		return result;
	}
}
