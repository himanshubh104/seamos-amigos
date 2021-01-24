package com.himansh.seamosamigos.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "comments")
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId;
	private String body;
	private Integer likes;
	private Integer userId;
	private Integer photoId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "replyId")
	private Set<Comments> replies;
	
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Set<Comments> getReplies() {
		return replies;
	}
	public void setReplies(Set<Comments> replies) {
		this.replies = replies;
	}
	public Integer getCommentId() {
		return commentId;
	}
	
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public Integer getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result + ((photoId == null) ? 0 : photoId.hashCode());
		result = prime * result + ((replies == null) ? 0 : replies.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		Comments other = (Comments) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (photoId == null) {
			if (other.photoId != null)
				return false;
		} else if (!photoId.equals(other.photoId))
			return false;
		if (replies == null) {
			if (other.replies != null)
				return false;
		} else if (!replies.equals(other.replies))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
}
