package org.model;

public class AgeGroupStat {
	
	private int nDay;
	private int nHour;
	private int nGroups;
	private int alerts;
	
	//Getter ans Setter
	
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
	public int getnGroups() {
		return nGroups;
	}
	public void setnGroups(int nGroups) {
		this.nGroups = nGroups;
	}
	public int getAlerts() {
		return alerts;
	}
	public void setAlerts(int alerts) {
		this.alerts = alerts;
	}

}
