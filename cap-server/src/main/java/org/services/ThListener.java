package org.services;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONObject;
import org.model.ReadObject;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;


public class ThListener implements Runnable{
	final String className = "thListener";
	
	Logger logger; 
	MyLogger mylog;

	Rutinas mylib = new Rutinas();
	ServiceControl sc;
	FlowControl fc;
	DataAccess da;
	GlobalParams gParams;
	
	public ThListener(GlobalParams m) {
		gParams = m;
		fc = new FlowControl(gParams);
		da = new DataAccess(gParams);
		sc = new ServiceControl(gParams);
	}
	
    @Override
    public void run() {
		mylog = new MyLogger(gParams, className, "run()");
		logger = mylog.getLogger();
    	mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
    	
    	//Variables de Proceso
    	String inputData;
    	String outputData;
    	String monID = gParams.getAppConfig().getMonID();
    	
        try {
        	/**
        	 * Iniciando Thread Listener
        	 */
        	mylog.info("Iniciando Listener Server port: " + gParams.getMapMonParams().get(monID).getMonPort());

            @SuppressWarnings("resource")
			ServerSocket skServidor = new ServerSocket(Integer.valueOf(gParams.getMapMonParams().get(monID).getMonPort()));
            
            
            while (true) {
            	
            	mylog.info("Esperando transaccion...");
                Socket skCliente = skServidor.accept();
                
                mylog.info("TX() Aceptada");
                
                mylog.info("Recuperando InputStream...");
                InputStream inpStr = skCliente.getInputStream();
                
                mylog.info("Creando ObjectInputStream...");
                ObjectInputStream objInput = new ObjectInputStream(inpStr);
                
                //Espera Entrada
                //
                try {
                	inputData = (String) objInput.readObject();
                	mylog.debug("Recibiendo RX(): "+ inputData);
                	
                	/*
                	 * Actualiza inicio de ejecucion del modulo
                	 */

                	mylog.info("Parseando Data Request...");
                	JSONObject jo = new JSONObject(inputData);

                	ReadObject ro = new ReadObject();
                	
                	ro.setAuth(jo.getString("auth"));
                	ro.setRequest(jo.getString("request"));
                	ro.setData(jo.getJSONObject("data"));
                	
                	mylog.info("Request Header: "+ro.getRequest()+" "+ro.getAuth());
                	
            		if (ro.getAuth().equals(gParams.getAppConfig().getAuthKey())) {
            			
            			//Valida si Thread Main y Listener están ENABLED
            			if (gParams.getMapMonParams().get(monID).getThMainAction().equals("ENABLE") && 
            				gParams.getMapMonParams().get(monID).getThListenerAction().equals("ENABLE")) {	
            				
            				mylog.info("Procesando Respuesta...");
            				
            				JSONObject data = new JSONObject(ro.getData().toString());
            			
	            			switch (ro.getRequest()) {
	                        	case "syncServiceParams":
	                        		
	                        		//Obtiene parametros del servicio desde Metadata
	                        		String strService = sc.syncServiceParams(data);
	                        		
	                        		outputData = genResponse(0,"",strService);
	                        		break;
	                        	case "monRequest":
	                        		Object strMonRequest = sc.getMonRequest(data);
	                        		outputData = genResponse(0, "", strMonRequest);
	                        		break;
	                        	case "syncTaskProcess":
	                        		String strMapTask = sc.syncTaskProcess(data);
	                        		outputData = genResponse(0,"",strMapTask);
	                        		break;
	                        	case "dbRequest":
	                        		Object strDbRequest = sc.getDBrequest(data); 
	                        		outputData = genResponse(0,"",strDbRequest);
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
	                        		mylog.warn("Request no es reconocido");
	                        		outputData = genResponse(40, "Request no es reconocido", "");
	            			} //end switch()
            			} else {
            				mylog.warn("Thread Listener no esta habilitado para procesar respuesta");
            				outputData = genResponse(20,"Thread Listener no esta habilitado para procesar respuesta",null);
            			}
                    } else {
                        outputData = genResponse(60,"Solicitud no está autorizada",null);
                    }
                } catch (Exception e) {
                	outputData = genResponse(90, "Exception error: "+e.getMessage(), "");
                }
                //Envia Respuesta
                //
                OutputStream outStr = skCliente.getOutputStream();
                ObjectOutputStream ObjOutput = new ObjectOutputStream(outStr); 
                
                if (outputData==null) {
                		outputData = genResponse(90,"Exception error",null);
                } 
                
                mylog.debug("Enviando TX(): "+ outputData);
                
                ObjOutput.writeObject(outputData);
                
                //Cierra Todas las conexiones
                //
                inpStr.close();
                ObjOutput.close();
                objInput.close();
                skCliente.close();

                mylog = new MyLogger(gParams, className, "run()");
            } //End while(true)
            
        } catch (Exception  e) {
        	mylog.error("Error general  ("+e.getMessage()+")");
        }
    }
    
    private String genResponse(int code, String message, Object response) throws Exception {
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
                
            return responseMessage.toString();
    	} catch (Exception e) {
    		throw new Exception("genResponse: "+e.getMessage());
    	}
    }
    
}
