package com.himansh.seamosamigos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//This is similar to follow on instagram
@Entity(name="connections")
public class Connections {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int connectionId;
	@ManyToOne
	@JoinColumn(name = "user1_id")
	private UserEntity user1;
	@ManyToOne
	@JoinColumn(name = "user2_id")
	private UserEntity user2;
	
	@JsonManagedReference
	public UserEntity getUser2() {
		return user2;
	}
	public void setUser2(UserEntity user2) {
		this.user2 = user2;
	}
	public int getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	@JsonManagedReference
	public UserEntity getUser1() {
		return user1;
	}
	public void setUser1(UserEntity user1) {
		this.user1 = user1;
	}
	
}
