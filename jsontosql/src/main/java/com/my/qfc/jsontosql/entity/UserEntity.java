package com.my.qfc.jsontosql.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Component
@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double userid;
	private String username;
	private String useraddress;

	// Constructors, getters, and setters

	public UserEntity() {
		// Default constructor
	}

	public UserEntity(double userid, String username, String useraddress) {
		this.userid = userid;
		this.username = username;
		this.useraddress = useraddress;
	}

	// Generate getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getUserid() {
		return userid;
	}

	public void setUserid(double userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUseraddress() {
		return useraddress;
	}

	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}

	@Override
	public String toString() {
		return "UserVO{" + "id=" + id + ", userid=" + userid + ", username='" + username + '\'' + ", useraddress='"
				+ useraddress + '\'' + '}';
	}
}