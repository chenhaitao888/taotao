package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.dao.CodisManager;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 用户管理service
 * <p>Title: UserService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月9日上午10:24:57
 * @version:1.0
 */

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private CodisManager codisManager;
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	/**
	 * 根据参数和类型检查用户数据是否有效
	 * <p>Title: checkData</p>
	 * <p>Description: </p>
	 * @param content
	 * @param type
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		//创造查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//对数据进行校验：1、2、3分别代表username、phone、email
		//用户名校验
		if(1 == type){
			criteria.andUsernameEqualTo(content);
		}
		//电话校验
		else if(2 == type){
			criteria.andPhoneEqualTo(content);
		}
		//email校验
		else{
			criteria.andPhoneEqualTo(content);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0){
			//数据可用
			return TaotaoResult.ok(true);
		}
		//数据不可用
		return TaotaoResult.ok(false);
	}

	
	/**
	 * 注册用户信息
	 * <p>Title: register</p>
	 * <p>Description: </p>
	 * @param user
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult register(TbUser user) {
		//补全用户信息
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//md5给password加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

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
	@Override
	public TaotaoResult userLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//开始查询
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//生成token
		String token = UUID.randomUUID().toString();
		/*把用户信息写入到reis*/
		//在写入到redis之前需要把密码清空,因为密码信息存入到redis不安全
		user.setPassword(null);
		//把用户信息写入redis
		try {
			codisManager.set(REDIS_USER_SESSION_KEY + ":" + token, 
					JsonUtils.objectToJson(user));
			//设置session的过期时间
			codisManager.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//添加写cookie的逻辑,cookie的有效期是关闭浏览器失效
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		//返回token
		return TaotaoResult.ok(token);
	}


	/**
	 * 根据token获取用户信息
	 * <p>Title: getUserByToken</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		//从redis中获取用户
		String json = codisManager.get(REDIS_USER_SESSION_KEY + ":" + token);
		//判断取到的值是否为空
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "此session已经过期,请重新登录");
		}
		//取到用户信息后,需要刷新session时间
		try {
			codisManager.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}

	/**
	 * 用户安全退出
	 * <p>Title: logOut</p>
	 * <p>Description: </p>
	 * @param token
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult userlogout(String token,HttpServletRequest request, HttpServletResponse response) {
		try {
			//用户退出,将redis中的用户信息清除
			codisManager.delete(REDIS_USER_SESSION_KEY + ":" + token);
			//删除cookie
			CookieUtils.deleteCookie(request, response, "TT_TOKEN");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

}
