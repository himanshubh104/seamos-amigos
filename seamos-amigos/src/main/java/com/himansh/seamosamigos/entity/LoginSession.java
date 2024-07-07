package com.himansh.seamosamigos.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "login_session")
public class LoginSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_id")
	private long loginId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "user_ip")
	private String userIp;

	@Column(name = "user_agent")
	private String userAgent;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "login_time")
	private Date loginTime = new Date();

}
