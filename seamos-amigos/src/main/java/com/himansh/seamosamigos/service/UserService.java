package com.himansh.seamosamigos.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.LoginSession;
import com.himansh.seamosamigos.entity.PersonalInfoEntity;
import com.himansh.seamosamigos.entity.Roles;
import com.himansh.seamosamigos.entity.User;
import com.himansh.seamosamigos.repository.LoginSessionRepository;
import com.himansh.seamosamigos.repository.PersonalInfoRepository;
import com.himansh.seamosamigos.repository.RoleRepository;
import com.himansh.seamosamigos.repository.UserRepository;
import com.himansh.seamosamigos.utility.AmigosUtils;
import com.himansh.seamosamigos.utility.CurrentUser;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	@Qualifier("personalInfoRepository")
	private PersonalInfoRepository infoRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoginSessionRepository loginSessionRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AmigosUtils utils;
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	private List<Roles> getUserRoles(List<String> roles){
		return roles.stream().map(r->{
			return roleRepository.findByRoleName(r);
		}).collect(Collectors.toList());
	}
	
	//Register User
	public UserDto addUser(User user, List<String> roles) {
		user.setRoles(getUserRoles(roles));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return UserDto.generateDto(userRepository.saveAndFlush(user));	
	}
	
	public PersonalInfoEntity getUserInfo(int userId) {
		return infoRepository.findById(userId).get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User ue=userRepository.findByEmail(username);
		if (ue==null) {
			throw new UsernameNotFoundException("User Not Found!");
		}
		return new UserPrincipal(ue);
	}
	
	public long updateActiveSessions(Integer userId, String clientIp, String userAgent) {
		LoginSession session = loginSessionRepo.getByUserIdAndUserIp(userId, clientIp);
		if (session != null) {
			log.info("User already logged in with same IP. Overwritting previois login..");
			session.setLoginTime(new Date());
		}
		else {
			session = new LoginSession();
			session.setUserId(userId);
			session.setUserIp(clientIp);
			session.setUserAgent(userAgent);
		}
		userRepository.addLoginSession(userId);
		return loginSessionRepo.saveAndFlush(session).getLoginId();
	}
	
	public boolean forceLogout(int userId, String clientIp) {
		User user = userRepository.getOne(userId);
		int activeSessions = user.getActiveSessions();
		if (activeSessions == 0)
			return false;
		user.setActiveSessions(activeSessions-1);
		userRepository.saveAndFlush(user);
		loginSessionRepo.deleteByUserIp(clientIp);
		return true;
	}
	
	public boolean logoutUser(String clientIp) {
		User user = userRepository.getOne(CurrentUser.getCurrentUserId());
		int activeSessions = user.getActiveSessions();
		if (activeSessions == 0)
			return false;
		user.setActiveSessions(activeSessions-1);
		userRepository.saveAndFlush(user);
		loginSessionRepo.deleteByUserIp(clientIp);
		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		auth.setAuthenticated(false);
		return true;
	}

	public List<Map<String, Object>> getActiveAgentsWithIp(int userId) {
		var loginDetails = loginSessionRepo.getUserIpAndAgents(userId);
		var ipList = loginDetails.stream().map(d->{
			Map<String, Object> ipObj = new HashMap<>();
			var ip = d.get(0, String.class);
			var agent = d.get(1, String.class);
			var timeStamp = d.get(2, Date.class);
			ipObj.put("ip", ip);
			ipObj.put("agent", agent);
			ipObj.put("time_stamp", utils.dateToString(timeStamp));
			return ipObj;
		}).collect(Collectors.toList());
		return ipList;
	}
	
	public int getLoginDetails(Long loginId) {
		return loginSessionRepo.countByLoginId(loginId);
	}
}
