package org.model;

public class MovResult {
	int rowsRead;
	int rowsLoad;
	int rowsError;
	long pctError;
	long maxPctError;
	int maxRowsError;
	
	public int getRowsRead() {
		return rowsRead;
	}
	public void setRowsRead(int rowsRead) {
		this.rowsRead = rowsRead;
	}
	public int getRowsLoad() {
		return rowsLoad;
	}
	public void setRowsLoad(int rowsLoad) {
		this.rowsLoad = rowsLoad;
	}
	public int getRowsError() {
		return rowsError;
	}
	public void setRowsError(int rowsError) {
		this.rowsError = rowsError;
	}
	public long getPctError() {
		return pctError;
	}
	public void setPctError(long pctError) {
		this.pctError = pctError;
	}
	public long getMaxPctError() {
		return maxPctError;
	}
	public void setMaxPctError(long maxPctError) {
		this.maxPctError = maxPctError;
	}
	public int getMaxRowsError() {
		return maxRowsError;
	}
	public void setMaxRowsError(int maxRowsError) {
		this.maxRowsError = maxRowsError;
	}
}
