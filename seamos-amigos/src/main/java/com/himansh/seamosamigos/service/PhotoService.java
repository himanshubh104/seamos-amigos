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
import com.himansh.seamosamigos.utility.AmigosUtils;

@Service
public class PhotoService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private AmigosUtils utility;
	
	//Get Profile photos
	public List<Photos> getUserPhotos(int userid){
		return photoRepository.getAllProfilePotos(userid);	
	}
	
	//Get photos in Feeds
	public List<PhotoWebModel> getFeedsByPicId(int userid, int picId){
		Pageable pageable= PageRequest.of(0, 10);
		return PhotoWebModel.findWebModels(photoRepository.getAllPotosByPhotoId(userid, picId, pageable));	
	}
	
	public List<PhotoWebModel> getHomeScreenPhotos(int userId) {
		Pageable pageable= PageRequest.of(0, 10);
		return PhotoWebModel.findWebModels(photoRepository.getAllPotos(userId, pageable));
	}
	
	//Get photos in Feeds
	public List<PhotoWebModel> getFeedsBytimestamp(int userid, String timestamp) throws Exception{
		Pageable pageable= PageRequest.of(0, 10);
		return PhotoWebModel.findWebModels(
				photoRepository.getAllPotosByTimestamp(userid, utility.stringToDateTime(timestamp), pageable));	
	}
	
	//Upload a picture
	public PhotoDto addUserPhoto(PhotoDto photo, int userId) throws Exception{
		Integer lastPicId = photoRepository.getMaxPhotoId();
		lastPicId = lastPicId==null? 0 : lastPicId;
		String uri=utility.saveUploadedFile(photo.getPicData(), userId, lastPicId);
		if (uri!=null) {
			Photos photoEntity=photo.generatePhotoEntity();
			photoEntity.setUrl(uri);
			photoEntity.setDateOfUpload(Calendar.getInstance().getTime());
			photoEntity.setUsers(userRepository.getListOfUsers(new int[]{userId}));
			return PhotoDto.generateDto(photoRepository.save(photoEntity));	
		}
		return photo;
	}

}
