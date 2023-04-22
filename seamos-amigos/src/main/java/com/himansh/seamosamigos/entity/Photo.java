package com.himansh.seamosamigos.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "photos")
public class Photo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private int photoId;
	private String url;
	private String caption;
	@Formula("(select count(1) from like_on_feed lof where lof.feed_id = photo_id)")
	private int likes;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_upload")
	private Date dateOfUpload;
	//Why A single photo have many-to-many mapping with users?
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_photos",
			joinColumns = @JoinColumn(name="photo_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
			)
	private List<User> users;
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((dateOfUpload == null) ? 0 : dateOfUpload.hashCode());
		result = prime * result + likes;
		result = prime * result + photoId;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dateOfUpload == null) {
			if (other.dateOfUpload != null)
				return false;
		} else if (!dateOfUpload.equals(other.dateOfUpload))
			return false;
		if (likes != other.likes)
			return false;
		if (photoId != other.photoId)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	
	
}
