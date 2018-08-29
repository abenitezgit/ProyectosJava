package org.model;

public class LoadTableParam {
	private String ltbID;
	private int enable;
	private int filePosIni;
	private int filePosFin;
	private int fileLoadOrder;
	private String tbFieldName;
	private String tbFieldDataType;
	private int tbFieldLength;
	private int tbLoadFromFile;
	
	public int getTbLoadFromFile() {
		return tbLoadFromFile;
	}
	public void setTbLoadFromFile(int tbLoadFromFile) {
		this.tbLoadFromFile = tbLoadFromFile;
	}
	public String getLtbID() {
		return ltbID;
	}
	public void setLtbID(String ltbID) {
		this.ltbID = ltbID;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public int getFilePosIni() {
		return filePosIni;
	}
	public void setFilePosIni(int filePosIni) {
		this.filePosIni = filePosIni;
	}
	public int getFilePosFin() {
		return filePosFin;
	}
	public void setFilePosFin(int filePosFin) {
		this.filePosFin = filePosFin;
	}
	public int getFileLoadOrder() {
		return fileLoadOrder;
	}
	public void setFileLoadOrder(int fileLoadOrder) {
		this.fileLoadOrder = fileLoadOrder;
	}
	public String getTbFieldName() {
		return tbFieldName;
	}
	public void setTbFieldName(String tbFieldName) {
		this.tbFieldName = tbFieldName;
	}
	public String getTbFieldDataType() {
		return tbFieldDataType;
	}
	public void setTbFieldDataType(String tbFieldDataType) {
		this.tbFieldDataType = tbFieldDataType;
	}
	public int getTbFieldLength() {
		return tbFieldLength;
	}
	public void setTbFieldLength(int tbFieldLength) {
		this.tbFieldLength = tbFieldLength;
	}
}
