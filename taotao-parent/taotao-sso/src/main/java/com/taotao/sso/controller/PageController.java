package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户注册登录页面
 * <p>Title: PageController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月10日上午11:41:19
 * @version:1.0
 */
@Controller
@RequestMapping("/user")
public class PageController {
	/**
	 * 用户注册页面
	 * <p>Title: showRegister</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	@RequestMapping("/showRegister")
	public String showRegister(){
		return "register";
	}
	/**
	 * 用户登录页面
	 * <p>Title: showLogin</p>
	 * <p>Description: </p>
	 * @return: String
	 */
	@RequestMapping("/showLogin")
	public String showLogin(String redirect, Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
}
