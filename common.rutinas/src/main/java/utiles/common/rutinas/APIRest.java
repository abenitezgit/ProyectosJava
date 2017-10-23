package utiles.common.rutinas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIRest {
	private String server;
	private String port;
	private String urlBase;
	private String ContentType;
	private String method;
	private String dataRequest;
	private String dataResponse;
	private int responseCode;
	private String responseMessage;
	private String urlAPI;
	
	//Getter and Setter
	
	public String getUrlAPI() {
		return urlAPI;
	}
	
	public String getServer() {
		return server;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUrlBase() {
		return urlBase;
	}
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
	public String getContentType() {
		return ContentType;
	}
	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDataRequest() {
		return dataRequest;
	}
	public void setDataRequest(String dataRequest) {
		this.dataRequest = dataRequest;
	}
	public String getDataResponse() {
		return dataResponse;
	}
	public void setDataResponse(String dataResponse) {
		this.dataResponse = dataResponse;
	}
	
	//Public Methods
	
	public void executeGET() throws Exception {
		try {
			urlAPI = "http://"+server+":"+port+urlBase;
	        URL url = new URL(urlAPI);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod(method);
	        conn.setRequestProperty("Accept", ContentType);
	        
	        responseCode = conn.getResponseCode();
	        responseMessage = conn.getResponseMessage();
	        
	
	        if (conn.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : "
	                        + conn.getResponseCode());
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                        (conn.getInputStream())));
	
	        String output;
	        StringBuilder sb = new StringBuilder();
	        while ((output = br.readLine()) != null) {
	                sb.append(output);
	        }
	        
	        br.close();
	        conn.disconnect();
	        
	        dataResponse = sb.toString();

		} catch (Exception e) {
			throw new Exception("Error executeGET: "+e.getMessage());
		}
	}
	
	public void execute(String input) throws Exception {
		try {
			urlAPI = "http://"+server+":"+port+urlBase;
	        URL url = new URL(urlAPI);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod(method);
	        conn.setRequestProperty("Content-Type", ContentType);
	
	        OutputStream os = conn.getOutputStream();
	        os.write(input.getBytes());
	        os.flush();
	        
	        responseCode = conn.getResponseCode();
	        responseMessage = conn.getResponseMessage();
	        
	
	        if (conn.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : "
	                        + conn.getResponseCode());
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                        (conn.getInputStream())));
	
	        String output;
	        StringBuilder sb = new StringBuilder();
	        while ((output = br.readLine()) != null) {
	                sb.append(output);
	        }
	        
	        br.close();
	        os.close();
	        conn.disconnect();
	        
	        dataResponse = sb.toString();

		} catch (Exception e) {
			throw new Exception("Error execute APIRest: "+e.getMessage());
		}
	}
}
