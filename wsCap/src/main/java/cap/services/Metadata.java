package cap.services;

import org.json.JSONObject;

import cap.model.DataRequest;
import cap.utiles.GlobalParams;

public class Metadata {
	GlobalParams gParams;
	SocketService ss;
	
	private int statusCode;
	private String message;
	private Object response;
	
	//Variables que pueden ser externas
	final String AUTHKEY = "qwerty0987";
	
	public Metadata(GlobalParams m) {
		this.gParams = m;
		this.ss = new SocketService(gParams);
	}
	
	public String genStringRequest(String listener_method, DataRequest dr) throws Exception {
		try {
			JSONObject joRequest = new JSONObject();
			
			switch (gParams.getConfig().getConnectTypeMon()) {
				case "SOCKET":
					JSONObject jData = new JSONObject();
					
					jData.put("method", dr.getMethod());
					jData.put("params", dr.getParam());
					
					joRequest.put("data", jData);
					joRequest.put("auth", AUTHKEY);
					joRequest.put("request", listener_method);
			
					break;
			}
			
			return joRequest.toString();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean executeRequest(String sendRequest) throws Exception {
		boolean exitStatus = false;
		
		try {
			
			switch (gParams.getConfig().getConnectTypeMon()) {
			case "SOCKET":
				if (ss.executeRequest(sendRequest)) {
					statusCode = ss.getStatusCode();
					message = ss.getMessage();
					response = ss.getResponse();
					exitStatus = true;
				} else {
					statusCode = ss.getStatusCode();
					message = ss.getMessage();
				}

				break;
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
}
