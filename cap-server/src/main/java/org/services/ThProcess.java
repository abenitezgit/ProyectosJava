package org.services;

import org.apache.log4j.Logger;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ThProcess implements Runnable{
	final static String thName = "thProcess";
	Logger logger = Logger.getLogger(thName);
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	public ThProcess(GlobalParams m) {
		logger.info("Iniciando constructor del servicio");
		gParams = m;
	}
	
    @Override
    public void run() {
		//Set Thread Name
		Thread.currentThread().setName(thName);
		
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
    		if (gParams.isSwFindNewGroup()) {
    			sc.findNewGroupProcess();
    		} else {
    			logger.warn("La búsqueda de nuevos Grupos de Proceso está deshabilitada");
    		}
    		
    		/**
    		 * Recupera los parametros de configuración de cada proceso que aun lo lo tenga
    		 */
    		logger.info("Actualizando Parametros asociados a nuevos procesos...");
    		if (gParams.isSwFindNewGroup()) {
    			sc.updateProcessParams();
    		} else {
    			logger.warn("La búsqueda de parametros de Proceso está deshabilitada");
    		}
    		
    		/**
    		 * Genera y/o actualiza la tabla de Task
    		 */
    		logger.info("Asignando TASK a procesos en estado PENDING...");
    		if (gParams.isSwAssignNewTask()) {
    			sc.assignNewTask();
    		} else {
    			logger.warn("La asignacion de nuevos Task está deshabilitada");
    		}
    		
    		/**
    		 * Muestra Task y Procesos activos
    		 */
    		//sc.showMapProcControl();
    		sc.showMapTask();
    		
    		/**
    		 * Cancela Procesos que se hayan requerido
    		 */
    		try {
    			sc.forceCancelProcess();
    		} catch (Exception e) {
    			logger.error("No es posible Cancelar Procesos: "+e.getMessage());
    		}
    		
    		/**
    		 * Actualización de Procesos Finalizados en Metadata
    		 */
    		sc.updateTaskFinished();
    		
    		/**
    		 * Finalizando ciclo del Modulo
    		 */
    		logger.info("Terminado Ciclo Servicio");

    	} catch (Exception e) {
    		logger.error(e.getMessage());
    		logger.error(e.getStackTrace());
    	}
    }
}
