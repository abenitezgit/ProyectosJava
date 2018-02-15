package cap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import cap.DataAccess.DataAccess;
import cap.model.PGPending;
import cap.model.ProcControl;
import cap.utiles.FlowControl;
import cap.utiles.GlobalParams;

public class ServiceControl {
	Logger logger = Logger.getLogger("ServiceControl");
	GlobalParams gParams;
	FlowControl fc;
	DataAccess dc;
	
	//Set Runnable Threads
	Runnable thProc;
	Runnable thListener;
	
	public ServiceControl(GlobalParams m) {
		gParams = m;
		fc = new FlowControl(gParams);
		dc = new DataAccess(gParams);
		thProc = new ThProcess(gParams);
		thListener = new ThListener(gParams);
	}
	
	/**
	 * thProcess
	 */
	public void updateProcessPending()  {
		try {
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.getActiveGroup();
			
			//Actualiza la Tabla de status de proceso global
			fc.updateMapProcControl(mapPg);
			
		} catch (Exception e) {
			logger.error("updateProcessPending: "+e.getMessage());
		}
	}

	public void updateProcessParams() {
		try {
			/**
			 * Recorre la lista de procesos globales recuperando la configuracion de cada proceso como objeto asociado a params()
			 */
			//Map<String, ProcControl> mappc = new HashMap<>(gParams.getMapProcControl());
			Map<String, ProcControl> mappc = gParams.getMapProcControl().entrySet().stream().filter(p -> p.getValue().getStatus().equals("PENDING")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			for(Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
				
				//Busca params solo si aun lo lo ha hecho
				if (entry.getValue().getParam()==null) {
					
					String key = entry.getKey();
					Object params = new Object();
					params = dc.getProcessParam(entry.getValue().getProcID(), entry.getValue().getTypeProc());
					
					if (params!=null) {
						fc.updateMapProcControl(key, params);
					} else {
						fc.updateMapProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
						dc.updateProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("updateProcessConfig: "+e.getMessage());
		}
	}
	
	public void updateTask() {
		try {
			/**
			 * Crea las Task correspondientes
			 * Recupera todos los procesos en estado PENDING para asignar Task
			 */
			Map<String, ProcControl> mappc = gParams.getMapProcControl().entrySet().stream().filter(p -> p.getValue().getStatus().equals("PENDING")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			//Busca para cada Proceso encontrado una lista de servicios disponibles para ejecutar el proceso
			for(Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
				
				//Valida si hay servicios disponibles para ejecutar procesos del cliente y del TypeProc
				List<String> lstServices = new ArrayList<>();
				lstServices = fc.isServiceAvailable(entry.getValue().getCliID(), entry.getValue().getTypeProc());
					
				if (lstServices.size()>0) {
					logger.info("Existen servicios disponibles para ejecutar proceso: "+entry.getKey());
					
					/**
					 * Debe seleccionar el servicio disponible de la lista encontrada si es que hay mas de uno
					 */
					String srvID;
					if (lstServices.size()==0) {
						srvID = lstServices.get(0);
					} else {
						//Hay mas servicios disponibles
						//Seleccionar uno en base a algun criterio
						srvID = lstServices.get(0);
					}
					
					//Se crea la Task asignandole un srvID y se cambia el status a READY
					
					
				
				} else {
					logger.info("No hay servcios disponibles para ejecutar proceso: "+entry.getKey());
				}
			}
			
		} catch (Exception e) {
			logger.error("updateTask: "+e.getMessage());
		}
	}

	public void listStatusThread() {
		try {
			
            //Thread tr = Thread.currentThread();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            //logger.info("Current Thread: "+tr.getName()+" ID: "+tr.getId());
            for ( Thread t : threadSet){
                logger.info("Thread :"+t+":"+"state:"+t.getState()+" ID: "+t.getId());
            }
			
		} catch (Exception e) {
			logger.error("listStatusThread: "+e.getMessage());
		}
	}
	
	public String getRoleService() {
		try {
			return dc.getRol(gParams.getInfo().getMonID());
		} catch (Exception e) {
			logger.error("getRoleService: "+e.getMessage());
			return null;
		}
	}
	
	public void startupThreadsService(String monRole) {
		String threadName;
		try {
			switch(monRole) {
			case "PRIMARY":
				fc.setEnablePrimaryThread();
				break;
			case "SECONDARY":
				fc.setEnableSecondaryThread();
				break;
			}
		
	    		/**
	    		 * Levanta thListener
	    		 */
		    threadName = "thListener";
		    logger.info("Starting Thread: "+threadName);
		    if (fc.isEnableThread(threadName)) {
		        try {
		        		if (!gParams.getMapExecutors().containsKey(threadName)) {
		        			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		        			executorService.execute(thListener);
		        			gParams.getMapExecutors().put(threadName, executorService);
		        			logger.info("Tread: "+threadName+ " subido correctamente!!");
		        		} else {
		        			if (gParams.getMapExecutors().get(threadName).isShutdown()) {
			        			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			        			executorService.execute(thListener);
			        			gParams.getMapExecutors().put(threadName, executorService);
			        			logger.info("Tread: "+threadName+ " subido correctamente!!");
		        			} else {
		        				logger.info("Tread: "+threadName+ " ya se encuentra Activo!!");
		        			}
		        		}
		        } catch (Exception e) {
		            logger.error("Error al Iniciar Thread: "+threadName+" ("+e.getMessage()+")");
		        }
		    } else {
		    		if (gParams.getMapExecutors().containsKey(threadName)) {
		    			if (!gParams.getMapExecutors().get(threadName).isShutdown()) {
		    				logger.info("Procediendo a bajar Thread: "+threadName+"...");
		    				gParams.getMapExecutors().get("thProcess").shutdown();
		    			}
		    		}
		    }
		
	    		/**
	    		 * Levanta thProcess
	    		 */
		    threadName = "thProcess";
		    int txpProcess = gParams.getInfo().getTxpProcess();
		    logger.info("Starting Thread: "+threadName);
		    if (fc.isEnableThread(threadName)) {
		        try {
		        		if (!gParams.getMapExecutors().containsKey(threadName)) {
		        			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		        			executorService.scheduleWithFixedDelay(thProc, 1000, txpProcess, TimeUnit.MILLISECONDS);
		        			gParams.getMapExecutors().put(threadName, executorService);
		        			logger.info("Tread: "+threadName+ " subido correctamente!!");
		        		} else {
		        			if (gParams.getMapExecutors().get(threadName).isShutdown()) {
			        			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			        			executorService.scheduleWithFixedDelay(thProc, 1000, txpProcess, TimeUnit.MILLISECONDS);
			        			gParams.getMapExecutors().put(threadName, executorService);
			        			logger.info("Tread: "+threadName+ " subido correctamente!!");
		        			} else {
		        				logger.info("Tread: "+threadName+ " ya se encuentra Activo!!");
		        			}
		        		}
		        } catch (Exception e) {
		            logger.error("Error al Iniciar Thread: "+threadName+" ("+e.getMessage()+")");
		        }
		    } else {
		    		if (gParams.getMapExecutors().containsKey(threadName)) {
		    			if (!gParams.getMapExecutors().get(threadName).isShutdown()) {
		    				logger.info("Procediendo a bajar Thread: "+threadName+"...");
		    				gParams.getMapExecutors().get("thProcess").shutdown();
		    			}
		    		}
		    }
    
		} catch (Exception e) {
			logger.error("startupThreadsService: "+e.getMessage());
		}
	}
	
	public boolean isThreadEnable(String thName) {
		try {
			return fc.isEnableThread(thName);
		} catch (Exception e) {
			logger.error("isThreadEnable: "+e.getMessage());
			return false;
		}
	}
}
