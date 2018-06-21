package org.model;

public class Config {
	String monHostName;
	String monIP;
	String monPort;
	String sMonHostName;
	String sMonIP;
	String sMonPort;
	int txpMain;
	String authKey;
	
	String connectTypeMon; 		//Primary: URL - SOCKET
	String sConnectTypeMon; 	//Secondary: URL - SOCKET
	
	//URL Params
	String urlServer;
	String urlPort;
	String urlBase;

	String sUrlServer;
	String sUrlPort;
	String sUrlBase;
	
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
}
