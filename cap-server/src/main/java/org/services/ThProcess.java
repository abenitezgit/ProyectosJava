package org.services;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThProcess implements Runnable{
	Logger logger = Logger.getLogger("ThProcess");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	//Control de Ejecucion del servicio
	final static String thName = "thProcess";
	
	public ThProcess(GlobalParams m) {
		logger.info("Iniciando constructor del servicio");
		gParams = m;
	}
	
    @Override
        public void run() {
        		ServiceControl sc = new ServiceControl(gParams);
	        	try {
	        		/**
	        		 * Inicia ciclo del Modulo
	        		 */
	        		
	        		logger.info("Iniciando Ciclo Modulo Process");
	        		
	        		/**
	        		 * Busca en BD grupos potenciales para activar
	        		 */
	        		logger.info("Buscando en Metadata Grupos potenciales para activar...");
	        		sc.updateProcessPending();
	        		
	        		/**
	        		 * Recupera los parametros de configuración de cada proceso que aun lo lo tenga
	        		 */
	        		logger.info("Actualizando Parametros asociados a nuevos procesos...");
	        		sc.updateProcessParams();
	        		
	        		/**
	        		 * Genera y/o actualiza la tabla de Task
	        		 */
	        		logger.info("Asignando TASK a procesos en estado PENDING...");
	        		sc.updateTask();
	        		
	        		/**
	        		 * Assigna Nuevas Task a capClient disponibles para cuendo estos ejecuten el Sync se lleven las tareas que les corresponden
	        		 */
	        		sc.showMapProcControl();
	        		
	        		sc.showMapTask();
	        		
	        		/**
	        		 * Finalizando ciclo del Modulo
	        		 */
	        		logger.info("Terminado Ciclo Servicio");
	        		logger.info("Ciclón Ñandú");
	
	        	} catch (Throwable e) {
	        		logger.error(e.getMessage());
	        		logger.error(e.getStackTrace());
	        	}
    }
}
