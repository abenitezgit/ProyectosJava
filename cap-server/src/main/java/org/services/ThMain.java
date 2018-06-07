package org.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


public class ThMain implements Runnable{
	Logger logger = Logger.getLogger("thMain");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	ScheduledExecutorService execListener = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execSync = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execDBAccess = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execProcess = Executors.newSingleThreadScheduledExecutor();
	
	//Control de Ejecucion del servicio
	
	public ThMain(GlobalParams m) {
		gParams = m;
	}
        
	public void run() {
		
			//Declaraciones
			String thName;
    	
    		logger.info("Iniciando Ciclo MainController");

    		/**
    		 * Valida el tipo de ROL del capMonitor para determinar
    		 * los servicios internos que debe levantar
    		 */
    		logger.info("Validando Rol del Servicio...");
    		String monRole = gParams.getMapMonParams().get(gParams.getMonID()).getMonRole();
    		String monID = gParams.getMonID();
    		
    		logger.info("ID del Servicio: "+monID);
    		logger.info("Rol del Servicio: "+monRole);

    		/**
    		 * Leyendo status de los Threads
    		 */
    		String thMainAction = gParams.getMapMonParams().get(monID).getThMainAction();
    		String thListenerAction = gParams.getMapMonParams().get(monID).getThListenerAction();
    		String thSyncAction = gParams.getMapMonParams().get(monID).getThSyncAction();
    		String thDBAccessAction = gParams.getMapMonParams().get(monID).getThDBAccessAction();
    		String thProcessAction = gParams.getMapMonParams().get(monID).getThProcessAction();
    		
    		logger.info("Informando Status de los Threads de Procesos");
    		logger.info("Status Thread Main: "+thMainAction);
    		logger.info("Status Thread Listener: "+thListenerAction);
    		logger.info("Status Thread Sync: "+thSyncAction);
    		logger.info("Status Thread DBAccess: "+thDBAccessAction);
    		logger.info("Status Thread Process: "+thProcessAction);
    		
    		logger.info("Informando Startup de los Threads de Procesos");
    		logger.info("Thread Main Started: "+gParams.getMapThreadRunnig().get("thMain"));
    		logger.info("Thread Listener Started: "+gParams.getMapThreadRunnig().get("thListener"));
    		logger.info("Thread Sync Started: "+gParams.getMapThreadRunnig().get("thSync"));
    		logger.info("Thread DBAccess Started: "+gParams.getMapThreadRunnig().get("thDBAccess"));
    		logger.info("Thread Process Started: "+gParams.getMapThreadRunnig().get("thProcess"));
    		
    		switch (thMainAction) {
	    		case "ENABLE":
	    			
	    			//Inicia threads comunes
	    			logger.info("Iniciando Threads de Procesos que no se han iniciado...");
	    			
	    			//Inicia thListener
	    			thName = "thListener";
	    			if (gParams.getMapMonParams().get(monID).getThListenerAction().equals("ENABLE")) {
	    				if (!gParams.getMapThreadRunnig().get(thName)) {
	    	        		Runnable thListener = new ThListener(gParams);
	    	        		gParams.getMapThreadRunnig().put(thName, true);
	    	        		execListener.execute(thListener);
	    				}
	    			} else 
		    			if (gParams.getMapMonParams().get(monID).getThListenerAction().equals("DISABLE")) {
		    				logger.warn("Thread Listener se encuentra DISABLE");
		    				logger.warn("No es posible levantar Listener hasta que habilite proceso");
		    			} 
	    			
	    			
	    			//En base al role determina si levanta o no el thProcess
	        		if (monRole.equals("PRIMARY")) {
	        			logger.info("Iniciando Servicios asociados al rol: "+monRole);
	        			
	        			//Inicia thProcess
	        			
	        			
	        			//Inicia thDBAccess
	        			
	        			
	        			
	        		} else if (monRole.equals("SECONDARY")) {
	        			logger.info("Iniciando Servicios asociados al rol: "+monRole);
	        		} else {
	        			logger.error("Servicio sin Role definido");
	        			logger.error("Abortando Startup de Servicios");
	        			System.exit(0);
	        		}
	
	    			break;
	    		case "DISABLE":
	    			/**
	    			 * Si thMain es puesto en DISABLE
	    			 */
	    			logger.info("Main Cotroller se encuentra DISABLED");
	    			logger.info("Todos los thread de servicios seran suspendidos");
	    			
	    			break;
	    		case "SHUTDOWN":
	    			logger.info("Main Controller se encuentra en SHUTDOWN");
	    			logger.info("Todos los thread de servicios seran bajados y la aplicacion sera finalizada");
	    			break;
    		}
    		
    		logger.info("Finalizando Ciclo Main Controller");
    }
}
