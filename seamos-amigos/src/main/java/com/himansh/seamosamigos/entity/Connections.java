package com.himansh.seamosamigos.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

//This is similar to follow on instagram
@Getter
@Setter
@Entity(name="connections")
public class Connections {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "connection_id")
	private int connectionId;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "follower_id")
	private User user1;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "following_id")
	private User user2;
}
