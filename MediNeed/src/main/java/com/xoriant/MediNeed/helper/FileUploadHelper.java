package com.xoriant.MediNeed.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	
	private String UPLOAD_DIR = "E:\\Uploaded Files";
	
	public String getUPLOAD_DIR() {
		return UPLOAD_DIR;
	}

	public void setUPLOAD_DIR(String uPLOAD_DIR) {
		UPLOAD_DIR = uPLOAD_DIR;
	}

	private String FULL_PATH;

	public String getFULL_PATH() {
		return FULL_PATH;
	}

	public void setFULL_PATH(String fULL_PATH) {
		FULL_PATH = fULL_PATH;
	}

	public boolean fileUploadHelper(MultipartFile file) 
	{ 
		try 
		{
			Files.copy(file.getInputStream(), Paths.get(getFULL_PATH() + File.separator +
					file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING); 
			
			return true; 
		} catch (Exception e) { 
			System.out.println(e.getMessage()); 
			return false; 
		} 
	}
	 
}
