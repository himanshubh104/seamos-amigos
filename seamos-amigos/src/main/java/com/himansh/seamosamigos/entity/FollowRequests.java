package com.himansh.seamosamigos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="follow_request")
public class FollowRequests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestId;
	@ManyToOne
	@JoinColumn(name = "requestedUser")
	private UserEntity requestedUser;
	@ManyToOne
	@JoinColumn(name = "requestingUser")
	private UserEntity requestingUser;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	@JsonBackReference
	public UserEntity getRequestedUser() {
		return requestedUser;
	}
	public void setRequestedUser(UserEntity requestedUser) {
		this.requestedUser = requestedUser;
	}
	
	@JsonBackReference
	public UserEntity getRequestingUser() {
		return requestingUser;
	}
	public void setRequestingUser(UserEntity requestingUser) {
		this.requestingUser = requestingUser;
	}
	
}
