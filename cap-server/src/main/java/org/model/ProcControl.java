package org.model;

import java.util.Date;

public class ProcControl {
	String grpID;
	String numSecExec;
	String procID;
	String typeProc;
	int nOrder;
	Date fecIns;
	Date fecUpdate;
	String status;
	String uStatus;
	String cliID;
	String cliDesc;
	int errCode;
	String errMesg;
	Object param;
	
	//Getter and Setter
	
	public String getGrpID() {
		return grpID;
	}
	public String getCliID() {
		return cliID;
	}
	public void setCliID(String cliID) {
		this.cliID = cliID;
	}
	public String getCliDesc() {
		return cliDesc;
	}
	public void setCliDesc(String cliDesc) {
		this.cliDesc = cliDesc;
	}
	public int getnOrder() {
		return nOrder;
	}
	public void setnOrder(int nOrder) {
		this.nOrder = nOrder;
	}
	public Date getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(Date fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public String getTypeProc() {
		return typeProc;
	}
	public void setTypeProc(String typeProc) {
		this.typeProc = typeProc;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public String getNumSecExec() {
		return numSecExec;
	}
	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}
	public String getProcID() {
		return procID;
	}
	public void setProcID(String procID) {
		this.procID = procID;
	}
	public Date getFecIns() {
		return fecIns;
	}
	public void setFecIns(Date fecIns) {
		this.fecIns = fecIns;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getuStatus() {
		return uStatus;
	}
	public void setuStatus(String uStatus) {
		this.uStatus = uStatus;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMesg() {
		return errMesg;
	}
	public void setErrMesg(String errMesg) {
		this.errMesg = errMesg;
	}
	
}
