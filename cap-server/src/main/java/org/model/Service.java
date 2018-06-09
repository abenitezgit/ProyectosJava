package org.model;

import java.util.Date;

public class Service {
	
	String srvID;
	String srvDesc;
	int enable;
	String srvTypeProc;
	int orderBalance;
	int pctBalance;
	int txpMain;
	Date fecUpdate;
	
	
	public Date getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(Date fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public String getSrvID() {
		return srvID;
	}
	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}
	public String getSrvDesc() {
		return srvDesc;
	}
	public void setSrvDesc(String srvDesc) {
		this.srvDesc = srvDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getSrvTypeProc() {
		return srvTypeProc;
	}
	public void setSrvTypeProc(String srvTypeProc) {
		this.srvTypeProc = srvTypeProc;
	}
	public int getOrderBalance() {
		return orderBalance;
	}
	public void setOrderBalance(int orderBalance) {
		this.orderBalance = orderBalance;
	}
	public int getPctBalance() {
		return pctBalance;
	}
	public void setPctBalance(int pctBalance) {
		this.pctBalance = pctBalance;
	}
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}

}
