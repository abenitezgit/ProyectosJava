package org.model;

public class LtbResult {
	int rowsRead;
	int rowsInserted;
	int rowsError;

	public int getRowsInserted() {
		return rowsInserted;
	}
	public void setRowsInserted(int rowsInserted) {
		this.rowsInserted = rowsInserted;
	}
	public int getRowsRead() {
		return rowsRead;
	}
	public void setRowsRead(int rowsRead) {
		this.rowsRead = rowsRead;
	}
	public int getRowsError() {
		return rowsError;
	}
	public void setRowsError(int rowsError) {
		this.rowsError = rowsError;
	}
}
