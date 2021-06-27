package com.himansh.seamosamigos.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class AmigosUtils {
	
	//private final String UPLOADED_FOLDER= "C:\\Users\\Himansh\\Documents\\GIT_Repo\\seamos-amigos\\stored-images\\";
	
	public Date stringToDate(String strDate)throws Exception{
		Date date= new SimpleDateFormat("dd-MMM-yyyy").parse(strDate);
		return date;
	}
	
	public Date stringToDateTime(String strDate)throws Exception{
		Date date= new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(strDate);
		return date;
	}
	
	public String dateToString(Date date) {
		String strDate= new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
		return strDate;
	}
	
	//Method to Save the Picture
	public String saveUploadedFile(MultipartFile file,int userId, Integer lastPicId) throws Exception {
	    if (!file.isEmpty()) {
	        byte[] bytes = file.getBytes();
	        Files.createDirectories(Paths.get(AmigosConstants.MEDIA_FOLDER));
	        String fileType = file.getContentType();
	        switch (fileType) {
	        	case "image/jpeg": 
	        		fileType = "JPEG";
	        		break;
	        	case "image/png":
	        		fileType = "PNG";
	        		break;
	        }
	        Path path = Paths.get(AmigosConstants.MEDIA_FOLDER + "IMG"+(lastPicId+1)+"-"+userId+"."+fileType);
	        return Files.write(path, bytes).toString();
	    }
	    return null;
	}
	
	
	public String extractClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
	
	public String extractClientUserAgent(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		return userAgent.getBrowser().getName()+" : "+userAgent.getOperatingSystem().getName();
	}
}
