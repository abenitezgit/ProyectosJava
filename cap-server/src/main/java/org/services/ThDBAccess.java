package org.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.model.LogMessage;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThDBAccess implements Runnable{
	final String className = "thDBAccess";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ServiceControl sc;
		
	//Control de Ejecucion del servicio
	
	public ThDBAccess(GlobalParams m) {
		gParams = m;
		sc = new ServiceControl(gParams);
		//mylog = new MyLogger(gParams, className, "run()");
		//logger = mylog.getLogger();
	}
        
	public void run() {
		try {
			mylog = new MyLogger(gParams, className, "run()");
			logger = mylog.getLogger();
			mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
			
			//Declaraciones
			mylog.info("Iniciando Ciclo DB Access");
			
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
			 * Valida si el rol es Primary para Generar la escritura de logs en BD
			 */
			if (monRole.equals("PRIMARY")) {
				mylog.info("Recuperando Logs desde LinkedList...");
				List<LogMessage> lstLog = sc.getLogMessage();
				
				mylog.info("Se enviarán : "+lstLog.size()+" registros de Logs hacia BD...");
				
				sc.addMyLog(lstLog);
				mylog.info("Logs enviado Exitosamente a BD!");
			}
			
			mylog.info("Finalizando Ciclo DB Access");
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
    }
}
