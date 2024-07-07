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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users") //{if we use this then in repository we have to use the actual class name}
//@SequenceGenerator(name = "user_id_seq",initialValue = 1000,allocationSize = 1)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = true)
	private int userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String password;
	@Column(name = "active_sessions")
	private int activeSessions;

	@Column(unique = true, nullable = false,length = 100)
	private String email;

	@JsonBackReference
	@ManyToMany(mappedBy = "users")
	private List<Photo> photos;

	@JsonBackReference
	@OneToMany(mappedBy = "user1")
	private List<Connections> connections;

	@JsonBackReference
	@OneToMany(mappedBy = "requestedUser")
	private List<FollowRequests> requests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    		joinColumns = @JoinColumn(name="user_id"),
    		inverseJoinColumns = @JoinColumn(name="role_id")
    		)
    private List<Roles> roles;
    @OneToMany
    private List<LoginSession> loginSessions;
	
}
