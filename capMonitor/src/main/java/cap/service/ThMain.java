package cap.service;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import cap.utiles.GlobalParams;

public class ThMain implements Runnable{
	static Logger logger = Logger.getLogger("thMain");
	static Rutinas mylib = new Rutinas();
	static GlobalParams gParams;
	
	//Control de Ejecucion del servicio
	
	public ThMain(GlobalParams m) {
		gParams = m;
	}
        
	public void run() {
    	
    		
    		logger.info("Iniciando Ciclo MainController");
    		//Carga Metodos Externos
    		ServiceControl sc = new ServiceControl(gParams);
    	
        	try {
        		
        		/**
        		 * Analiza los Thread activos y los registra en map Local mapThread
        		 */
        		logger.info("Actualizando status Threads activos");
        		sc.listStatusThread();
        		
        		/**
        		 * Valida el tipo de ROL del capMonitor para determinar
        		 * los servicios internos que debe levantar
        		 */
        		logger.info("Validando Rol del Servicio en Metadata...");
        		String monRole = sc.getRoleService();
        		
        		logger.info("Rol del Servicio: "+monRole);
        		
        		
        		if (!monRole.equals("NOROLE") && !mylib.isNullOrEmpty(monRole)) {
        			gParams.getInfo().setMonRol(monRole);
        			sc.startupThreadsService(monRole);
        			
        		} else {
        			logger.warn("Servicio no puede ser iniciado como capMonitor");
        		}
        		
                
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.info("Terminado ciclo MainController");

        	} catch (Exception e) {
        		logger.error("thMain: "+e.getMessage());
        	}
    }
}
