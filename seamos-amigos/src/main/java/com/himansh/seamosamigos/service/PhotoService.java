package com.himansh.seamosamigos.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.repository.PhotoRepository;
import com.himansh.seamosamigos.repository.UserRepository;
import com.himansh.seamosamigos.utility.Utilities;

@Service
public class PhotoService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private Utilities utility;
	
	//Get Profile photos
	public List<Photos> getUserPhotos(int userid){
		return photoRepository.getAllProfilePotos(userid);	
	}
	
	//Get photos in Home
	public List<Photos> getHomeScreenPhotos(int userid){
		return photoRepository.getAllPotos(userid);	
	}
	
	//Upload a picture
	public PhotoDto addUserPhoto(PhotoDto photo) throws Exception{
		String uri=utility.saveUploadedFile(photo.getPicData(),photo.getUserId());
		if (uri!=null) {
			Photos photoEntity=photo.generatePhotoEntity();
			photoEntity.setUrl(uri);
			photoEntity.setDateOfUpload(Calendar.getInstance().getTime());
			photoEntity.setUsers(userRepository.getListOfUsers(photo.getUserId()));
			return PhotoDto.generateDto(photoRepository.save(photoEntity));	
		}
		return photo;
	}

}
