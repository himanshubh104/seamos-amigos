package com.himansh.seamosamigos.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.service.UserService;
import com.himansh.seamosamigos.utility.JwtUtility;
import com.himansh.seamosamigos.utility.Utilities;

@RestController
@RequestMapping("api/seamos-amigos/")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtility jwtUtil;
	@Autowired
	private Utilities utilities;
    @Autowired
    private AuthenticationManager authenticationManager;
	    
	//Register A User
	@PostMapping(path = "users/register",consumes = "application/JSON")
	public UserDto addUser(@RequestBody UserDto user) {
		return userService.addUser(user.generateEntity(),user.getRoles());	
	}
	
	//Login end point
	@PostMapping(path = "users/login",consumes = "application/JSON")
	public Map<String,Object> loginUser(@RequestBody UserDto user, HttpServletRequest request) {
		try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		String clientIp= utilities.getClientIp(request);
		UserPrincipal userPrincipal=(UserPrincipal) userService.loadUserByUsername(user.getEmail());
		String jwt=jwtUtil.generateToken(userPrincipal, clientIp);
		 Map<String,Object> map= new HashMap<>();
	        map.put("authenticated",true);
	        map.put("userId", userPrincipal.getUserId());
	        map.put("jwt",jwt);
		return map;
	}

}
