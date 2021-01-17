package com.himansh.seamosamigos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.PhotoWebModel;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.service.PhotoService;

@RestController
@RequestMapping("api/seamos-amigos/")
public class MediaController {
	@Autowired
	private PhotoService photoService;
	private int userId=-1;
	
	
    @ModelAttribute
    public void fetchUser() {
    	Authentication auth= SecurityContextHolder.getContext().getAuthentication();
    	if (auth !=null) {
    		Object principal =auth.getPrincipal();
        	if (principal instanceof UserPrincipal) {
        		userId = ((UserPrincipal)principal).getUserId();
        		System.out.println("User is: "+((UserPrincipal)principal).getUsername());
        	} 
		}
    }
    /*-----------------------------------------------Photos-------------------------------------------------------*/
	@PostMapping(path = "media/photos")
	public PhotoDto addUserPhoto(@ModelAttribute PhotoDto photoDto) throws Exception {
		PhotoDto pic=photoService.addUserPhoto(photoDto);
		pic.setPicData(null);
		return pic;
	}
	@GetMapping(path = "media/photos")
	public List<Photos> getUserPhotos(){
		return photoService.getUserPhotos(userId);		
	}
	@GetMapping(path = "media/feeds/photosById")
	public List<PhotoWebModel> getFeedsByPicId(@RequestParam(name="picId", required = false) Integer picId){
		if(picId==null) {
			return photoService.getHomeScreenPhotos(userId);
		}
		return photoService.getFeedsByPicId(userId, picId);		
	}
	
	@GetMapping(path = "media/feeds/photos")
	public List<PhotoWebModel> getFeedsPhotos(@RequestParam(name = "timestamp", required = false) String timestamp) throws Exception{
		if (StringUtils .isEmpty(timestamp)) {
			return photoService.getHomeScreenPhotos(userId);
		}
		return photoService.getFeedsBytimestamp(userId, timestamp);		
	}
}