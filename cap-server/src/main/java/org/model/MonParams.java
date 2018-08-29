package org.model;

public class MonParams {
	long logID;
	long messageID;
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
	String thMainAction;
	String thListenerAction;
	String thSyncAction;
	String thDBAccessAction;
	String thProcessAction;
	
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
	public String getThMainAction() {
		return thMainAction;
	}
	public void setThMainAction(String thMainAction) {
		this.thMainAction = thMainAction;
	}
	public String getThListenerAction() {
		return thListenerAction;
	}
	public void setThListenerAction(String thListenerAction) {
		this.thListenerAction = thListenerAction;
	}
	public String getThSyncAction() {
		return thSyncAction;
	}
	public void setThSyncAction(String thSyncAction) {
		this.thSyncAction = thSyncAction;
	}
	public String getThDBAccessAction() {
		return thDBAccessAction;
	}
	public void setThDBAccessAction(String thDBAccessAction) {
		this.thDBAccessAction = thDBAccessAction;
	}
	public String getThProcessAction() {
		return thProcessAction;
	}
	public void setThProcessAction(String thProcessAction) {
		this.thProcessAction = thProcessAction;
	}
	public long getMessageID() {
		return messageID;
	}
	public void setMessageID(long messageID) {
		this.messageID = messageID;
	}
	public long getLogID() {
		return logID;
	}
	public void setLogID(long logID) {
		this.logID = logID;
	}
}	
