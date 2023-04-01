package com.himansh.seamosamigos.controller;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.UserService;
import com.himansh.seamosamigos.utility.AmigosConstants;
import com.himansh.seamosamigos.utility.AmigosUtils;
import com.himansh.seamosamigos.utility.JwtUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/seamos-amigos/")
public class UserController {
	private final UserService userService;
	private final JwtUtility jwtUtil;
	private final AmigosUtils utilities;
    private final AuthenticationManager authenticationManager;

	public UserController(UserService userService, JwtUtility jwtUtil, AmigosUtils utilities, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.utilities = utilities;
		this.authenticationManager = authenticationManager;
	}

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
		Authentication authenticate = null;
		try {
			authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		String clientIp= utilities.extractClientIp(request);
		String userAgent = utilities.extractClientUserAgent(request);
		UserPrincipal userPrincipal=(UserPrincipal) authenticate.getPrincipal();
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
		Authentication authenticate = null;
		try {
			authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect Username or Password",e);
		}
		UserPrincipal userPrincipal=(UserPrincipal) authenticate.getPrincipal();
		return userService.getActiveAgentsWithIp(userPrincipal.getUserId());
	}
	
	@GetMapping("users/logout")
	public ResponseEntity<Object> logoutUser(HttpServletRequest request) {
		boolean resp = userService.logoutUser(utilities.extractClientIp(request));
		return ResponseEntity.ok(resp);
	}

}
