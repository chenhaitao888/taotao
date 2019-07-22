package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
/**
 * 图片上传处理service
 * <p>Title: PictureServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月18日下午6:24:40
 * @version:1.0
 */
@Service
public class PictureServiceImpl implements PictureService {
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USER_NAME}")
	private String FTP_USER_NAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	/**
	 * 图片上传方法
	 * <p>Title: uploadPicture</p>
	 * <p>Description: </p>
	 * @param uploadFile
	 * @return: Map
	 */
	@SuppressWarnings({ "rawtypes", "null", "unchecked" })
	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		Map map = new HashMap<>();
		try {
			//判断上传的文件是否为空
			if(uploadFile == null && uploadFile.isEmpty()){
				map.put("error", 1);
				map.put("message", "文件为空,无法上传");
				return map;
			}
			//取文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			//截取从.开始的后面字符串
			String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
			//生成新文件名
			//使用时间+随机数生成
			String newName = IDUtils.genImageName();
			//使用时间(/yyyy/MM/dd)的格式作为filepath
			DateTime dateTime = new DateTime();
			String filePath =  dateTime.toString("/yyyy/MM/dd");
			//把文件上传到Ngnix服务器
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USER_NAME, FTP_PASSWORD, FTP_BASE_PATH,
						filePath, newName + ext, uploadFile.getInputStream());
			if(!result){
				map.put("error", 1);
				map.put("message", "文件上传发生错误");
				return map;
			}
			map.put("error", 0);
			map.put("url", IMAGE_BASE_URL + filePath + "/" + newName + ext);
			return map;
		} catch (IOException e) {
			map.put("error", 1);
			map.put("message", "文件上传发生异常");
			return map;
		}
		
	}

}
