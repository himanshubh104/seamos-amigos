package com.himansh.seamosamigos.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//This is similar to follow on instagram
@Entity(name="connections")
public class Connections {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "connection_id")
	private int connectionId;
	@ManyToOne
	@JoinColumn(name = "user1_id")
	private User user1;
	@ManyToOne
	@JoinColumn(name = "user2_id")
	private User user2;
	
	@JsonManagedReference
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	public int getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	@JsonManagedReference
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	
}
