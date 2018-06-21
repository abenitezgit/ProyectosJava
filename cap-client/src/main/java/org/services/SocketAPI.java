package org.services;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONObject;

public class SocketAPI {
	private String serverIP;
	private int port;
	private String request;
	private String response;
	private Socket skClient;
	private int statusCode;
	private String status;
	private String message;
	
	public void open() throws Exception {
		try {
			skClient = new Socket(serverIP, port);
		} catch (Exception e) {
			throw new Exception("open(): "+e.getMessage());
		}
	}
	
	public boolean isConnected() throws Exception {
		try {
			return skClient.isConnected();
		} catch (Exception e) {
			throw new Exception("isConnected(): "+e.getMessage());
		}
	}
	
	public void close() throws Exception {
		try {
			if (skClient.isConnected()) {
				skClient.close();
			}
		} catch (Exception e) {
			throw new Exception("close(): "+e.getMessage());
		}
	}
	
	public boolean send(String request) throws Exception {
		try {
			setRequest(request);
			
            OutputStream aux = skClient.getOutputStream(); 
            ObjectOutputStream flujo= new ObjectOutputStream( aux );

            flujo.writeObject( request ); 
            
            InputStream inpStr = skClient.getInputStream();
            ObjectInputStream dataInput = new ObjectInputStream(inpStr);
            
            String jsonResponse = (String) dataInput.readObject();
            
            JSONObject jo = new JSONObject(jsonResponse);
            
            setStatus(jo.getJSONObject("header").getString("status"));
            setStatusCode(jo.getJSONObject("header").getInt("statusCode"));
            setMessage(jo.getJSONObject("header").getString("message"));
            setResponse(jo.getString("response"));
            
            inpStr.close();
            dataInput.close();
            aux.close();
            flujo.close();
            
            return true;
		} catch (Exception e) {
			throw new Exception("send(): "+e.getMessage());
		}
	}

	//Getter and Setter
	
	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Socket getSkClient() {
		return skClient;
	}

	public void setSkClient(Socket skClient) {
		this.skClient = skClient;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
