package com.himansh.seamosamigos.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="follow_request")
public class FollowRequests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int requestId;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "requested_user")
	private User requestedUser;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "requesting_user")
	private User requestingUser;
}
