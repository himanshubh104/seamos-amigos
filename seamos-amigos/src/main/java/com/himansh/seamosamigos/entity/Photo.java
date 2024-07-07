package com.himansh.seamosamigos.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "photos")
public class Photo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private int photoId;

	private String url;

	private String caption;

	// @Formula("(select count(1) from like_on_feed lof where lof.feed_id = photo_id)")
	private int likes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_upload")
	private Date dateOfUpload;

	//Why A single photo have many-to-many mapping with users?
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_photos",
			joinColumns = @JoinColumn(name="photo_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
			)
	private List<User> users;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "photo_id")
	private Set<Comment> comments;
}
