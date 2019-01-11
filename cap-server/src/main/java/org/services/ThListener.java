package org.services;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;


public class ThListener implements Runnable{
	final String thName = "thListener";
	
	Logger logger; 
	MyLogger mylog;

	Rutinas mylib = new Rutinas();
	ServiceControl sc;
	FlowControl fc;
	DataAccess da;
	GlobalParams gParams;
	ExecutorService execWorker;
	
	public ThListener(GlobalParams m) {
		gParams = m;
		fc = new FlowControl(gParams);
		da = new DataAccess(gParams);
		sc = new ServiceControl(gParams);
	}
	
    @Override
    public void run() {
    	//Set Thread Name
    	Thread.currentThread().setName(thName);
    	
		mylog = new MyLogger(gParams, thName, "run()");
		logger = mylog.getLogger();
    	mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
    	
    	//Variables de Proceso
    	String monID = gParams.getAppConfig().getMonID();
    	
        try {
        	/**
        	 * Iniciando Thread Listener
        	 */
        	
        	//Inicializa Cola de trabajo.
        	execWorker = Executors.newFixedThreadPool(5);
        	
        	mylog.info("Iniciando Listener Server port: " + gParams.getMapMonParams().get(monID).getMonPort());

            @SuppressWarnings("resource")
			ServerSocket skServidor = new ServerSocket(Integer.valueOf(gParams.getMapMonParams().get(monID).getMonPort()));
            
            
            while (true) {
            	
            	mylog.info("Esperando transaccion...");
                Socket skCliente = skServidor.accept();
                
                mylog.info("Transacción recibido desde: "+skCliente.getInetAddress().getHostAddress());
                
                try {
                	mylog.info("Iniciando Listener worker...");
                	Runnable worker = new ThListenerClient(skCliente, gParams);
                	execWorker.execute(worker);
                	mylog.info("Listener worker iniciado exitosamente!");
                	
                } catch (Exception e) {
                	logger.error("No es posible atender una petición del Listener: "+e.getMessage());
                }
            }
            
        } catch (Exception  e) {
        	mylog.error("thListener(): "+e.getMessage());
        }
    }
}
