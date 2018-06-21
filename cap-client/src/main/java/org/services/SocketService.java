package org.services;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class SocketService {
	Logger logger = Logger.getLogger("SocketService");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
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
	
	public String getServiceParams(String srvID) throws Exception {
		try {
			//Clase de Sockets
			SocketAPI socketPri = new SocketAPI();
			
			//Variable de Respuesta
			String response="";
			boolean isSocketPriActive=true;
			
			
			//Genera Request
			JSONObject jData = new JSONObject();
			jData.put("srvID", srvID);
			jData.put("service", mylib.serializeObjectToJSon(gParams.getService(), false));
			String authKey = gParams.getCfgParams().getAuthKey();
			String request = genCapRequest(authKey, "syncServiceParams", jData);
			
			//Try Primary MonID
			if (isSocketPriActive) {
				try {
					logger.info("Iniciando acceso a Primary cap-server");
					logger.info("Validando Puerto disponible en IP: "+gParams.getCfgParams().getMonIP()+ "  Port: "+gParams.getCfgParams().getMonPort());
					
					socketPri.setServerIP(gParams.getCfgParams().getMonIP());
					socketPri.setPort(Integer.valueOf(gParams.getCfgParams().getMonPort()));
					
					logger.info("Abriendo Socket Server...");
					socketPri.open();
					
					if (socketPri.isConnected()) {
						
						logger.info("Socket Connected!");
						logger.info("Enviando request: "+request);
						if (socketPri.send(request)) {
							
							if (socketPri.getStatusCode()==0) {
								logger.info("Respuesta Existosa: "+socketPri.getResponse());
								response = socketPri.getResponse();
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
					
			return response;
		} catch (Exception e) {
			throw new Exception("getServiceParams(): "+e.getMessage());
		} 
	}
	
}
