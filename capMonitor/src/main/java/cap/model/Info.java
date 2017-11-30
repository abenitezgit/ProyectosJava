package cap.model;

public class Info {
	//Parametros del servicio Monitor
	String monRol;
	String monID;
	String monHostName;
	String monIP;
	int monPort;
	String SmonID;
	String SmonHostName;
	String SmonIP;
	int SmonPort;
	String connectType;
	String dbHostName;
	String dbIP;
	String dbName;
	int dbPort;
	String dbUser;
	String dbPass;
	int dbTimeOut;
	String dbType;
	String apiIP;
	String apiPort;
	String apiURLBase;
		
	//Parametros de control de tiempos de ejecucion
	int txpMain;
	int txpSync;
	int txpIns;
	int txpDB;
	
	//Parametros de recuperacion de agendas
	int ageShowHour;
	int ageGapMinute;
	
	//Parametro de intercambio de key
	String authKey;
	
	//Parametros de archivos de properties y log
	String pathProperties;
	String fileProperties;
	String logProperties;
	
	//Getter and Setter
	
	public String getMonHostName() {
		return monHostName;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public int getDbTimeOut() {
		return dbTimeOut;
	}
	public void setDbTimeOut(int dbTimeOut) {
		this.dbTimeOut = dbTimeOut;
	}
	public String getMonRol() {
		return monRol;
	}
	public void setMonRol(String monRol) {
		this.monRol = monRol;
	}
	public String getMonID() {
		return monID;
	}
	public void setMonID(String monID) {
		this.monID = monID;
	}
	public String getSmonID() {
		return SmonID;
	}
	public void setSmonID(String smonID) {
		SmonID = smonID;
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
	public int getMonPort() {
		return monPort;
	}
	public void setMonPort(int monPort) {
		this.monPort = monPort;
	}
	public String getSmonHostName() {
		return SmonHostName;
	}
	public void setSmonHostName(String smonHostName) {
		SmonHostName = smonHostName;
	}
	public String getSmonIP() {
		return SmonIP;
	}
	public void setSmonIP(String smonIP) {
		SmonIP = smonIP;
	}
	public int getSmonPort() {
		return SmonPort;
	}
	public void setSmonPort(int smonPort) {
		SmonPort = smonPort;
	}
	public String getConnectType() {
		return connectType;
	}
	public void setConnectType(String connectType) {
		this.connectType = connectType;
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
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
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
	public String getApiIP() {
		return apiIP;
	}
	public void setApiIP(String apiIP) {
		this.apiIP = apiIP;
	}
	public String getApiPort() {
		return apiPort;
	}
	public void setApiPort(String apiPort) {
		this.apiPort = apiPort;
	}
	public String getApiURLBase() {
		return apiURLBase;
	}
	public void setApiURLBase(String apiURLBase) {
		this.apiURLBase = apiURLBase;
	}
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public int getTxpSync() {
		return txpSync;
	}
	public void setTxpSync(int txpSync) {
		this.txpSync = txpSync;
	}
	public int getTxpIns() {
		return txpIns;
	}
	public void setTxpIns(int txpIns) {
		this.txpIns = txpIns;
	}
	public int getTxpDB() {
		return txpDB;
	}
	public void setTxpDB(int txpDB) {
		this.txpDB = txpDB;
	}
	public int getAgeShowHour() {
		return ageShowHour;
	}
	public void setAgeShowHour(int ageShowHour) {
		this.ageShowHour = ageShowHour;
	}
	public int getAgeGapMinute() {
		return ageGapMinute;
	}
	public void setAgeGapMinute(int ageGapMinute) {
		this.ageGapMinute = ageGapMinute;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getPathProperties() {
		return pathProperties;
	}
	public void setPathProperties(String pathProperties) {
		this.pathProperties = pathProperties;
	}
	public String getFileProperties() {
		return fileProperties;
	}
	public void setFileProperties(String fileProperties) {
		this.fileProperties = fileProperties;
	}
	public String getLogProperties() {
		return logProperties;
	}
	public void setLogProperties(String logProperties) {
		this.logProperties = logProperties;
	}
	
}
