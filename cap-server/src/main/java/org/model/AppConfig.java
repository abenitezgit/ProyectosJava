package org.model;

public class AppConfig {
	//App Params 
	private int txpMain;
	private String log4jName;
	private String log4jLevel;
	private String authKey;
	
	//Load from External Params
	private String monID;
	private String pathConfig;
	private String fileConfig;
	
	//DB Params
	private String dbHostName;
	private String dbIP;
	private String dbName;
	private String dbPort;
	private String dbUser;
	private String dbPass;
	private int dbTimeOut;
	
	//Getter and Setter
	
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getMonID() {
		return monID;
	}
	public void setMonID(String monID) {
		this.monID = monID;
	}
	public String getPathConfig() {
		return pathConfig;
	}
	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}
	public String getFileConfig() {
		return fileConfig;
	}
	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}
	public String getLog4jName() {
		return log4jName;
	}
	public void setLog4jName(String log4jName) {
		this.log4jName = log4jName;
	}
	public String getLog4jLevel() {
		return log4jLevel;
	}
	public void setLog4jLevel(String log4jLevel) {
		this.log4jLevel = log4jLevel;
	}
	public String getDbHostName() {
		return dbHostName;
	}
	public void setDbHostName(String dbHostName) {
		this.dbHostName = dbHostName;
	}
	public String getDbIP() {
		return dbIP;
	}
	public void setDbIP(String dbIP) {
		this.dbIP = dbIP;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPass() {
		return dbPass;
	}
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	public int getDbTimeOut() {
		return dbTimeOut;
	}
	public void setDbTimeOut(int dbTimeOut) {
		this.dbTimeOut = dbTimeOut;
	}
}
