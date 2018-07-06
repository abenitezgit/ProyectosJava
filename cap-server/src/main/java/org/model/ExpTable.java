package org.model;

import java.util.Map;
import java.util.TreeMap;

public class ExpTable {
	
	private String etbID;
	private String etbDesc;
	private String etbTableName;
	private String etbFileName;
	private String etbFileExt;
	private String etbFileType;
	private String etbFileSep;
	private int etbAppend;
	private int etbHeader;
	private int etbWhere_active;
	private String etbWhere_body;
	private int etbOrder_active;
	private String etbOrder_body;
	private int etbMaxRows;
	private int etbMultiFiles;
	private int etbMaxRows_multiFiles;
	private int etbGetEmptyFile;
	private String dbServerIP;
	private String dbID;
	private String dbPort;
	private String dbInstance;
	private String dbName;
	private String dbType;
	private String loginID;
	private String loginName;
	private String loginPass;
	private String ownerID;
	private String ownerName;
	private String ownerPass;
	private int ftpEnable;
	private int ftpSecure;
	private String ftpRemotePath;
	private String ftpServerID;
	private String ftpUserID;
	private String ftpServerIP;
	private String ftpUserName;
	private String ftpUserPass;
	
	private Map<String, ExpTableParam> mapEtbParam = new TreeMap<>();

	
	//Getter and Setter
	
	public String getEtbID() {
		return etbID;
	}

	public void setEtbID(String etbID) {
		this.etbID = etbID;
	}

	public String getEtbDesc() {
		return etbDesc;
	}

	public void setEtbDesc(String etbDesc) {
		this.etbDesc = etbDesc;
	}

	public String getEtbTableName() {
		return etbTableName;
	}

	public void setEtbTableName(String etbTableName) {
		this.etbTableName = etbTableName;
	}

	public String getEtbFileName() {
		return etbFileName;
	}

	public void setEtbFileName(String etbFileName) {
		this.etbFileName = etbFileName;
	}

	public String getEtbFileExt() {
		return etbFileExt;
	}

	public void setEtbFileExt(String etbFileExt) {
		this.etbFileExt = etbFileExt;
	}

	public String getEtbFileType() {
		return etbFileType;
	}

	public void setEtbFileType(String etbFileType) {
		this.etbFileType = etbFileType;
	}

	public String getEtbFileSep() {
		return etbFileSep;
	}

	public void setEtbFileSep(String etbFileSep) {
		this.etbFileSep = etbFileSep;
	}

	public int getEtbAppend() {
		return etbAppend;
	}

	public void setEtbAppend(int etbAppend) {
		this.etbAppend = etbAppend;
	}

	public int getEtbHeader() {
		return etbHeader;
	}

	public void setEtbHeader(int etbHeader) {
		this.etbHeader = etbHeader;
	}

	public int getEtbWhere_active() {
		return etbWhere_active;
	}

	public void setEtbWhere_active(int etbWhere_active) {
		this.etbWhere_active = etbWhere_active;
	}

	public String getEtbWhere_body() {
		return etbWhere_body;
	}

	public void setEtbWhere_body(String etbWhere_body) {
		this.etbWhere_body = etbWhere_body;
	}

	public int getEtbOrder_active() {
		return etbOrder_active;
	}

	public void setEtbOrder_active(int etbOrder_active) {
		this.etbOrder_active = etbOrder_active;
	}

	public String getEtbOrder_body() {
		return etbOrder_body;
	}

	public void setEtbOrder_body(String etbOrder_body) {
		this.etbOrder_body = etbOrder_body;
	}

	public int getEtbMaxRows() {
		return etbMaxRows;
	}

	public void setEtbMaxRows(int etbMaxRows) {
		this.etbMaxRows = etbMaxRows;
	}

	public int getEtbMultiFiles() {
		return etbMultiFiles;
	}

	public void setEtbMultiFiles(int etbMultiFiles) {
		this.etbMultiFiles = etbMultiFiles;
	}

	public int getEtbMaxRows_multiFiles() {
		return etbMaxRows_multiFiles;
	}

	public void setEtbMaxRows_multiFiles(int etbMaxRows_multiFiles) {
		this.etbMaxRows_multiFiles = etbMaxRows_multiFiles;
	}

	public int getEtbGetEmptyFile() {
		return etbGetEmptyFile;
	}

	public void setEtbGetEmptyFile(int etbGetEmptyFile) {
		this.etbGetEmptyFile = etbGetEmptyFile;
	}

	public String getDbServerIP() {
		return dbServerIP;
	}

	public void setDbServerIP(String dbServerIP) {
		this.dbServerIP = dbServerIP;
	}

	public String getDbID() {
		return dbID;
	}

	public void setDbID(String dbID) {
		this.dbID = dbID;
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

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
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

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
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

	public Map<String, ExpTableParam> getMapEtbParam() {
		return mapEtbParam;
	}

	public void setMapEtbParam(Map<String, ExpTableParam> mapEtbParam) {
		this.mapEtbParam = mapEtbParam;
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
	
	

	
}
