package com.himansh.seamosamigos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.Connections;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.entity.User;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.repository.ConnectionRepository;
import com.himansh.seamosamigos.repository.RequestsRepository;
import com.himansh.seamosamigos.repository.UserRepository;

@Service
public class ConnectionService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConnectionRepository conRepository;
	@Autowired
	private RequestsRepository reqRepository;
	
	
	private User getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
	
	public List<UserDto> getUserFollowers(int userId){
		User ue=getUserById(userId);
		return ue.getConnections().stream().map(con->{
			return UserDto.generateDto(con.getUser2());
		}).collect(Collectors.toList());
	}
	
	public List<UserDto> getUserFolloiwngs(int userId){
		return userRepository.getFollowings(userId).stream().map(u->{
			return UserDto.generateDto(u);
		}).collect(Collectors.toList());
	}
	
	//Method for Follow Requests
	 public boolean acceptOrRejectRequest(int userId, int requestId,char response) throws InAppException {
		 User ue=getUserById(userId);
		 FollowRequests request=null;
		 for(FollowRequests r: ue.getRequests()) {
			 if (r.getRequestId()==requestId) {
				request=r;
			}
		 }
		 if (request==null) {
			throw new InAppException("No Request Found!!");
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
	 
	 public FollowRequests createRequest(int requestedUser,int requestingUser) throws InAppException {
		 if (reqRepository.checkIfRequestAlreadyExists(requestedUser, requestingUser)!=null) {
				throw new InAppException("Already Requested");
			}
		 if (conRepository.checkConnection(requestedUser, requestingUser)!=null) {
			throw new InAppException("Already Following");
		}
		 if(requestedUser==requestingUser) {
			 throw new InAppException("Can't follow yourself");
		 }
		 FollowRequests request=new FollowRequests();
		 request.setRequestedUser(getUserById(requestedUser));
		 request.setRequestingUser(getUserById(requestingUser));
		 return reqRepository.saveAndFlush(request);
	 }
	 
	@GetMapping(path="users/request")
	public List<FollowRequests> getAllRequests(int requestedUser){
		return reqRepository.getAllRequests(requestedUser);
	}
}
