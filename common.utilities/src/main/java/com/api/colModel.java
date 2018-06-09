package com.api;

public class colModel {
	String rowKey;	//Key del row
	String family;  //cf: column family
	String column;  //cq: column quialifier
	String value;   //valoe
	long timesTamp;
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public long getTimesTamp() {
		return timesTamp;
	}
	public void setTimesTamp(long timesTamp) {
		this.timesTamp = timesTamp;
	}
	
}
