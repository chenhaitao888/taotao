package com.taotao.portal.service;

import com.taotao.pojo.TbUser;

/**
 * 用户信息
 * <p>Title: UserService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月13日下午8:25:09
 * @version:1.0
 */
public interface UserService {
	/**
	 * 根据token获取用户信息
	 * <p>Title: getUserByToken</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TbUser
	 */
	TbUser getUserByToken(String token);
}
