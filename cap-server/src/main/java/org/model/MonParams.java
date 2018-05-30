package org.model;

public class MonParams {
	String monID;
	String monHostName;
	String monIP;
	String monPort;
	String monRole;
	int txpMain;
	int TxpSync;
	int TxpProcess;
	int ageShowHour;
	int ageGapMinute;
	
	public String getMonID() {
		return monID;
	}
	public void setMonID(String monID) {
		this.monID = monID;
	}
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
	public String getMonRole() {
		return monRole;
	}
	public void setMonRole(String monRole) {
		this.monRole = monRole;
	}
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public int getTxpSync() {
		return TxpSync;
	}
	public void setTxpSync(int txpSync) {
		TxpSync = txpSync;
	}
	public int getTxpProcess() {
		return TxpProcess;
	}
	public void setTxpProcess(int txpProcess) {
		TxpProcess = txpProcess;
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
}	
