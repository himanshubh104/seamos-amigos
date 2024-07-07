package com.himansh.seamosamigos.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;

@Setter
@Getter
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

}
