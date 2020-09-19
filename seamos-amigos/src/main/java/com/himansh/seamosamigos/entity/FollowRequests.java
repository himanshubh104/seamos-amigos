package com.himansh.seamosamigos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="follow_request")
public class FollowRequests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestId;
	@ManyToOne
	@JoinColumn(name = "user1_id")
	private UserEntity user1;
	@ManyToOne
	@JoinColumn(name = "user2_id")
	private UserEntity user2;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	@JsonManagedReference
	public UserEntity getUser1() {
		return user1;
	}
	public void setUser1(UserEntity user1) {
		this.user1 = user1;
	}
	@JsonBackReference
	public UserEntity getUser2() {
		return user2;
	}
	public void setUser2(UserEntity user2) {
		this.user2 = user2;
	}
	
	
}
