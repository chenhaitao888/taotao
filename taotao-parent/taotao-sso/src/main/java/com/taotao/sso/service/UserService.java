package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.pojo.TbUser;
import com.taotao.result.TaotaoResult;

/**
 * 用户管理service
 * <p>Title: UserService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月9日上午10:24:57
 * @version:1.0
 */
public interface UserService {
	/**
	 * 根据参数和类型检查用户数据是否有效
	 * <p>Title: checkData</p>
	 * <p>Description: </p>
	 * @param content
	 * @param type
	 * @return: TaotaoResult
	 */
	TaotaoResult checkData(String content, Integer type);
	/**
	 * 注册用户信息
	 * <p>Title: register</p>
	 * <p>Description: </p>
	 * @param user
	 * @return: TaotaoResult
	 */
	TaotaoResult register(TbUser user);
	
	/**
	 * 用户登录
	 * 登录成功,生成一个token,token使用UUID
	 * 把用户信息写入redis,key就是token,value就是用户信息
	 * 返回token字段
	 * <p>Title: userLogin</p>
	 * <p>Description: </p>
	 * @param username
	 * @param password
	 * @return: TaotaoResult
	 */
	TaotaoResult userLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response);
	/**
	 * 根据token获取用户信息
	 * <p>Title: getUserByToken</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TaotaoResult
	 */
	TaotaoResult getUserByToken(String token);
	/**
	 * 用户安全退出
	 * <p>Title: logOut</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TaotaoResult
	 */
	TaotaoResult userlogout(String token,HttpServletRequest request, HttpServletResponse response);
}
