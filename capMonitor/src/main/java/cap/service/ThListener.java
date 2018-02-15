package cap.service;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.JSONObject;


import cap.utiles.GlobalParams;
import cap.utiles.FlowControl;
import utiles.common.rutinas.Rutinas;

public class ThListener implements Runnable{
	static Logger logger = Logger.getLogger("thListener");
	static Rutinas mylib = new Rutinas();
	static GlobalParams gParams;
	static FlowControl fc;
	
	public ThListener(GlobalParams m) {
		gParams = m;
		fc = new FlowControl(gParams);
	}
	
    @Override
    public void run() {
        try {
            	/**
            	 * Iniciando Thread Listener
            	 */
            logger.info("Iniciando Listener Server port: " + fc.getListenerPort());

            @SuppressWarnings("resource")
			ServerSocket skServidor = new ServerSocket(fc.getListenerPort());
            String inputData;
            String outputData = null;
            String dRequest;
            String dAuth;
            JSONObject jHeader;
            JSONObject jData;
            
            while (true) {
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
}
