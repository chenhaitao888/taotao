package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.HttpClientUtil;
/**
 * 用户信息
 * <p>Title: UserService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月13日下午8:25:09
 * @version:1.0
 */
@Service
public class UserServiceImpl implements UserService {
	@Value("${SSO_BASE_URL}")
	private String SSO_BASE_URL;
	@Value("${SSO_USER_TOKEN}")
	private String SSO_USER_TOKEN;
	/**
	 * 根据token获取用户信息
	 * <p>Title: getUserByToken</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TbUser
	 */
	@Override
	public TbUser getUserByToken(String token) {
		try {
			//调用sso系统的服务，根据token取用户信息
			String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
			//把json转换成TaotaoResult
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbUser.class);
			if(taotaoResult.getStatus() == 200){
				return  (TbUser) taotaoResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
