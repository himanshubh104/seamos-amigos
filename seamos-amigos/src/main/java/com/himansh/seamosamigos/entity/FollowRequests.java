package com.himansh.seamosamigos.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="follow_request")
public class FollowRequests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int requestId;
	@ManyToOne
	@JoinColumn(name = "requested_user")
	private User requestedUser;
	@ManyToOne
	@JoinColumn(name = "requesting_user")
	private User requestingUser;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	@JsonBackReference
	public User getRequestedUser() {
		return requestedUser;
	}
	public void setRequestedUser(User requestedUser) {
		this.requestedUser = requestedUser;
	}
	
	@JsonBackReference
	public User getRequestingUser() {
		return requestingUser;
	}
	public void setRequestingUser(User requestingUser) {
		this.requestingUser = requestingUser;
	}
	
}
