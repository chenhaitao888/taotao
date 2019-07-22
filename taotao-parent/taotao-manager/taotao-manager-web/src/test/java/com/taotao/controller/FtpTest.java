package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

public class FtpTest {
	@Test
	public void testFtp() throws Exception{
		//创建FtpClient对象
		FTPClient client = new FTPClient();
		//创建ftp连接
		client.connect("192.168.98.141",21);
		//登录ftp服务器
		client.login("ftpuser", "071359cht");
		/**
		 * 上传文件
		 */
		//从本地读取文件
		InputStream inputStream = new FileInputStream(new File("/Users/chenhaitao/Pictures/2.jpg"));
		//设置上传路径
		client.changeWorkingDirectory("/home/ftpuser/www/images");
		//修改上传格式
		client.setFileType(FTP.BINARY_FILE_TYPE);
		//第一个参数：服务器端文档名  
        //第二个参数，上传文档的inputStream 
		client.storeFile("hello.jpg", inputStream);
		//关闭连接
		client.logout();
	}
}
