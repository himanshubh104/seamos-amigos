package com.himansh.seamosamigos.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "login_session")
public class LoginSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long loginId;
	private int userId;
	private String userIp;
	private String userAgent;
	@Temporal(value = TemporalType.TIMESTAMP)
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
