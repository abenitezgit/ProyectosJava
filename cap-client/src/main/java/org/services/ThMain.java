package org.services;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThMain implements Runnable {
	Logger logger = Logger.getLogger("thMain");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ServiceControl sc;
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThMain(GlobalParams m) {
		gParams = m;
		sc = new ServiceControl(gParams);
//		myproc = new Procedure(gDatos);
//		logger = gDatos.getLogger();
	}
	
    @Override
	public void run() {
		try {
			logger.info("Iniciando Ciclo Modulo Principal...");
			logger.info("Analizando Status del Servicio...");
			
			//Consulta datos del Servicio
			//Obtiene los tipos de proceso que puede ejecutar, y la cantidad de thread maximos
			//Lee desde la base de datos si esta Enable, trae la lista de typeProc y cliProc
			
			sc.syncServiceParams(gParams.getSrvID());
			
			//Muestra los parametros retornados
			sc.showSrvParams();
			
			
		} catch (Exception e) {
			logger.error("Exception error en thMain: "+e.getMessage());
		}
    }

}
