package org.model;

public class Dependence {
	String grpID;
	int critical;
	String procHijo;
	String procPadre;
	
	public String getGrpID() {
		return grpID;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public int getCritical() {
		return critical;
	}
	public void setCritical(int critical) {
		this.critical = critical;
	}
	public String getProcHijo() {
		return procHijo;
	}
	public void setProcHijo(String procHijo) {
		this.procHijo = procHijo;
	}
	public String getProcPadre() {
		return procPadre;
	}
	public void setProcPadre(String procPadre) {
		this.procPadre = procPadre;
	}
}
