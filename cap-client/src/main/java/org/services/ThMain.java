package org.services;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThMain implements Runnable {
	final String thName = "thMain";
	Logger logger = Logger.getLogger(thName);
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	public ThMain(GlobalParams m) {
		gParams = m;
	}
	
    @Override
	public void run() {
		try {
			//Set LogLevel
			mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
			
			//Set Thread Name
			Thread.currentThread().setName(thName);
			
			logger.info("Iniciando Ciclo Modulo Principal...");
			
			logger.info("Validando Ejecución de Thread de Servicios...");
			
			/**
			 * Analiza los Thread activos y los registra en map Local mapThread
			 */
			listStatusThread();
			
			String admThread = "thSync";
			logger.info("Validando Inicialización de Thread "+admThread);
			if (getTotalThread(admThread)==0) {
				logger.info("Activando Thread "+admThread);
	    		Runnable thSync = new ThSync(gParams);
				gParams.getExecutorThSync().scheduleWithFixedDelay(thSync, 1000, 10000, TimeUnit.MILLISECONDS);
			}
			
			admThread = "thProcess";
			logger.info("Validando Inicialización de Thread "+admThread);
			if (getTotalThread(admThread)==0) {
				logger.info("Activando Thread "+admThread);
	    		Runnable thProcess = new ThProcess(gParams);
				gParams.getExecutorThProcess().scheduleWithFixedDelay(thProcess, 1000, 10000, TimeUnit.MILLISECONDS);
			} 

			logger.info("Finalizando Ciclo Modulo Principal");
		} catch (Exception e) {
			logger.error("Exception error en Ciclo Modulo Principal: "+e.getMessage());
		}
    }
    
	public void listStatusThread() {
		try {
			
            Thread th = Thread.currentThread();
            
            logger.info("This Thread Name: "+th.getName()+ " Status: " + th.getState() + " ID: "+ th.getId()+  " LastCheck"+ mylib.getDateNow());
            
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for ( Thread t : threadSet){
                logger.info(" Thread Name: " + t.getName()+ " Status:" + t.getState()+" ID: "+t.getId());
            }
			
		} catch (Exception e) {
			logger.error("listStatusThread: "+e.getMessage());
		}
	}

	public int getTotalThread(String thName) {
		try {
			
			int numThread = 0;
			
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for ( Thread t : threadSet){
            	if (t.getName().equals(thName)) {
            		numThread++;
            	}
            }
            
            return numThread;
		} catch (Exception e) {
			logger.error("getTotalThread: "+e.getMessage());
			return 0;
		}
	}


}
