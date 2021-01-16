package com.himansh.seamosamigos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.RequestDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.service.ConnectionService;

@RestController
@RequestMapping("api/seamos-amigos/")
public class ConnectionsController {
	@Autowired
	private ConnectionService connectionService;
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
    
	@GetMapping(path = "connections/followers")
	public List<UserDto> getUserFollowers(){
		return connectionService.getUserFollowers(userId);		
	}
	@GetMapping(path = "connections/followings")
	public List<UserDto> getUserFollowings(){
		return connectionService.getUserFolloiwngs(userId);		
	}
	
	@GetMapping(path="connections/request/respond")
	public boolean acceptOrRejectRequest(@RequestParam(name = "requestId") int requestId,
			@RequestParam(name = "response") char response) throws Exception {
		return connectionService.acceptOrRejectRequest(userId, requestId, response);
	}
	@GetMapping(path="connections/request")
	public ResponseEntity<Object> getAllRequests(){
		return ResponseEntity.ok(connectionService.getAllRequests(userId).stream()
				.map(RequestDto::genrateDto).collect(Collectors.toList()));
	}
	
	@GetMapping(path="connections/request/create")
	 public FollowRequests createRequest(@RequestParam(name = "requestedUser")int requestedUser)
			 throws Exception {
		int requestingUser=userId;
		return connectionService.createRequest(requestedUser, requestingUser);
	 }
}
