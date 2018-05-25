package com.cn.eric.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	String uploadFile(MultipartFile uploadFile,String dir);
	
}