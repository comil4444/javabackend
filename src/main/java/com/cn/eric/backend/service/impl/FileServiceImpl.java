package com.cn.eric.backend.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.service.FileService;
import com.cn.eric.backend.util.FTPUtil;

@Service
public class FileServiceImpl implements FileService {
	
	private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile uploadFile,String dir) {
		
		String fileName = uploadFile.getOriginalFilename();
		String extensionName = fileName.substring(fileName.lastIndexOf(".")+1);
		String newFileName = UUID.randomUUID().toString()+"."+extensionName;
		
		logger.info("上传文件，原始文件名:{},服务器文件名:{}",fileName,newFileName);
		
		File d = new File(dir);
		if(!d.exists()) {
			d.setWritable(true);
			d.mkdirs();
		}
		
		File file = new File(d,newFileName);
		
		try {
			uploadFile.transferTo(file);
			
			List<File> files = new ArrayList<File>();
			files.add(file);
			FTPUtil.uploadFile(files);
			
			file.delete();
		} catch (IOException e) {
			logger.error("上传文件失败！",e);
			return null;
		}
		return file.getName();
	}

}
