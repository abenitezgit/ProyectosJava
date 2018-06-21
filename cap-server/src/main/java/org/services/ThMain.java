package org.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


public class ThMain implements Runnable{
	Logger logger = Logger.getLogger("thMain");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ServiceControl sc;
	
	ScheduledExecutorService execListener = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execSync = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execDBAccess = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService execProcess = Executors.newSingleThreadScheduledExecutor();
	
	//Control de Ejecucion del servicio
	
	public ThMain(GlobalParams m) {
		gParams = m;
		sc = new ServiceControl(gParams);
	}
        
	public void run() {
		final String module = "run()";
		final String logmsg = module + " - ";
		
		//Declaraciones
		String thName;
	
		logger.info(logmsg+"Iniciando Ciclo MainController");
		
		/**
		 * Recuperando MonParams
		 */
		try {
			logger.info(logmsg+"Recuperando MonParams...");
			sc.getMonParams();
			
			sc.getMonParams();
		} catch (Exception e) {
			logger.warn(logmsg+"No es posible recuperar monParams");
		}

		/**
		 * Valida el tipo de ROL del capMonitor para determinar
		 * los servicios internos que debe levantar
		 */
		logger.info(logmsg+"Validando Rol del Servicio...");
		String monRole = gParams.getMapMonParams().get(gParams.getMonID()).getMonRole();
		String monID = gParams.getMonID();
		
		logger.info(logmsg+"ID del Servicio: "+monID);
		logger.info(logmsg+"Rol del Servicio: "+monRole);

		/**
		 * Leyendo status de los Threads
		 */
		String thMainAction = gParams.getMapMonParams().get(monID).getThMainAction();
		String thListenerAction = gParams.getMapMonParams().get(monID).getThListenerAction();
		String thSyncAction = gParams.getMapMonParams().get(monID).getThSyncAction();
		String thDBAccessAction = gParams.getMapMonParams().get(monID).getThDBAccessAction();
		String thProcessAction = gParams.getMapMonParams().get(monID).getThProcessAction();
		
		logger.info(logmsg+"Informando Status de los Threads de Procesos");
		logger.info(logmsg+"Status Thread Main: "+thMainAction);
		logger.info(logmsg+"Status Thread Listener: "+thListenerAction);
		logger.info(logmsg+"Status Thread Sync: "+thSyncAction);
		logger.info(logmsg+"Status Thread DBAccess: "+thDBAccessAction);
		logger.info(logmsg+"Status Thread Process: "+thProcessAction);
		
		logger.info(logmsg+"Informando Startup de los Threads de Procesos");
		logger.info(logmsg+"Thread Main Started: "+gParams.getMapThreadRunnig().get("thMain"));
		logger.info(logmsg+"Thread Listener Started: "+gParams.getMapThreadRunnig().get("thListener"));
		logger.info(logmsg+"Thread Sync Started: "+gParams.getMapThreadRunnig().get("thSync"));
		logger.info(logmsg+"Thread DBAccess Started: "+gParams.getMapThreadRunnig().get("thDBAccess"));
		logger.info(logmsg+"Thread Process Started: "+gParams.getMapThreadRunnig().get("thProcess"));
		
		switch (thMainAction) {
    		case "ENABLE":
    			
    			//Inicia threads comunes
    			logger.info(logmsg+"Iniciando Threads de Procesos que no se han iniciado...");
    			
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
	    				logger.warn(logmsg+"Thread Listener se encuentra DISABLE");
	    				logger.warn(logmsg+"No es posible levantar Listener hasta que habilite proceso");
	    			}
    			
    			
    			//En base al role determina si levanta o no el thProcess
        		if (monRole.equals("PRIMARY")) {
        			logger.info(logmsg+"Iniciando Servicios asociados al rol: "+monRole);
        			
        			//Inicia thProcess
        			thName = "thProcess";
	    			if (gParams.getMapMonParams().get(monID).getThProcessAction().equals("ENABLE")) {
	    				if (!gParams.getMapThreadRunnig().get(thName)) {
	    	        		Runnable thProcess = new ThProcess(gParams);
	    	        		gParams.getMapThreadRunnig().put(thName, true);
	    	        		execProcess.scheduleWithFixedDelay(thProcess, 1000, gParams.getInfo().getTxpMain(), TimeUnit.MILLISECONDS);
	    	        		//execProcess.execute(thProcess);
	    				}
	    			} else 
		    			if (gParams.getMapMonParams().get(monID).getThProcessAction().equals("DISABLE")) {
		    				logger.warn(logmsg+"Thread Process se encuentra DISABLE");
		    				logger.warn(logmsg+"No es posible levantar Process hasta que habilite proceso");
		    			} 
        			
        			//Inicia thDBAccess
        			
        		} else if (monRole.equals("SECONDARY")) {
        			logger.info(logmsg+"Iniciando Servicios asociados al rol: "+monRole);
        		} else {
        			logger.error(logmsg+"Servicio sin Role definido");
        			logger.error(logmsg+"Abortando Startup de Servicios");
        			System.exit(0);
        		}

    			break;
    		case "DISABLE":
    			/**
    			 * Si thMain es puesto en DISABLE
    			 */
    			logger.info(logmsg+"Main Cotroller se encuentra DISABLED");
    			logger.info(logmsg+"Todos los thread de servicios seran suspendidos");
    			
    			break;
    		case "SHUTDOWN":
    			logger.info(logmsg+"Main Controller se encuentra en SHUTDOWN");
    			logger.info(logmsg+"Todos los thread de servicios seran bajados y la aplicacion sera finalizada");
    			break;
		}
		
		logger.info(logmsg+"Finalizando Ciclo Main Controller");
    }
}
