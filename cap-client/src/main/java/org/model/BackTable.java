package org.model;

public class BackTable {
	private String btbID;
	private String btbDesc;
	private int enable;
	private String btbTableName;
	private String btbFileName;
	private String btbFilePath;
	private int btbWhere_active;
	private String btbWhere_body;
	private String cliID;
	private String serverID;
	private String dbID;
	private String loginID;
	private String ownerID;
	
	public String getBtbID() {
		return btbID;
	}
	public void setBtbID(String btbID) {
		this.btbID = btbID;
	}
	public String getBtbDesc() {
		return btbDesc;
	}
	public void setBtbDesc(String btbDesc) {
		this.btbDesc = btbDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getBtbTableName() {
		return btbTableName;
	}
	public void setBtbTableName(String btbTableName) {
		this.btbTableName = btbTableName;
	}
	public String getBtbFileName() {
		return btbFileName;
	}
	public void setBtbFileName(String btbFileName) {
		this.btbFileName = btbFileName;
	}
	public String getBtbFilePath() {
		return btbFilePath;
	}
	public void setBtbFilePath(String btbFilePath) {
		this.btbFilePath = btbFilePath;
	}
	public int getBtbWhere_active() {
		return btbWhere_active;
	}
	public void setBtbWhere_active(int btbWhere_active) {
		this.btbWhere_active = btbWhere_active;
	}
	public String getBtbWhere_body() {
		return btbWhere_body;
	}
	public void setBtbWhere_body(String btbWhere_body) {
		this.btbWhere_body = btbWhere_body;
	}
	public String getCliID() {
		return cliID;
	}
	public void setCliID(String cliID) {
		this.cliID = cliID;
	}
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public String getDbID() {
		return dbID;
	}
	public void setDbID(String dbID) {
		this.dbID = dbID;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
}
