package com.himansh.seamosamigos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.config.UserPrincipal;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.PersonalInfoEntity;
import com.himansh.seamosamigos.entity.Roles;
import com.himansh.seamosamigos.entity.UserEntity;
import com.himansh.seamosamigos.repository.PersonalInfoRepository;
import com.himansh.seamosamigos.repository.RoleRepository;
import com.himansh.seamosamigos.repository.UserRepository;

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
	private PasswordEncoder passwordEncoder;
	
	private List<Roles> getUserRoles(List<String> roles){
		return roles.stream().map(r->{
			return roleRepository.findByRoleName(r);
		}).collect(Collectors.toList());
	}
	
	//Register User
	public UserDto addUser(UserEntity user, List<String> roles) {
		user.setRoles(getUserRoles(roles));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity ue=userRepository.findByEmail(username);
		if (ue==null) {
			throw new UsernameNotFoundException("User Not Found!");
		}
		return new UserPrincipal(ue);
	}
}
