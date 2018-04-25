package cap.service;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import cap.utiles.GlobalParams;

public class ThProcess implements Runnable{
	static Logger logger = Logger.getLogger("ThProcess");
	static Rutinas mylib = new Rutinas();
	static GlobalParams gParams;
	
	//Control de Ejecucion del servicio
	static int TxP;
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
	        		
	        		logger.info("Iniciando Ciclo Sericios");
	        		
	        		/**
	        		 * Busca en BD grupos potenciales para activar
	        		 */
	        		sc.updateProcessPending();
	        		
	        		/**
	        		 * Recupera los parametros de configuración de cada proceso que aun lo lo tenga
	        		 */
	        		sc.updateProcessParams();
	        		
	        		/**
	        		 * Genera y/o actualiza la tabla de Task
	        		 */
	        		sc.updateTask();
	        		
	        		/**
	        		 * Assigna Nuevas Task a capClient disponibles para cuendo estos ejecuten el Sync se lleven las tareas que les corresponden
	        		 */
	        		//sc.assigneTask();
	        		
	        		/**
	        		 * Finalizando ciclo del Modulo
	        		 */
	        		logger.info("Terminado Ciclo Servicio");
	
	        	} catch (Throwable e) {
	        		logger.error(e.getMessage());
	        		logger.error(e.getStackTrace());
	        	}
    }
}
