package com.taotao.rest.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.rest.dao.CodisManager;
import com.taotao.rest.service.RedisService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.ExceptionUtil;
/**
 * redis服务
 * <p>Title: RedisService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月3日下午8:12:15
 * @version:1.0
 */
@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	private CodisManager codisManager;
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	/**
	 * 同步CMS系统内容管理
	 * <p>Title: sysnContent</p>
	 * <p>Description: </p>
	 * @param contentCid
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult syncContent(Long contentCid) {
		try {
			codisManager.hdel(INDEX_CONTENT_REDIS_KEY, contentCid + "");
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}

}
