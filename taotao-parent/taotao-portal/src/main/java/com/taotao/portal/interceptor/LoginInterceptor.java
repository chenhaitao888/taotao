package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import com.taotao.utils.CookieUtils;

public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;
	@Value("${SSO_BASE_URL}")
	private String SSO_BASE_URL;
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// 在handler执行之前
		// 返回值决定handler是否执行,true:执行,false:不执行
		/**
		 * 1.判断用户是否登录,从cookie中取token 2.根据token取用户信息,调用sso接口
		 * 3.如果取不到,跳转到登录页面,把用户请求的url作为参数传递给登录页面,返回false 4.如果取到用户信息,则返回true,放行
		 */
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 根据token换取用户信息
		TbUser user = userService.getUserByToken(token);
		// 如果取不到用户
		if (null == user) {
			// 跳转到登录页面，把用户请求的url作为参数传递给登录页面
			response.sendRedirect(SSO_BASE_URL + SSO_LOGIN_URL + "?redirect=" + request.getRequestURL());
			// 返回false
			return false;
		}
		//取到用户信息放行
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//在handler执行之后,返回ModelAndView之前
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// 返回ModelAndView之后
		// 响应用户之后
	}
}
