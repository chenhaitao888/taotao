package com.taotao.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.taotao.result.PictureResult;

public interface PictureService {
	/**
	 * 图片上传方法
	 * <p>Title: uploadPicture</p>
	 * <p>Description: </p>
	 * @param uploadFile
	 * @return: Map
	 */
	Map uploadPicture(MultipartFile uploadFile);
}
