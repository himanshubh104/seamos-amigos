package com.himansh.seamosamigos.dto;

import com.himansh.seamosamigos.entity.UserEntity;

public class UserDto {
	private int userId;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
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
	
	public UserEntity generateEntity() {
		UserEntity ue=new UserEntity();
		ue.setFirstName(firstName);
		ue.setLastName(lastName);
		ue.setPassword(password);
		ue.setEmail(email);
		return ue;		
	}
	
	public static UserDto generateDto(UserEntity ue) {
		UserDto ud=new UserDto();
		ud.setUserId(ue.getUserId());
		ud.setFirstName(ue.getFirstName());
		ud.setLastName(ue.getLastName());
		ud.setEmail(ue.getEmail());
		ud.setPassword(ue.getPassword());
		return ud;
	}
}
