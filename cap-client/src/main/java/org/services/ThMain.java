package org.services;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThMain implements Runnable {
	Logger logger = Logger.getLogger("thMain");
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
			
			logger.info("Iniciando Ciclo Modulo Principal...");
			
			logger.info("Validando Ejecución de Thread de Servicios...");
			
			logger.info("Validando Thread Sync...");
			if (!gParams.getMapThreadRunnig().get("thSync")) {
				logger.info("Activando Thread Sync...");
	    		Runnable thSync = new ThSync(gParams);
				gParams.getMapThreadRunnig().put("thSync", true);
				gParams.getExecutorThSync().scheduleWithFixedDelay(thSync, 1000, 10000, TimeUnit.MILLISECONDS);
			} else {
				logger.info("Thread Sync esta en Ejecucion");
			}

			logger.info("Validando Thread Process...");
			if (!gParams.getMapThreadRunnig().get("thProcess")) {
				if (gParams.getService().getEnable()==1) {
					logger.info("Activando Thread Process...");
		    		Runnable thProcess = new ThProcess(gParams);
					gParams.getMapThreadRunnig().put("thProcess", true);
					gParams.getExecutorThProcess().scheduleWithFixedDelay(thProcess, 1000, 10000, TimeUnit.MILLISECONDS);
				} else {
					logger.info("thProcess se encuentra DISABLED");
				}
			} else {
				if (gParams.getService().getEnable()==1) {
					logger.info("Thread Process esta ENABLED y en Ejecucion");
				} else {
					logger.info("Thread Process ha cambiado a DISABLED");
					logger.info("Bajando Thread Process...");
					
					gParams.getMapThreadRunnig().put("thProcess", false);
					gParams.getExecutorThProcess().shutdown();
					
					while (!gParams.getExecutorThProcess().isShutdown()) {
						Thread.sleep(5);
					}
					logger.info("Thread Process ha sido bajado exitosamnte");
				}
			}
			
			logger.info("Finalizando Ciclo Modulo Principal");
		} catch (Exception e) {
			logger.error("Exception error en Ciclo Modulo Principal: "+e.getMessage());
		}
    }

}
