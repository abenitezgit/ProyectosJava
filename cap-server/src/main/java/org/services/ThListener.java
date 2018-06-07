package org.services;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


public class ThListener implements Runnable{
	Logger logger = Logger.getLogger("cap-Listener");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	public ThListener(GlobalParams m) {
		gParams = m;
	}
	
    @Override
    public void run() {
        try {
        	/**
        	 * Iniciando Thread Listener
        	 */
        	logger.info("Iniciando Listener Server port: " + gParams.getMapMonParams().get(gParams.getMonID()).getMonPort());

            @SuppressWarnings("resource")
			ServerSocket skServidor = new ServerSocket(Integer.valueOf(gParams.getMapMonParams().get(gParams.getMonID()).getMonPort()));
            String inputData;
            String outputData = null;
            String dRequest;
            String dAuth;
            JSONObject jHeader;
            JSONObject jData;
            
            while (true) {
            	
            	logger.info("Esperando transaccion...");
            	
                Socket skCliente = skServidor.accept();
                InputStream inpStr = skCliente.getInputStream();
                ObjectInputStream objInput = new ObjectInputStream(inpStr);
                
                //Espera Entrada
                //
                try {
                	inputData  = (String) objInput.readObject();
                	logger.info("Recibiendo RX(): "+ inputData);
                	
                	/*
                	 * Actualiza inicio de ejecucion del modulo
                	 */
	                    
	                jHeader = new JSONObject(inputData);
	                jData = jHeader.getJSONObject("data");
	                    
	                dAuth = jHeader.getString("auth");
	                dRequest = jHeader.getString("request");
	                    
            		if (dAuth.equals(gParams.getInfo().getAuthKey())) {
            			logger.info("Recibiendo RX("+ dRequest +"): "+ jData.toString());
            			
            			//Valida si Thread Main y Listener están ENABLED
            			if (gParams.getMapMonParams().get(gParams.getMonID()).getThMainAction().equals("ENABLED") && 
            				gParams.getMapMonParams().get(gParams.getMonID()).getThListenerAction().equals("ENABLED")) {	
            				
            				
            				logger.info("Procesando Respuesta...");
            			
	            			switch (dRequest) {
	                        	case "getStatus":
	                        		outputData = "";
	                        		break;
	                        	case "getProcControl":
	                        		outputData = "";
	                        		break;
	                        	case "getGroupControl":
	                        		outputData = "";
	                        		break;
	                        	case "getMapTask":
	                        		outputData = "";
	                        		break;
	                        	case "getMapInterval":
	                        		outputData = "";
	                        		break;
	                        	case "syncService":
	                        		outputData = "";
	                        		break;
	                        	case "syncMonitor":
	                        		outputData = ""; //myproc.syncMonitor(jData);
	                        		break;
	                        	default:
	                        		mylib.console("Request: "+dRequest);
	            			} //end switch()
            			} else {
            				logger.warn("Thread Listener no esta habilitado para procesar respuesta");
            				outputData = genResponse(20,"Thread Listener no esta habilitado para procesar respuesta",null);
            			}
                    } else {
                        outputData = mylib.sendError(60);
                    }
                } catch (Exception e) {
                    outputData = mylib.sendError(90);
                }
                //Envia Respuesta
                //
                OutputStream outStr = skCliente.getOutputStream();
                ObjectOutputStream ObjOutput = new ObjectOutputStream(outStr); 
                
                if (outputData==null) {
                		outputData = mylib.sendError(90);
                } 
                
                logger.info("Enviando TX(): "+ outputData);
                
                ObjOutput.writeObject(outputData);
                
                //Cierra Todas las conexiones
                //
                inpStr.close();
                ObjOutput.close();
                objInput.close();
                skCliente.close();
                

            } //End while(true)
            
        } catch (Exception  e) {
            logger.error("Error general  ("+e.getMessage()+")");
        }
    }
    
    private String genResponse(int code, String message, String response) throws Exception {
    	try {
            JSONObject jHeader = new JSONObject();
            JSONObject responseMessage = new JSONObject();
    		String errorMessage=message;
    		int errorNum=code;
    		String status;
            
            if (code==0) {
            	status="OK";
            } else {
            	status="ERROR";	
            }
    		
            jHeader.put("status", status);
            jHeader.put("statusCode", errorNum);
            jHeader.put("message", errorMessage);
            
            responseMessage.put("response",response);
            responseMessage.put("header", jHeader);
                
            return jHeader.toString();
    	} catch (Exception e) {
    		throw new Exception("genResponse: "+e.getMessage());
    	}
    }
    
}
