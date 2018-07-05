package org.model;

import java.util.Map;
import java.util.TreeMap;

public class Osp {
	
	String ospID;
	String ospDesc;
	int enable;
	String ospName;
	String cliID;
	String serverID;
	String dbID;
	String loginID;
	String ownerID;
	
	String cliDesc;
	String serverIP;
	String dbName;
	String dbPort;
	String dbType;
	String dbInstance;
	
	String dbLoginUser;
	String dbLoginPass;
	String dbOwnerUser;
	String dbOwnerPass;
	
	Map<String, OspParam> mapOspParam = new TreeMap<>();
	
	//Getter and setter
	
	public String getOspID() {
		return ospID;
	}
	public void setOspID(String ospID) {
		this.ospID = ospID;
	}
	public String getOspDesc() {
		return ospDesc;
	}
	public void setOspDesc(String ospDesc) {
		this.ospDesc = ospDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getOspName() {
		return ospName;
	}
	public void setOspName(String ospName) {
		this.ospName = ospName;
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
	public String getCliDesc() {
		return cliDesc;
	}
	public void setCliDesc(String cliDesc) {
		this.cliDesc = cliDesc;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
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
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDbLoginUser() {
		return dbLoginUser;
	}
	public void setDbLoginUser(String dbLoginUser) {
		this.dbLoginUser = dbLoginUser;
	}
	public String getDbLoginPass() {
		return dbLoginPass;
	}
	public void setDbLoginPass(String dbLoginPass) {
		this.dbLoginPass = dbLoginPass;
	}
	public String getDbOwnerUser() {
		return dbOwnerUser;
	}
	public void setDbOwnerUser(String dbOwnerUser) {
		this.dbOwnerUser = dbOwnerUser;
	}
	public String getDbOwnerPass() {
		return dbOwnerPass;
	}
	public void setDbOwnerPass(String dbOwnerPass) {
		this.dbOwnerPass = dbOwnerPass;
	}
	public Map<String, OspParam> getMapOspParam() {
		return mapOspParam;
	}
	public void setMapOspParam(Map<String, OspParam> mapOspParam) {
		this.mapOspParam = mapOspParam;
	}
	public String getDbInstance() {
		return dbInstance;
	}
	public void setDbInstance(String dbInstance) {
		this.dbInstance = dbInstance;
	}

}
