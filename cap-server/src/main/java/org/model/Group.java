package org.model;

public class Group {

	private String grpID;
	private String grpDesc;
	private int enable;
	private String horID;
	private String cliID;
	private int maxTimeExec;
	private String typeBalance;
	private String typeRequest;
	private String catID;
	
	//Getter and setter
	
	public String getGrpID() {
		return grpID;
	}
	public String getCatID() {
		return catID;
	}
	public void setCatID(String catID) {
		this.catID = catID;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public String getGrpDesc() {
		return grpDesc;
	}
	public void setGrpDesc(String grpDesc) {
		this.grpDesc = grpDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getHorID() {
		return horID;
	}
	public void setHorID(String horID) {
		this.horID = horID;
	}
	public String getCliID() {
		return cliID;
	}
	public void setCliID(String cliID) {
		this.cliID = cliID;
	}
	public int getMaxTimeExec() {
		return maxTimeExec;
	}
	public void setMaxTimeExec(int maxTimeExec) {
		this.maxTimeExec = maxTimeExec;
	}
	public String getTypeBalance() {
		return typeBalance;
	}
	public void setTypeBalance(String typeBalance) {
		this.typeBalance = typeBalance;
	}
	public String getTypeRequest() {
		return typeRequest;
	}
	public void setTypeRequest(String typeRequest) {
		this.typeRequest = typeRequest;
	}
}
