package org.model;

public class Ftp {
	
	private String ftpID;
	private String ftpDesc;
	private int enable;
	private int ftpSecure;
	private String remoteFile;
	private String localFile;
	private String remotePath;
	private String localPath;
	private String serverID;
	private String userID;
	private String serverIP;
	private String userName;
	private String userPass;
	
	//Getter and setter
	
	public String getFtpID() {
		return ftpID;
	}
	public void setFtpID(String ftpID) {
		this.ftpID = ftpID;
	}
	public String getFtpDesc() {
		return ftpDesc;
	}
	public void setFtpDesc(String ftpDesc) {
		this.ftpDesc = ftpDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public int getFtpSecure() {
		return ftpSecure;
	}
	public void setFtpSecure(int ftpSecure) {
		this.ftpSecure = ftpSecure;
	}
	public String getRemoteFile() {
		return remoteFile;
	}
	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}
	public String getLocalFile() {
		return localFile;
	}
	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

}
