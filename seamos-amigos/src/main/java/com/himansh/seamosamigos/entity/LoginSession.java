package com.himansh.seamosamigos.entity;

import java.util.Date;

import javax.persistence.*;

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
	
	public long getLoginId() {
		return loginId;
	}
	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}
