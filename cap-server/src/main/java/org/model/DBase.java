package org.model;

public class DBase {
	
	private String dbID;
	private String dbDesc;
	private String dbPort;
	private String dbInstance;
	private String dbName;
	private String dbType;
	private String dbJDBCString;
	private String dbFileConf;
	private int dbEnable;
	
	public String getDbID() {
		return dbID;
	}
	public void setDbID(String dbID) {
		this.dbID = dbID;
	}
	public String getDbDesc() {
		return dbDesc;
	}
	public void setDbDesc(String dbDesc) {
		this.dbDesc = dbDesc;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbInstance() {
		return dbInstance;
	}
	public void setDbInstance(String dbInstance) {
		this.dbInstance = dbInstance;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDbJDBCString() {
		return dbJDBCString;
	}
	public void setDbJDBCString(String dbJDBCString) {
		this.dbJDBCString = dbJDBCString;
	}
	public String getDbFileConf() {
		return dbFileConf;
	}
	public void setDbFileConf(String dbFileConf) {
		this.dbFileConf = dbFileConf;
	}
	public int getDbEnable() {
		return dbEnable;
	}
	public void setDbEnable(int dbEnable) {
		this.dbEnable = dbEnable;
	}
}
