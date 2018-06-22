package org.services;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class SocketService {
	Logger logger = Logger.getLogger("SocketService");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	String socketResponse="";
	
	public SocketService(GlobalParams m) {
		gParams = m;
	}
	
	public String genCapRequest(String authKey, String request, JSONObject jData) throws Exception {
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

	public boolean syncTaskProcess(String srvID) throws Exception {
		try {
			//Clase de Sockets
			SocketAPI socketPri = new SocketAPI();
			
			//Variable de Respuesta
			boolean isSocketPriActive=true;
			boolean result = false;
			
			//Genera Request
			JSONObject jData = new JSONObject();
			jData.put("srvID", srvID);
			jData.put("mapTask", mylib.serializeObjectToJSon(gParams.getMapTask(), false));
			String authKey = gParams.getAppConfig().getAuthKey();
			String request = genCapRequest(authKey, "syncTaskProcess", jData);
			
			//Try Primary MonID
			if (isSocketPriActive) {
				try {
					logger.info("Iniciando acceso a Primary cap-server");
					logger.info("Validando Puerto disponible en IP: "+gParams.getAppConfig().getMonIP()+ "  Port: "+gParams.getAppConfig().getMonPort());
					
					socketPri.setServerIP(gParams.getAppConfig().getMonIP());
					socketPri.setPort(Integer.valueOf(gParams.getAppConfig().getMonPort()));
					
					logger.info("Abriendo Socket Server...");
					socketPri.open();
					
					if (socketPri.isConnected()) {
						
						logger.info("Socket Connected!");
						logger.info("Enviando request: "+request);
						if (socketPri.send(request)) {
							
							if (socketPri.getStatusCode()==0) {
								logger.info("Respuesta Existosa: "+socketPri.getResponse());
								setSocketResponse(socketPri.getResponse());
								result = true;
							} else {
								logger.error("Error respuesta socket: "+socketPri.getStatusCode()+"  "+socketPri.getMessage());
							}
						}
						
						logger.info("Cerrando Socket...");
						socketPri.close();
					}
				} catch (Exception e) {
					logger.error("Error conectando a socket primario: "+e.getMessage());
					isSocketPriActive = false;
				}
			}
					
			return result;
		} catch (Exception e) {
			throw new Exception("getServiceParams(): "+e.getMessage());
		} 
	}

	public boolean getServiceParams(String srvID) throws Exception {
		try {
			//Clase de Sockets
			SocketAPI socketPri = new SocketAPI();
			
			//Variable de Respuesta
			boolean isSocketPriActive=true;
			boolean result = false;
			
			//Genera Request
			JSONObject jData = new JSONObject();
			jData.put("srvID", srvID);
			jData.put("service", mylib.serializeObjectToJSon(gParams.getService(), false));
			String authKey = gParams.getAppConfig().getAuthKey();
			String request = genCapRequest(authKey, "syncServiceParams", jData);
			
			//Try Primary MonID
			if (isSocketPriActive) {
				try {
					logger.info("Iniciando acceso a Primary cap-server");
					logger.info("Validando Puerto disponible en IP: "+gParams.getAppConfig().getMonIP()+ "  Port: "+gParams.getAppConfig().getMonPort());
					
					socketPri.setServerIP(gParams.getAppConfig().getMonIP());
					socketPri.setPort(Integer.valueOf(gParams.getAppConfig().getMonPort()));
					
					logger.info("Abriendo Socket Server...");
					socketPri.open();
					
					if (socketPri.isConnected()) {
						
						logger.info("Socket Connected!");
						logger.info("Enviando request: "+request);
						if (socketPri.send(request)) {
							
							if (socketPri.getStatusCode()==0) {
								logger.info("Respuesta Existosa: "+socketPri.getResponse());
								setSocketResponse(socketPri.getResponse());
								result = true;
							} else {
								logger.error("Error respuesta socket: "+socketPri.getStatusCode()+"  "+socketPri.getMessage());
							}
						}
						
						logger.info("Cerrando Socket...");
						socketPri.close();
					}
				} catch (Exception e) {
					logger.error("Error conectando a socket primario: "+e.getMessage());
					isSocketPriActive = false;
				}
			}
					
			return result;
		} catch (Exception e) {
			throw new Exception("getServiceParams(): "+e.getMessage());
		} 
	}

	public String getSocketResponse() {
		return socketResponse;
	}

	public void setSocketResponse(String socketResponse) {
		this.socketResponse = socketResponse;
	}
	
	
}
