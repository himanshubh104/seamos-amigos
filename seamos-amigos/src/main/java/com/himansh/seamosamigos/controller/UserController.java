package com.himansh.seamosamigos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.UserService;
import com.himansh.seamosamigos.utility.AmigosConstants;
import com.himansh.seamosamigos.utility.JwtUtility;
import com.himansh.seamosamigos.utility.AmigosUtils;

@RestController
@RequestMapping("api/seamos-amigos/")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtility jwtUtil;
	@Autowired
	private AmigosUtils utilities;
    @Autowired
    private AuthenticationManager authenticationManager;
	    
	//Register A User
	@PostMapping(path = "users/register",consumes = "application/JSON")
	public UserDto addUser(@RequestBody UserDto user) {
		return userService.addUser(user.generateEntity(),user.getRoles());	
	}
	
	//Login end point
	@PostMapping(path = "users/login",consumes = "application/JSON")
	public Map<String,Object> loginUser(@RequestBody UserDto user,
			@RequestParam(required = false) String forceLoginClienIp,
			HttpServletRequest request) throws InAppException {
		try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		String clientIp= utilities.extractClientIp(request);
		String userAgent = utilities.extractClientUserAgent(request);
		UserPrincipal userPrincipal=(UserPrincipal) userService.loadUserByUsername(user.getEmail());
		if (forceLoginClienIp != null) {
			userService.forceLogout(userPrincipal.getUserId(), forceLoginClienIp);
		}
		else if (userPrincipal.getActiveSessions() >= AmigosConstants.MAX_ACTIVE_SEESIONS) {
			throw new InAppException(AmigosConstants.LOGIN_ERROR+": User already logged in with: "+userPrincipal.getActiveSessions()+" active sessions.");
		}
		long loginId = userService.updateActiveSessions(userPrincipal.getUserId(), clientIp, userAgent);
		Map<String, Object> claims = new HashMap<>();
        claims.put("clientIp", clientIp);
        claims.put("loginId", loginId);
		String jwt=jwtUtil.generateToken(userPrincipal, claims);
		
		 Map<String,Object> map= new HashMap<>();
	        map.put("authenticated",true);
	        map.put("userId", userPrincipal.getUserId());
	        map.put("jwt",jwt);
		return map;
	}
	
	@PostMapping(path = "users/get-active-user-agents",consumes = "application/JSON")
	public List<Map<String, Object>> getActiveAgentsWithIp(@RequestBody UserDto user, HttpServletRequest request) throws InAppException {
		try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		UserPrincipal userPrincipal=(UserPrincipal) userService.loadUserByUsername(user.getEmail());
		return userService.getActiveAgentsWithIp(userPrincipal.getUserId());
	}
	
	@GetMapping("users/logout")
	public ResponseEntity<Object> logoutUser(HttpServletRequest request) {
		//Object response = true;
		//if (!userService.logoutUser()) {
			//response = "No active session found.";
		//}
		boolean resp = userService.logoutUser(utilities.extractClientIp(request));
		return ResponseEntity.ok(resp);
	}

}
