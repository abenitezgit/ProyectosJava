package org.model;

public class AgeGroup {
	private String grpID;
	private String ageID;
	private String numSecExec;
	private int nDay;
	private int nHour;
	private int alert;
	
	//Getter and Setter
	
	public String getAgeID() {
		return ageID;
	}
	public String getGrpID() {
		return grpID;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public void setAgeID(String ageID) {
		this.ageID = ageID;
	}
	public String getNumSecExec() {
		return numSecExec;
	}
	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}
	public int getnDay() {
		return nDay;
	}
	public void setnDay(int nDay) {
		this.nDay = nDay;
	}
	public int getnHour() {
		return nHour;
	}
	public void setnHour(int nHour) {
		this.nHour = nHour;
	}
	public int getAlert() {
		return alert;
	}
	public void setAlert(int alert) {
		this.alert = alert;
	}
}
