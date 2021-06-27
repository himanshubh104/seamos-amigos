package com.himansh.seamosamigos.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Roles;
import com.himansh.seamosamigos.entity.User;

public class UserDto {
	private int userId;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private List<String> roles;
	
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
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
