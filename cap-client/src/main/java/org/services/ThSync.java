package org.services;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThSync implements Runnable {
	Logger logger = Logger.getLogger("thSync");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ServiceControl sc;
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThSync(GlobalParams m) {
		gParams = m;
		sc = new ServiceControl(gParams);
	}
	
    @Override
	public void run() {
		try {
			//Set LogLevel
			mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());

			logger.info("Iniciando Ciclo de Thread Sync...");

			logger.info("Actualizando parametros del Servicio...");
			try {
				sc.syncServiceParams(gParams.getAppConfig().getSrvID());
			} catch (Exception e) {
				logger.error("No es posible actualizar parametros de servicio");
			}
			
			logger.info("Actualizando Task Process...");
			try {
				sc.syncTaskProcess();
			} catch (Exception e) {
				logger.error("No es posible actualizar Task Process");
			}
			
			//Muestra los parametros retornados
			sc.showSrvParams();
			
			sc.showTaskProcess();
			
			logger.info("Finalizando Ciclo de Thread Sync...");
		} catch (Exception e) {
			logger.error("Exception error en Thread Sync: "+e.getMessage());
		}
    }

}
