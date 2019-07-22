package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 用户管理controller
 * <p>Title: UserController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月9日上午10:54:45
 * @version:1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbUser;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.service.UserService;
import com.taotao.utils.ExceptionUtil;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * 检查用户数据是否可用
	 * <p>Title: checkData</p>
	 * <p>Description: </p>
	 * @param param
	 * @param type
	 * @param callback
	 * @return: Object
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type,
			String callback){
		TaotaoResult taotaoResult = null;
		//参数有效性校验
		if(StringUtils.isBlank(param)){
			taotaoResult = TaotaoResult.build(400, "校验内容不能为空");
		}
		if(null == type){
			taotaoResult = TaotaoResult.build(400, "校验类型不能为空");
		}
		if(1 != type && 2!= type && 3 != type){
			taotaoResult = TaotaoResult.build(400,"校验类型错误");
		}
		/*如果校验出错*/
		if(null != taotaoResult){
			/*判断是否要支持jsonp*/
			if(StringUtils.isNotBlank(callback)){
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}else {
				return taotaoResult;
			}
		}
		/*如果校验正确,则调用userService*/
		try {
			taotaoResult = userService.checkData(param, type);
		} catch (Exception e) {
			e.printStackTrace();
			taotaoResult = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		/*判断是否需要支持jsonp*/
		if(null != callback){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}else{
			return taotaoResult;
		}
	}
	
	/**
	 * 用户注册
	 * <p>Title: register</p>
	 * <p>Description: </p>
	 * @param user
	 * @return: TaotaoResult
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user){
		try {
			TaotaoResult result = userService.register(user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	
	/**
	 * 用户登录
	 * <p>Title: userLogin</p>
	 * <p>Description: </p>
	 * @param username
	 * @param password
	 * @return: TaotaoResult
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response){
		try {
			TaotaoResult result = userService.userLogin(username,password,request,response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 根据token取用户信息
	 * <p>Title: getUserByToken</p>
	 * <p>Description: </p>
	 * @param token
	 * @param callback
	 * @return: Object
	 */
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback){
		TaotaoResult result = null;
		try {
			result = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		//判断是否支持jsonp
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return result;
	}
	//用户退出
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object userlogout(@PathVariable String token,String callback,
			HttpServletRequest request, HttpServletResponse response){
		TaotaoResult result = null;
		try {
			result = userService.userlogout(token,request,response);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		//判断是否支持jsonp
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
