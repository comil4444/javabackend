package com.cn.eric.backend.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
	
	private static String IP = PropertiesUtil.getProperty("ftp.server.ip");
	private static String USERNAME = PropertiesUtil.getProperty("ftp.user");
	private static String PASSWORD = PropertiesUtil.getProperty("ftp.password");
	
			
	private String username;
	private String password;
	private String host;
	private int port;
	private FTPClient ftpClient;
	
	public FTPUtil(String host,int port,String username,String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	private boolean connect(String host,int port,String username,String password) {
		boolean isSuccess = true;
		
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(host);
			ftpClient.login(username, password);
		} catch (IOException e) {
			isSuccess = false;
			logger.error("连接服务器异常！",e);
		}
		
		return isSuccess;
	}
	
	public static boolean uploadFile(List<File> files) throws UnknownHostException {
		boolean uploaded = true;
		
		FTPUtil ftpUtil = new FTPUtil(IP,21,USERNAME,PASSWORD);
		
		logger.info("开始连接服务器！");
		boolean result = ftpUtil.uploadFile("img",files);
		logger.info("开始连接ftp服务器,结束上传,上传结果:{}",result);
		return uploaded;
	}
	
	
	private boolean uploadFile(String remotePath, List<File> files) {
		boolean upload = false;
		FileInputStream fis = null;
		
		if(connect(this.host,this.port,this.username,this.password)) {
			try {
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(4096);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				for(File file:files) {
					fis = new FileInputStream(file);
					ftpClient.storeFile(file.getName(), fis);
				}
				upload = true;
			} catch (IOException e) {
				logger.error("上传文件失败！",e);
			}
		}
		return upload;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
}
