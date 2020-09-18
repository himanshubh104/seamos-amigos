package com.himansh.seamosamigos.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.PersonalInfoEntity;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.entity.UserEntity;
import com.himansh.seamosamigos.repository.PersonalInfoRepository;
import com.himansh.seamosamigos.repository.PhotoRepository;
import com.himansh.seamosamigos.repository.UserRepository;
import com.himansh.seamosamigos.utility.Utilities;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	@Qualifier("personalInfoRepository")
	private PersonalInfoRepository infoRepository;
	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private Utilities utility;
	
	public UserDto addUser(UserEntity user) {
		return UserDto.generateDto(userRepository.saveAndFlush(user));	
	}
	
	public UserEntity getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
	
	public PersonalInfoEntity getUserInfo(int userId) {
		return infoRepository.findById(userId).get();
	}
	
	public List<Photos> getUserPhotos(int userid){
		return photoRepository.getAllPotos(userid);	
	}
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
