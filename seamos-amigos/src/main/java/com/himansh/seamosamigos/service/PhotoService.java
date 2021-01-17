package com.himansh.seamosamigos.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.PhotoWebModel;
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
	
	//Get photos in Feeds
	public List<PhotoWebModel> getFeedsByPicId(int userid, int picId){
		Pageable pageable= PageRequest.of(0, 1);
		return PhotoWebModel.findWebModels(photoRepository.getAllPotosByPhotoId(userid, picId, pageable));	
	}
	
	public List<PhotoWebModel> getHomeScreenPhotos(int userId) {
		Pageable pageable= PageRequest.of(0, 1);
		return PhotoWebModel.findWebModels(photoRepository.getAllPotos(userId, pageable));
	}
	
	//Get photos in Feeds
	public List<PhotoWebModel> getFeedsBytimestamp(int userid, String timestamp) throws Exception{
		Pageable pageable= PageRequest.of(0, 1);
		return PhotoWebModel.findWebModels(
				photoRepository.getAllPotosByTimestamp(userid, utility.stringToTimeStamp(timestamp), pageable));	
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
