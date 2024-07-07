package com.himansh.seamosamigos.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode(exclude = "replies")
@Entity
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "comment_id")
	private Integer commentId;

	private String body;

	// @Formula("(select count(1) from like_on_feed lof where lof.feed_id = comment_id)")
	private Integer likes;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "photo_id")
	private Integer photoId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@Column(name = "reply_id")
	private Integer replyId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "reply_id")
	private Set<Comment> replies;
	
}
