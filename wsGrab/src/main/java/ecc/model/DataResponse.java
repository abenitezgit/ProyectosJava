package ecc.model;

import java.util.HashMap;
import java.util.Map;

public class DataResponse {
	int status;
	String message;
	int numFound;
	int limit;
	Map<String, Grabacion> data = new HashMap<>();
	
	/*
	 * Getter and setter
	 */
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Map<String, Grabacion> getData() {
		return data;
	}
	public void setData(Map<String, Grabacion> data) {
		this.data = data;
	}

}
