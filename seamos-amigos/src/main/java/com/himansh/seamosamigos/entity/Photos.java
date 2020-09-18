package com.himansh.seamosamigos.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "photos")
public class Photos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int photoId;
	private String url;
	private String caption;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfUpload;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_photos",
			joinColumns = @JoinColumn(name="photoId"),
			inverseJoinColumns = @JoinColumn(name = "userId")
			)
	private List<UserEntity> users;
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public Date getDateOfUpload() {
		return dateOfUpload;
	}
	public void setDateOfUpload(Date dateOfUpload) {
		this.dateOfUpload = dateOfUpload;
	}
	
	@JsonBackReference
	public List<UserEntity> getUsers() {
		return users;
	}
	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
	
	
}
