package org.model;

import java.util.Map;
import java.util.TreeMap;

public class LoadTable {
	private String ltbID;
	private String ltbDesc;
	private int enable;
	private String ltbFileName;
	private String ltbFileType;
	private String ltbFilePath;
	private int ltbLoadFixed;
	private String ltbFileSep;
	private String ltbTableName;
	private int ltbAppend;
	private int lbtHeader;
	private int ltbMaxRows;
	private int deleteWhereActive;
	private String deleteWhereBody;
	private String cliID;
	private String serverID;
	private String dbID;
	private String dbType;
	private String loginID;
	private String ownerID;
	private int ftpEnable;
	private int ftpSecure;
	private String ftpRemotePath;
	private String ftpServerID;
	private String ftpUserID;
	private String serverIP;
	private String loginName;
	private String loginPass;
	private String ownerName;
	private String ownerPass;
	private String dbName;
	private String dbPort;
	private String ftpServerIP;
	private String ftpUserName;
	private String ftpUserPass;
	
	private Map<Integer, LoadTableParam> mapLtbParam = new TreeMap<>();
	
	public int getLtbLoadFixed() {
		return ltbLoadFixed;
	}
	public void setLtbLoadFixed(int ltbLoadFixed) {
		this.ltbLoadFixed = ltbLoadFixed;
	}
	public String getLtbTableName() {
		return ltbTableName;
	}
	public void setLtbTableName(String ltbTableName) {
		this.ltbTableName = ltbTableName;
	}
	public Map<Integer, LoadTableParam> getMapLtbParam() {
		return mapLtbParam;
	}
	public void setMapLtbParam(Map<Integer, LoadTableParam> mapLtbParam) {
		this.mapLtbParam = mapLtbParam;
	}
	public int getDeleteWhereActive() {
		return deleteWhereActive;
	}
	public void setDeleteWhereActive(int deleteWhereActive) {
		this.deleteWhereActive = deleteWhereActive;
	}
	public String getDeleteWhereBody() {
		return deleteWhereBody;
	}
	public void setDeleteWhereBody(String deleteWhereBody) {
		this.deleteWhereBody = deleteWhereBody;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerPass() {
		return ownerPass;
	}
	public void setOwnerPass(String ownerPass) {
		this.ownerPass = ownerPass;
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
	public String getFtpServerIP() {
		return ftpServerIP;
	}
	public void setFtpServerIP(String ftpServerIP) {
		this.ftpServerIP = ftpServerIP;
	}
	public String getFtpUserName() {
		return ftpUserName;
	}
	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}
	public String getFtpUserPass() {
		return ftpUserPass;
	}
	public void setFtpUserPass(String ftpUserPass) {
		this.ftpUserPass = ftpUserPass;
	}
	public String getLtbID() {
		return ltbID;
	}
	public void setLtbID(String ltbID) {
		this.ltbID = ltbID;
	}
	public String getLtbDesc() {
		return ltbDesc;
	}
	public void setLtbDesc(String ltbDesc) {
		this.ltbDesc = ltbDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getLtbFileName() {
		return ltbFileName;
	}
	public void setLtbFileName(String ltbFileName) {
		this.ltbFileName = ltbFileName;
	}
	public String getLtbFileType() {
		return ltbFileType;
	}
	public void setLtbFileType(String ltbFileType) {
		this.ltbFileType = ltbFileType;
	}
	public String getLtbFilePath() {
		return ltbFilePath;
	}
	public void setLtbFilePath(String ltbFilePath) {
		this.ltbFilePath = ltbFilePath;
	}
	public String getLtbFileSep() {
		return ltbFileSep;
	}
	public void setLtbFileSep(String ltbFileSep) {
		this.ltbFileSep = ltbFileSep;
	}
	public int getLtbAppend() {
		return ltbAppend;
	}
	public void setLtbAppend(int ltbAppend) {
		this.ltbAppend = ltbAppend;
	}
	public int getLbtHeader() {
		return lbtHeader;
	}
	public void setLbtHeader(int lbtHeader) {
		this.lbtHeader = lbtHeader;
	}
	public int getLtbMaxRows() {
		return ltbMaxRows;
	}
	public void setLtbMaxRows(int ltbMaxRows) {
		this.ltbMaxRows = ltbMaxRows;
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
	public int getFtpEnable() {
		return ftpEnable;
	}
	public void setFtpEnable(int ftpEnable) {
		this.ftpEnable = ftpEnable;
	}
	public int getFtpSecure() {
		return ftpSecure;
	}
	public void setFtpSecure(int ftpSecure) {
		this.ftpSecure = ftpSecure;
	}
	public String getFtpRemotePath() {
		return ftpRemotePath;
	}
	public void setFtpRemotePath(String ftpRemotePath) {
		this.ftpRemotePath = ftpRemotePath;
	}
	public String getFtpServerID() {
		return ftpServerID;
	}
	public void setFtpServerID(String ftpServerID) {
		this.ftpServerID = ftpServerID;
	}
	public String getFtpUserID() {
		return ftpUserID;
	}
	public void setFtpUserID(String ftpUserID) {
		this.ftpUserID = ftpUserID;
	}
	
}
