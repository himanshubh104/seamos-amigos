package com.himansh.seamosamigos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="connection")
public class Connection {
	@Id
	@GeneratedValue
	private int connectionId;
	@ManyToOne
	private UserEntity user1;
	private int user2Id;
	public int getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	@JsonBackReference
	public UserEntity getUser1() {
		return user1;
	}
	public void setUser1(UserEntity user1) {
		this.user1 = user1;
	}
	public int getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(int user2Id) {
		this.user2Id = user2Id;
	}
	
	
}
