package org.services;


import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThProcess implements Runnable {
	Logger logger = Logger.getLogger("thProcess");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ServiceControl sc;
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThProcess(GlobalParams m) {
		gParams = m;
		sc = new ServiceControl(gParams);
	}
	
    @Override
	public void run() {
		try {
			//Set LogLevel
			mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
			
			logger.info("Iniciando Ciclo thProcess...");
			
			if (gParams.getService().getEnable()==1) {
				sc.executeTask();
			}

			sc.showTaskProcess();
			
			
			logger.info("Finalizando Ciclo de Thead Process...");
		} catch (Exception e) {
			logger.error("Exception error en Thead Process: "+e.getMessage());
		}
    }

}
