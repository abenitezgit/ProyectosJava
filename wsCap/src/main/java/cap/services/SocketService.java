package cap.services;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import cap.utiles.GlobalParams;

public class SocketService {
	Logger logger = Logger.getLogger("SocketService");
	SocketAPI socket = new SocketAPI();
	GlobalParams gParams;
	
	private String message;
	private int statusCode;
	private Object response;
	
	public SocketService(GlobalParams m) {
		gParams = m;
	}
	
	
	public String genRequest(String authKey, String request, JSONObject jData) throws Exception {
		try {
			
            JSONObject jo = new JSONObject();
            
            jo.put("data", jData);
            jo.put("auth", authKey);
            jo.put("request", request);
	
			return jo.toString();
			
		} catch (Exception e) {
			throw new Exception("genCapRequest(): "+e.getMessage());
		}
	}
	
	public boolean executeRequest(String request) throws Exception {
		boolean exitStatus = false;
		
		try {
			logger.info("Iniciando acceso a Primary cap-server");
			
			socket.setServerIP(gParams.getConfig().getMonIP());
			socket.setPort(Integer.valueOf(gParams.getConfig().getMonPort()));
			
			logger.info("Abriendo Socket Server...");
			socket.open();
			
			if (socket.isConnected()) {
				
				logger.info("Socket Connected!");
				logger.info("Enviando request: "+request);
				if (socket.send(request)) {
					
					if (socket.getStatusCode()==0) {
						logger.info("Respuesta Existosa: "+socket.getResponse());
						statusCode = 0;
						message = "";
						response = socket.getResponse();
						exitStatus=true;
					} else {
						logger.error("Error respuesta socket: "+socket.getStatusCode()+"  "+socket.getMessage());
						statusCode = socket.getStatusCode();
						message = socket.getMessage();
					}
				}
				
				logger.info("Cerrando Socket...");
				socket.close();
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("Error conectando a socket primario: "+e.getMessage());
		}
	}
	
	public Object getResponse() {
		return response;
	}


	public void setResponse(Object response) {
		this.response = response;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
