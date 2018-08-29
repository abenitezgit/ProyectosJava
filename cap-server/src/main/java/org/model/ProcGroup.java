package org.model;

public class ProcGroup {
	
	private String grpID;
	private String procID;
	private int nOrder;
	private int enable;
	private String type;
	
	public String getGrpID() {
		return grpID;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public String getProcID() {
		return procID;
	}
	public void setProcID(String procID) {
		this.procID = procID;
	}
	public int getnOrder() {
		return nOrder;
	}
	public void setnOrder(int nOrder) {
		this.nOrder = nOrder;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
