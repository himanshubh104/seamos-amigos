package com.himansh.seamosamigos.service;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.LoginSession;
import com.himansh.seamosamigos.entity.PersonalInfo;
import com.himansh.seamosamigos.entity.Roles;
import com.himansh.seamosamigos.entity.User;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.exception.ValidationException;
import com.himansh.seamosamigos.repository.LoginSessionRepository;
import com.himansh.seamosamigos.repository.PersonalInfoRepository;
import com.himansh.seamosamigos.repository.RoleRepository;
import com.himansh.seamosamigos.repository.UserRepository;
import com.himansh.seamosamigos.Constants.AmigosConstants;
import com.himansh.seamosamigos.utility.AmigosUtils;
import com.himansh.seamosamigos.utility.CurrentUser;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{

	private final UserRepository userRepository;
	private final PersonalInfoRepository infoRepository;
	private final RoleRepository roleRepository;
	private final LoginSessionRepository loginSessionRepo;
	private final PasswordEncoder passwordEncoder;
	private final AmigosUtils utils;
	private final Logger log = LoggerFactory.getLogger(UserService.class);

	public UserService(UserRepository userRepository, PersonalInfoRepository infoRepository, RoleRepository roleRepository,
					   LoginSessionRepository loginSessionRepo, PasswordEncoder passwordEncoder, AmigosUtils utils) {
		this.userRepository = userRepository;
		this.infoRepository = infoRepository;
		this.roleRepository = roleRepository;
		this.loginSessionRepo = loginSessionRepo;
		this.passwordEncoder = passwordEncoder;
		this.utils = utils;
	}

	private List<Roles> getUserRoles(List<String> roles){
		return roleRepository.findAllByRoleNames(roles);
	}
	
	//Register User
	public UserDto addUser(User user, List<String> roles) throws ValidationException {
		if (StringUtil.isEmpty(user.getEmail())) {
			throw new ValidationException("User email is mandatory.");
		}
		boolean isUserAlreadyExists = Optional.ofNullable(userRepository.findByEmail(user.getEmail())).isEmpty();
		if (isUserAlreadyExists) {
			throw new ValidationException("User Already exists with same email.");
		}
		user.setRoles(getUserRoles(roles));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return UserDto.generateDto(userRepository.save(user));
	}
	
	public PersonalInfo getUserInfo(int userId) {
		return infoRepository.findById(userId).get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Loading details of user:{}", username);
		User ue=userRepository.findByEmail(username);
		if (ue==null) {
			throw new UsernameNotFoundException("User Not Found.");
		}
		return new UserPrincipal(ue);
	}
	
	public long updateActiveSessions(Integer userId, String clientIp, String userAgent) {
		LoginSession session = loginSessionRepo.getByUserIdAndUserIp(userId, clientIp);
		if (session != null) {
			log.info("User already logged in with same IP. Overwriting previous login..");
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

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getActiveAgentsWithIp(int userId) {
		var loginDetails = loginSessionRepo.getUserIpAndAgents(userId);
		var ipList = loginDetails.map(d->{
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

	@javax.transaction.Transactional
	public long initUserLogin(UserPrincipal userPrincipal, String clientIp, String userAgent, String forceLoginClienIp) throws InAppException {
		if (forceLoginClienIp != null) {
			forceLogout(userPrincipal.getUserId(), forceLoginClienIp);
		}
		else if (userPrincipal.getActiveSessions() >= AmigosConstants.MAX_ACTIVE_SEESIONS) {
			throw new InAppException(AmigosConstants.LOGIN_ERROR+": User already logged in with: "+userPrincipal.getActiveSessions()+" active sessions.");
		}
		return updateActiveSessions(userPrincipal.getUserId(), clientIp, userAgent);
	}
}
