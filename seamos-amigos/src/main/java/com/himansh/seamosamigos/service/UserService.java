package com.himansh.seamosamigos.service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.Connections;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.entity.PersonalInfoEntity;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.entity.UserEntity;
import com.himansh.seamosamigos.repository.ConnectionRepository;
import com.himansh.seamosamigos.repository.PersonalInfoRepository;
import com.himansh.seamosamigos.repository.PhotoRepository;
import com.himansh.seamosamigos.repository.RequestsRepository;
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
	private ConnectionRepository conRepository;
	@Autowired
	private RequestsRepository reqRepository;
	
	@Autowired
	private Utilities utility;
	
	public UserDto addUser(UserEntity user) {
		return UserDto.generateDto(userRepository.saveAndFlush(user));	
	}
	
	private UserEntity getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
	
	public PersonalInfoEntity getUserInfo(int userId) {
		return infoRepository.findById(userId).get();
	}
	
	public List<UserDto> getUserFollowers(int userId){
		UserEntity ue=getUserById(userId);
		return ue.getConnections().stream().map(con->{
			return UserDto.generateDto(con.getUser2());
		}).collect(Collectors.toList());
	}
	
	public List<UserDto> getUserFolloiwngs(int userId){
		return userRepository.getFollowings(userId).stream().map(u->{
			return UserDto.generateDto(u);
		}).collect(Collectors.toList());
	}
	
	/*-------------------------------------------Photos--------------------------------------------*/
	
	
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
		
	
	/*-------------------------------------------------Photos------------------------------------------*/
	/*------------------------------------------------Requests----------------------------------------------*/
	
	
	//Method for Follow Requests
	 public boolean acceptOrRejectRequest(int userId, int requestId,char response) throws Exception {
		 UserEntity ue=getUserById(userId);
		 FollowRequests request=null;
		 for(FollowRequests r: ue.getRequests()) {
			 if (r.getRequestId()==requestId) {
				request=r;
			}
		 }
		 if (request==null) {
			throw new Exception("No Request Found!!");
		 }
		switch (response) {
		case 'A':
			Connections con=new Connections();
			con.setUser1(request.getRequestedUser());
			con.setUser2(request.getRequestingUser());
			boolean result=conRepository.saveAndFlush(con)!=null;
			reqRepository.delete(request);
			return result;
		case 'R':
			reqRepository.delete(request);
			return true;
		default:
			return false;
		}	 
	 }
	 
	 public FollowRequests createRequest(int requestedUser,int requestingUser) throws Exception {
		 if (reqRepository.checkIfRequestAlreadyExists(requestedUser, requestingUser)!=null) {
				throw new Exception("Already Requested");
			}
		 if (conRepository.checkConnection(requestedUser, requestingUser)!=null) {
			throw new Exception("Already Following");
		}
		 if(requestedUser==requestingUser) {
			 throw new Exception("Can't follow yourself");
		 }
		 FollowRequests request=new FollowRequests();
		 request.setRequestedUser(getUserById(requestedUser));
		 request.setRequestingUser(getUserById(requestingUser));
		 return reqRepository.saveAndFlush(request);
	 }
	 
	 
	 /*------------------------------------------------Requests----------------------------------------------*/
}
