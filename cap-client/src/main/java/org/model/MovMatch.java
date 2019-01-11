package org.model;

public class MovMatch {
	String movID;
	int movOrder;
	String sourceField;
	int sourceLength;
	String sourceType;
	String destField;
	int destLength;
	String fieldType;
	int enable;

	//Getter and Setter
	
	public String getMovID() {
		return movID;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public void setMovID(String movID) {
		this.movID = movID;
	}
	public int getMovOrder() {
		return movOrder;
	}
	public void setMovOrder(int movOrder) {
		this.movOrder = movOrder;
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public int getSourceLength() {
		return sourceLength;
	}
	public void setSourceLength(int sourceLength) {
		this.sourceLength = sourceLength;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getDestField() {
		return destField;
	}
	public void setDestField(String destField) {
		this.destField = destField;
	}
	public int getDestLength() {
		return destLength;
	}
	public void setDestLength(int destLength) {
		this.destLength = destLength;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
}
