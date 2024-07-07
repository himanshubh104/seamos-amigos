package com.himansh.seamosamigos.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Roles;
import com.himansh.seamosamigos.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	private int userId;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private List<String> roles;
	
	public User generateEntity() {
		User ue=new User();
		ue.setFirstName(firstName);
		ue.setLastName(lastName);
		ue.setPassword(password);
		ue.setEmail(email);
		
		ue.setRoles(roles.stream().map(r->{
			Roles role=new Roles();
			role.setRoleName(r);
			return role;
		}).collect(Collectors.toList()));
		return ue;		
	}
	
	public static UserDto generateDto(User ue) {
		UserDto ud=new UserDto();
		ud.setUserId(ue.getUserId());
		ud.setFirstName(ue.getFirstName());
		ud.setLastName(ue.getLastName());
		ud.setEmail(ue.getEmail());
		//ud.setRoles(ue.getRoles().stream().map(Roles::getRoleName).collect(Collectors.toList()));
		//ud.setPassword(ue.getPassword());
		return ud;
	}
}
