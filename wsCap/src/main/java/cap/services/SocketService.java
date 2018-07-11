package cap.services;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.rutinas.Rutinas;

import cap.utiles.GlobalParams;

public class SocketService {
	Logger logger = Logger.getLogger("SocketService");
	SocketAPI socket = new SocketAPI();
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
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
	
	public String getMonRequest(String method, Object param) throws Exception {
		try {
			JSONObject jData;
			String response = "";
			String request = "";
			String strParam = "";
			String authKey = "qwerty0987";
			
			switch(method) {
				case "getProcControl":
					logger.info("Ejecutando: "+method);
					strParam = mylib.serializeObjectToJSon(param, false);
					logger.info("Parametros recibidos: "+strParam);
					
					String status = new JSONObject(strParam).getString("status");
					String uStatus = new JSONObject(strParam).getString("uStatus");
					
					jData = new JSONObject();
					jData.put("method", method);
					jData.put("status", status);
					jData.put("uStatus", uStatus);
					
					request = genRequest(authKey, "monRequest", jData);
	
					response = executeRequest(request);
					
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String getDBRequest(String method, Object param) throws Exception {
		try {
			JSONObject jData;
			String response = "";
			String request = "";
			String strParam = "";
			String authKey = "qwerty0987";
			
			switch(method) {
				case "getDBGroup":
					logger.info("Ejecutando: "+method);
					strParam = mylib.serializeObjectToJSon(param, false);
					logger.info("Parametros recibidos: "+strParam);
					
					String grpID = new JSONObject(strParam).getString("grpID");
					
					jData = new JSONObject();
					jData.put("method", method);
					jData.put("grpID", grpID);
					
					request = genRequest(authKey, "dbRequest", jData);
	
					response = executeRequest(request);
					
					break;
				case "getDBprocGroup":
					logger.info("Ejecutando: "+method);
					strParam = mylib.serializeObjectToJSon(param, false);
					logger.info("Parametros recibidos: "+strParam);
					
					String grpID2 = new JSONObject(strParam).getString("grpID");

					jData = new JSONObject();
					jData.put("method", method);
					jData.put("grpID", grpID2);
					
					request = genRequest(authKey, "dbRequest", jData);
	
					response = executeRequest(request);
					
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String executeRequest(String request) throws Exception {
		try {
			String response = "";
			
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
						response = socket.getResponse();
					} else {
						logger.error("Error respuesta socket: "+socket.getStatusCode()+"  "+socket.getMessage());
					}
				}
				
				logger.info("Cerrando Socket...");
				socket.close();
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("Error conectando a socket primario: "+e.getMessage());
		}
	}
	
	public String getMapProc() {
		
		//Try Primary MonID
		try {
			logger.info("Socket Access");
			logger.info("Iniciando acceso a Primary cap-server");
			logger.info("Validando Puerto disponible en IP: "+gParams.getConfig().getMonIP()+ "  Port: "+gParams.getConfig().getMonPort());
			Socket skClient = new Socket(gParams.getConfig().getMonIP(), Integer.valueOf(gParams.getConfig().getMonPort()));
			
            OutputStream aux = skClient.getOutputStream(); 
            ObjectOutputStream flujo= new ObjectOutputStream( aux );
            
            //Generando Socket Request
            logger.info("Generando Socket Request...");
            JSONObject jData = new JSONObject();
            JSONObject jo = new JSONObject();
            jo.put("data", jData);
            jo.put("auth", "qwerty0987");
            jo.put("request", "getProcControl");
            
            logger.info("Enviando request: "+jo.toString());
            flujo.writeObject( jo.toString() ); 
            
            logger.info("Recibiendo respuesta...");
            InputStream inpStr = skClient.getInputStream();
            ObjectInputStream dataInput = new ObjectInputStream(inpStr);
            
            String response = (String) dataInput.readObject();
            logger.info("Respuesta desde cap-server: "+response);

            logger.info("Cerrando objetos sockets client...");
            inpStr.close();
            dataInput.close();
            aux.close();
            flujo.close();
            skClient.close();
            
            logger.info("Retornando respuesta...");
			
			return response;
		} catch (Exception e) {
			logger.error("getMapProc: "+e.getMessage());
			return null;
		}
	}
}
