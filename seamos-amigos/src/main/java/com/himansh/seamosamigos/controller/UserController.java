package com.himansh.seamosamigos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.RequestDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.service.PhotoService;
import com.himansh.seamosamigos.service.RequestService;
import com.himansh.seamosamigos.service.UserService;
import com.himansh.seamosamigos.utility.JwtUtility;

@RestController
@RequestMapping("api/seamos-amigos/")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private JwtUtility jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
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
    
	//Register A User
	@PostMapping(path = "users/register",consumes = "application/JSON")
	public UserDto addUser(@RequestBody UserDto user) {
		return userService.addUser(user.generateEntity(),user.getRoles());	
	}
	
	//Login end point
	@PostMapping(path = "users/login",consumes = "application/JSON")
	public Map<String,Object> loginUser(@RequestBody UserDto user) {
		try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		UserPrincipal userPrincipal=(UserPrincipal) userService.loadUserByUsername(user.getEmail());
		String jwt=jwtUtil.generateToken(userPrincipal);
		 Map<String,Object> map= new HashMap<>();
	        map.put("authenticated",true);
	        map.put("jwt",jwt);
		return map;
	}
	/*-----------------------------------------------Photos-------------------------------------------------------*/
	@PostMapping(path = "users/photos")
	public PhotoDto addUserPhoto(@ModelAttribute PhotoDto photoDto) throws Exception {
		PhotoDto pic=photoService.addUserPhoto(photoDto);
		pic.setPicData(null);
		return pic;
	}
	@GetMapping(path = "users/photos")
	public List<Photos> getUserPhotos(){
		return photoService.getUserPhotos(userId);		
	}
	@GetMapping(path = "users/home/photos")
	public List<Photos> getHomeScreenPhotos(){
		return photoService.getHomeScreenPhotos(userId);		
	}
	/*-----------------------------------------------Connections&Requests-------------------------------------------------------*/
	@GetMapping(path = "users/connections/followers")
	public List<UserDto> getUserFollowers(){
		return userService.getUserFollowers(userId);		
	}
	@GetMapping(path = "users/connections/followings")
	public List<UserDto> getUserFollowings(){
		return userService.getUserFolloiwngs(userId);		
	}
	
	@GetMapping(path="users/request/respond")
	public boolean acceptOrRejectRequest(@RequestParam(name = "requestId") int requestId,
			@RequestParam(name = "response") char response) throws Exception {
		return requestService.acceptOrRejectRequest(userId, requestId, response);
	}
	@GetMapping(path="users/request")
	public ResponseEntity<Object> getAllRequests(){
		return ResponseEntity.ok(requestService.getAllRequests(userId).stream()
				.map(RequestDto::genrateDto).collect(Collectors.toList()));
	}
	
	@GetMapping(path="users/request/create/")
	 public FollowRequests createRequest(@RequestParam(name = "requestedUser")int requestedUser)
			 throws Exception {
		int requestingUser=userId;
		return requestService.createRequest(requestedUser, requestingUser);
	 }

}
