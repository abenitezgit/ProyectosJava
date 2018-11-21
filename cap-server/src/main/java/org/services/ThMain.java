package org.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThMain implements Runnable{
	final String className = "thMain";
	
	Logger logger; 
	MyLogger mylog;
	
	//Logger logger = Logger.getLogger("thMain");
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
		//mylog = new MyLogger(gParams, className, "run()");
		//logger = mylog.getLogger();
	}
        
	public void run() {
		mylog = new MyLogger(gParams, className, "run()");
		logger = mylog.getLogger();
		mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
		
		//Declaraciones
		String thName;
	
		mylog.info("Iniciando Ciclo MainController");
		
		/**
		 * Recuperando MonParams
		 */
		try {
			mylog.info("Recuperando MonParams...");
			sc.getMonParams();
			
			sc.getMonParams();
		} catch (Exception e) {
			mylog.warn("No es posible recuperar monParams");
		}

		/**
		 * Valida el tipo de ROL del capMonitor para determinar
		 * los servicios internos que debe levantar
		 */
		mylog.info("Validando Rol del Servicio...");
		String monID = gParams.getAppConfig().getMonID();
		String monRole = gParams.getMapMonParams().get(monID).getMonRole();
		
		mylog.info("ID del Servicio: "+monID);
		mylog.info("Rol del Servicio: "+monRole);

		/**
		 * Leyendo status de los Threads
		 */
		String thMainAction = gParams.getMapMonParams().get(monID).getThMainAction();
		String thListenerAction = gParams.getMapMonParams().get(monID).getThListenerAction();
		String thSyncAction = gParams.getMapMonParams().get(monID).getThSyncAction();
		String thDBAccessAction = gParams.getMapMonParams().get(monID).getThDBAccessAction();
		String thProcessAction = gParams.getMapMonParams().get(monID).getThProcessAction();
		
		mylog.info("Informando Status de los Threads de Procesos");
		mylog.info("Status Thread Main: "+thMainAction);
		mylog.info("Status Thread Listener: "+thListenerAction);
		mylog.info("Status Thread Sync: "+thSyncAction);
		mylog.info("Status Thread DBAccess: "+thDBAccessAction);
		mylog.info("Status Thread Process: "+thProcessAction);
		
		mylog.info("Informando Startup de los Threads de Procesos");
		mylog.info("Thread Main Started: "+gParams.getMapThreadRunnig().get("thMain"));
		mylog.info("Thread Listener Started: "+gParams.getMapThreadRunnig().get("thListener"));
		mylog.info("Thread Sync Started: "+gParams.getMapThreadRunnig().get("thSync"));
		mylog.info("Thread DBAccess Started: "+gParams.getMapThreadRunnig().get("thDBAccess"));
		mylog.info("Thread Process Started: "+gParams.getMapThreadRunnig().get("thProcess"));
		
		switch (thMainAction) {
    		case "ENABLE":
    			
    			//Inicia threads comunes
    			mylog.info("Iniciando Threads de Procesos que no se han iniciado...");
    			
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
	    				mylog.warn("Thread Listener se encuentra DISABLE");
	    				mylog.warn("No es posible levantar Listener hasta que habilite proceso");
	    			}
    			
    			
    			//En base al role determina si levanta o no el thProcess
        		if (monRole.equals("PRIMARY")) {
        			mylog.info("Iniciando Servicios asociados al rol: "+monRole);
        			
        			//Inicia thProcess
        			thName = "thProcess";
	    			if (gParams.getMapMonParams().get(monID).getThProcessAction().equals("ENABLE")) {
	    				if (!gParams.getMapThreadRunnig().get(thName)) {
	    	        		Runnable thProcess = new ThProcess(gParams);
	    	        		gParams.getMapThreadRunnig().put(thName, true);
	    	        		execProcess.scheduleWithFixedDelay(thProcess, 1000, gParams.getAppConfig().getTxpMain(), TimeUnit.MILLISECONDS);
	    	        		//execProcess.execute(thProcess);
	    				}
	    			} else 
		    			if (gParams.getMapMonParams().get(monID).getThProcessAction().equals("DISABLE")) {
		    				mylog.warn("Thread Process se encuentra DISABLE");
		    				mylog.warn("No es posible levantar Process hasta que habilite proceso");
		    			} 
        			
        			//Inicia thDBAccess
        			thName = "thDBAccess";
	    			if (gParams.getMapMonParams().get(monID).getThDBAccessAction().equals("ENABLE")) {
	    				if (!gParams.getMapThreadRunnig().get(thName)) {
	    	        		Runnable thDBAccess = new ThDBAccess(gParams);
	    	        		gParams.getMapThreadRunnig().put(thName, true);
	    	        		execDBAccess.scheduleWithFixedDelay(thDBAccess, 1000, gParams.getMapMonParams().get(monID).getTxpSync(), TimeUnit.MILLISECONDS);
	    				}
	    			} else 
		    			if (gParams.getMapMonParams().get(monID).getThProcessAction().equals("DISABLE")) {
		    				mylog.warn("Thread DBAccess se encuentra DISABLE");
		    				mylog.warn("No es posible levantar DBAccess hasta que habilite proceso");
		    			} 
	    			
	    			//Inicia TimerExecAgeWeek
	    			thName = "TimerExecWeek";
	    			if (!gParams.getMapThreadRunnig().get(thName)) {
		    			Date horaExec = new Date(System.currentTimeMillis());
		    	        
		    	        Calendar c = Calendar.getInstance();
		    	        c.setTime(horaExec);
		    	        System.out.println(c.get(Calendar.DAY_OF_WEEK));
		    	        // Si la hora es posterior a las 8am se programa la alarma para el dia siguiente
		    	        if (c.get(Calendar.HOUR_OF_DAY) >= 1) {
		    	            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
		    	        }
		    	        
		    	        c.set(Calendar.HOUR_OF_DAY, 0);
		    	        c.set(Calendar.MINUTE, 15);
		    	        c.set(Calendar.SECOND, 0);
		    	        
		    	        horaExec = c.getTime();
		    	        // El despertador suena cada 24h (una vez al dia)
		    	        int tiempoRepeticion = 86400000; 
		    	        
		    	        // Programamos el despertador para que "suene" a las 8am todos los dias 
		    	        Timer TempoExec = new Timer(thName);
		    	        gParams.getMapThreadRunnig().put(thName, true);
		    	        TempoExec.schedule(new TimerExecAgeWeek(gParams), horaExec, tiempoRepeticion);
	    			}
        			
        		} else if (monRole.equals("SECONDARY")) {
        			mylog.info("Iniciando Servicios asociados al rol: "+monRole);
        		} else {
        			mylog.error("Servicio sin Role definido");
        			mylog.error("Abortando Startup de Servicios");
        			System.exit(0);
        		}

    			break;
    		case "DISABLE":
    			/**
    			 * Si thMain es puesto en DISABLE
    			 */
    			mylog.info("Main Cotroller se encuentra DISABLED");
    			mylog.info("Todos los thread de servicios seran suspendidos");
    			
    			break;
    		case "SHUTDOWN":
    			mylog.info("Main Controller se encuentra en SHUTDOWN");
    			mylog.info("Todos los thread de servicios seran bajados y la aplicacion sera finalizada");
    			break;
		}
		
		mylog.info("Finalizando Ciclo Main Controller");
    }
}
