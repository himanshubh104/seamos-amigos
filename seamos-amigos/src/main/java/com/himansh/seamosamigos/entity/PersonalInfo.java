package com.himansh.seamosamigos.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "personalInfo")
public class PersonalInfo {
	@Id
	@Column(name = "user_id")
	private int userId;

	private String gender;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "phone_umber")
	private String phoneNumber;

	@Column(name = "personal_email")
	private String personalEmail;

	private String bio;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

}
