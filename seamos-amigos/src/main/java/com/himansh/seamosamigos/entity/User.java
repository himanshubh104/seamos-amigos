package com.himansh.seamosamigos.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "users") //{if we use this then in in repository we have to use the actual class name}
//@SequenceGenerator(name = "user_id_seq",initialValue = 1000,allocationSize = 1)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String firstName;
	private String lastName;
	private String password;
	private int activeSessions;
	@Column(unique = true, nullable = false,length = 100)
	private String email;
	@ManyToMany(mappedBy = "users")
	private List<Photo> photos;
	
	@OneToMany(mappedBy = "user1")
	private List<Connections> connections;
	
	@OneToMany(mappedBy = "requestedUser")
	private List<FollowRequests> requests;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    		joinColumns = @JoinColumn(name="userId"),
    		inverseJoinColumns = @JoinColumn(name="roleId")
    		)
    private List<Roles> roles;
    @OneToMany
    private List<LoginSession> loginSessions;
    
    
	public List<LoginSession> getLoginSessions() {
		return loginSessions;
	}
	public void setLoginSessions(List<LoginSession> loginSessions) {
		this.loginSessions = loginSessions;
	}
	public List<Roles> getRoles() {
		return roles;
	}
	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	@JsonBackReference
	public List<FollowRequests> getRequests() {
		return requests;
	}
	public void setRequests(List<FollowRequests> requests) {
		this.requests = requests;
	}
	@JsonBackReference
	public List<Connections> getConnections() {
		return connections;
	}
	public void setConnections(List<Connections> connections) {
		this.connections = connections;
	}
	@JsonBackReference
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
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
	public int getActiveSessions() {
		return activeSessions;
	}
	public void setActiveSessions(int activeSessions) {
		this.activeSessions = activeSessions;
	}
	
}
