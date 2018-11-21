package cap.model;

public class Config {
	String monHostName;
	String monIP;
	String monPort;
	String sMonHostName;
	String sMonIP;
	String sMonPort;
	
	String connectTypeMon; 		//Primary: URL - SOCKET
	String sConnectTypeMon; 	//Secondary: URL - SOCKET
	
	//URL Params
	String urlServer;
	String urlPort;
	String urlBase;

	String sUrlServer;
	String sUrlPort;
	String sUrlBase;
	
	//MetaData DB
	String dbHostName;
	String dbIP;
	String dbName;
	String dbPort;
	String dbUser;
	String dbPass;
	int dbTimeOut;
	String dbType;
	
	//Getter and Setter
	
	public String getMonHostName() {
		return monHostName;
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
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
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
}
