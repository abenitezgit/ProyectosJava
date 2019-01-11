package org.model;

public class User {
	
	private String userID;
	private String userName;
	private String userType;
	private String userPass;
	private String userDomain;
	private String userCliID;
	private int userEnable;
	private String userDesc;
	
	//Getter and Setter
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserDomain() {
		return userDomain;
	}
	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}
	public String getUserCliID() {
		return userCliID;
	}
	public void setUserCliID(String userCliID) {
		this.userCliID = userCliID;
	}
	public int getUserEnable() {
		return userEnable;
	}
	public void setUserEnable(int userEnable) {
		this.userEnable = userEnable;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

}
