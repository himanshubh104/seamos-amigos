package com.himansh.seamosamigos.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "like_on_feed")
public class LikeOnFeed implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Integer likeId;

	@Column(name = "feed_id")
	private Integer feedId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "feed_type")
	private String feedType;
	
	public Integer getLikeId() {
		return likeId;
	}
	public void setLikeId(Integer likeId) {
		this.likeId = likeId;
	}
	public Integer getFeedId() {
		return feedId;
	}
	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFeedType() {
		return feedType;
	}
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	
}
