package ecc.model;

public class PutResponse {
	int status;
	String message;
	
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
}
