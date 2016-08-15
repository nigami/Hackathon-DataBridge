package com.hack.pojo;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	
	MultipartFile file;
	
	public FileUpload(){
		
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}
