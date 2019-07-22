package com.taotao.portal.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 显示首页
 * <p>Title: IndexController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月26日下午1:16:22
 * @version:1.0
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.portal.service.ContentService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	@Value("${SSO_BASE_URL}")
	private String SSO_BASE_URL;
	@Value("${USER_LOGOUT_URL}")
	private String USER_LOGOUT_URL;
	/**
	 * 门户首页
	 * <p>Title: showIndex</p>
	 * <p>Description: </p>
	 * @param model
	 * @return: String
	 */
	@RequestMapping("/index")//因为web.xml中初始化页面是index.xml,且使用了伪静态化*.html,所以.html的会被拦截
	public String showIndex(Model model){
		String adJson = contentService.getContentList();
		model.addAttribute("ad1", adJson);
		return "index";
	}
	
	@RequestMapping(value="/httpclient/post",method=RequestMethod.POST)
	@ResponseBody
	public String testPost(String username,String password){
		return "username" + username +"/t" + "password" + password;
	}
	/**
	 * 用户退出
	 * <p>Title: returnIndex</p>
	 * <p>Description: </p>
	 * @param model
	 * @return: String
	 */
	@RequestMapping("/user/logout.html")
	public String returnIndex(String callback, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
//			Map<String, String> params = new HashMap<String,String>();
//			params.put("callback", null);
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			//调用单点登录系统安全退出接口
			String json = HttpClientUtil.doGet(SSO_BASE_URL + USER_LOGOUT_URL + token);
			TaotaoResult result = JsonUtils.jsonToPojo(json, TaotaoResult.class);
			//如果退出失败
			if (result.getStatus() != 200) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String adJson = contentService.getContentList();
		model.addAttribute("ad1", adJson);
		return "index";
	}
}
