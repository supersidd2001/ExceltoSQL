package com.my.qfc.common.vo;

public class UserVO implements Cloneable {

	private double userid;
	private String username;
	private String useraddress;

	// Constructors, getters, and setters

	// Default constructor
	public UserVO() {
	}

	// Parameterized constructor
	public UserVO(double userid, String username, String useraddress) {
		this.userid = userid;
		this.username = username;
		this.useraddress = useraddress;
	}

	// Generate getters and setters

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

	// Clone method to create a deep copy
	@Override
	public UserVO clone() {
		try {
			return (UserVO) super.clone();
		} catch (CloneNotSupportedException e) {
			// Handle the exception as needed
			return null;
		}
	}

	@Override
	public String toString() {
		return "UserVO{" + "userid=" + userid + ", username='" + username + '\'' + ", useraddress='" + useraddress
				+ '\'' + '}';
	}
}
