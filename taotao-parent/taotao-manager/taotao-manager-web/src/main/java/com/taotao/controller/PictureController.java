package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;
import com.taotao.utils.JsonUtils;

/**
 * 图片上传controller
 * <p>Title: PictureController</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月18日下午6:25:32
 * @version:1.0
 */
@Controller
public class PictureController {
	@Autowired
	private PictureService pictureService;
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile){
		Map result = pictureService.uploadPicture(uploadFile);
		//为了保证浏览器的兼容性,将map对象转换成json格式的字符串
		String jsonString = JsonUtils.objectToJson(result);
		return jsonString;
	}
}
