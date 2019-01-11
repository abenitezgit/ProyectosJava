package org.model;

import java.util.Date;

public class PGPending {
	
	String grpID;
	String numSecExec;
	String procID;
	String procDesc;
	String typeProc;
	int nOrder;
	String status;
	Date fecIns;
	String cliID;
	String cliDesc;
	String typeExec;
	String grpDesc;

	//Getter and setter
	
	public String getCliID() {
		return cliID;
	}
	public String getGrpDesc() {
		return grpDesc;
	}
	public void setGrpDesc(String grpDesc) {
		this.grpDesc = grpDesc;
	}
	public String getProcDesc() {
		return procDesc;
	}
	public void setProcDesc(String procDesc) {
		this.procDesc = procDesc;
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
	public String getGrpID() {
		return grpID;
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
	public String getTypeProc() {
		return typeProc;
	}
	public void setTypeProc(String typeProc) {
		this.typeProc = typeProc;
	}
	public int getnOrder() {
		return nOrder;
	}
	public void setnOrder(int nOrder) {
		this.nOrder = nOrder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getFecIns() {
		return fecIns;
	}
	public void setFecIns(Date fecIns) {
		this.fecIns = fecIns;
	}
	public String getTypeExec() {
		return typeExec;
	}
	public void setTypeExec(String typeExec) {
		this.typeExec = typeExec;
	}

}
