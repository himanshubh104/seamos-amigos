package com.himansh.seamosamigos.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Utilities {
	
	private final String UPLOADED_FOLDER= "C:\\Users\\Himansh\\Documents\\GIT_Repo\\seamos-amigos\\stored-images\\";
	
	public Date stringToDate(String strDate)throws Exception{
		Date date= new SimpleDateFormat("dd-MMM-yyyy").parse(strDate);
		return date;
	}
	
	public Date stringToTimeStamp(String strDate)throws Exception{
		Date date= new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(strDate);
		return date;
	}
	
	public String dateToString(Date date)throws Exception{
		String strDate= new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
		return strDate;
	}
	
	//Method to Save the Picture
	public String saveUploadedFile(MultipartFile file,int userId) throws Exception {
	    if (!file.isEmpty()) {
	        byte[] bytes = file.getBytes();
	        Path path = Paths.get(UPLOADED_FOLDER + "img_"+userId+"-"+file.getOriginalFilename());
//	        Path path = Paths.get(UPLOADED_FOLDER + "img_"+userId+"-"+dateToString(Calendar.getInstance().getTime())+file.getContentType());
	        return Files.write(path, bytes).toUri().getPath();
	    }
	    return null;
	}

}
