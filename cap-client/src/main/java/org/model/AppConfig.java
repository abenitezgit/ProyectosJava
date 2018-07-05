package org.model;

public class AppConfig {
	//App Params 
	private int txpMain;
	private String connectTypeMon; 		//Primary: URL - SOCKET
	private String sConnectTypeMon; 	//Secondary: URL - SOCKET
	private String log4jName;
	private String log4jLevel;
	private String workPath;
	
	//Load from External Params
	private String srvID;
	private String pathConfig;
	private String fileConfig;
	
	//Socket Params
	private String monHostName;
	private String monIP;
	private String monPort;
	private String sMonHostName;
	private String sMonIP;
	private String sMonPort;
	private String authKey;
	
	//URL Params
	private String urlServer;
	private String urlPort;
	private String urlBase;
	private String sUrlServer;
	private String sUrlPort;
	private String sUrlBase;
	
	//Getter and Setter
	
	public String getMonHostName() {
		return monHostName;
	}
	public void setMonHostName(String monHostName) {
		this.monHostName = monHostName;
	}
	public String getMonIP() {
		return monIP;
	}
	public void setMonIP(String monIP) {
		this.monIP = monIP;
	}
	public String getMonPort() {
		return monPort;
	}
	public void setMonPort(String monPort) {
		this.monPort = monPort;
	}
	public String getsMonHostName() {
		return sMonHostName;
	}
	public void setsMonHostName(String sMonHostName) {
		this.sMonHostName = sMonHostName;
	}
	public String getsMonIP() {
		return sMonIP;
	}
	public void setsMonIP(String sMonIP) {
		this.sMonIP = sMonIP;
	}
	public String getsMonPort() {
		return sMonPort;
	}
	public void setsMonPort(String sMonPort) {
		this.sMonPort = sMonPort;
	}
	public String getConnectTypeMon() {
		return connectTypeMon;
	}
	public void setConnectTypeMon(String connectTypeMon) {
		this.connectTypeMon = connectTypeMon;
	}
	public String getsConnectTypeMon() {
		return sConnectTypeMon;
	}
	public void setsConnectTypeMon(String sConnectTypeMon) {
		this.sConnectTypeMon = sConnectTypeMon;
	}
	public String getUrlServer() {
		return urlServer;
	}
	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}
	public String getUrlPort() {
		return urlPort;
	}
	public void setUrlPort(String urlPort) {
		this.urlPort = urlPort;
	}
	public String getUrlBase() {
		return urlBase;
	}
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
	public String getsUrlServer() {
		return sUrlServer;
	}
	public void setsUrlServer(String sUrlServer) {
		this.sUrlServer = sUrlServer;
	}
	public String getsUrlPort() {
		return sUrlPort;
	}
	public void setsUrlPort(String sUrlPort) {
		this.sUrlPort = sUrlPort;
	}
	public String getsUrlBase() {
		return sUrlBase;
	}
	public void setsUrlBase(String sUrlBase) {
		this.sUrlBase = sUrlBase;
	}
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
	public String getSrvID() {
		return srvID;
	}
	public void setSrvID(String srvID) {
		this.srvID = srvID;
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
	public String getWorkPath() {
		return workPath;
	}
	public void setWorkPath(String workPath) {
		this.workPath = workPath;
	}
}
