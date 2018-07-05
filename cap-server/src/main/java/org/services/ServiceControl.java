package org.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.Dependence;
import org.model.MonParams;
import org.model.PGPending;
import org.model.ProcControl;
import org.model.Service;
import org.model.Task;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ServiceControl {
	Logger logger = Logger.getLogger("ServiceControl");
	Rutinas mylib = new Rutinas();
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
	}
	
	/**
	 * thProcess
	 */
	
	public void showMapProcControl() {
		logger.info("Detalle de Procesos");
		for (Map.Entry<String, ProcControl> entry : gParams.getMapProcControl().entrySet()) {
			logger.info("--> "+entry.getKey()+" "+entry.getValue().getStatus()+" "+entry.getValue().getuStatus()+" "+entry.getValue().getErrCode()+" "+entry.getValue().getErrMesg());
		}
	}
	
	public void showMapTask() throws Exception {
		logger.info("Detalle de Task");
		for (Map.Entry<String, Task> entry : gParams.getMapTask().entrySet()) {
			logger.info("--> "+entry.getKey()+" "+entry.getValue().getStatus()+" "+entry.getValue().getuStatus()+" "+entry.getValue().getErrCode()+" "+entry.getValue().getErrMesg());
		}
	}
	
	public String syncTaskProcess(JSONObject data) throws Exception {
		try {
			
			String srvID = data.getString("srvID");
			String strMapTask = data.getString("mapTask");
			
			//Actualizando mapTask con datos desde cap-client
			fc.updateTaskProcess(strMapTask);
			
			//Recuperando datos actualizados para enviar al cap-client
			
			Map<String, Task> mapTask = fc.getServiceMapTask(srvID);
			
			if (mapTask.size()>0) {
				strMapTask = mylib.serializeObjectToJSon(mapTask, false);
			}
			
			return strMapTask;
		} catch (Exception e) {
			throw new Exception("syncTaskProcess(): "+e.getMessage());
		}
	}
	
	public String syncServiceParams(JSONObject data) throws Exception{
		try {
			String strService="";
			
			//Recupera los parametros de servicio desde Metadata
			
			String srvID = data.getString("srvID");
			
			logger.info("Iniciando SyncServiceParams para cap-client: "+srvID);
			
			logger.info("Recuperando serviceParams desde Metadata...");
			String response = dc.getServiceParam(srvID);
			logger.info("Respuesta desde Metadata: "+response);
			
			//Actualiza mapService desde Metadata
			logger.info("Actualizando mapService desde Metadata...");
			fc.updateMapServiceFromMD(response);
			
			//Actualiza mapService desde cap-client
			try {
				logger.info("Actualizando mapService desde cap-client...");
				Service service = (Service) mylib.serializeJSonStringToObject(data.getString("service"), Service.class);
				fc.updateMapServiceFromCC(service);
			} catch (Exception e) {
				logger.warn("No se actualizara mapService con datos de cap-client: "+e.getMessage());
			}
			
			strService = mylib.serializeObjectToJSon(gParams.getMapService().get(srvID), false);
			if (mylib.isNullOrEmpty(strService)) {
				throw new Exception("getServiceParams(): No hay datos para el servicio "+gParams.getMapService().get(srvID));
			}
			
			logger.info("Se ha actualizado mapService!");
			logger.info("Data Service: "+strService);
			return strService;
		} catch (Exception e) {
			throw new Exception("getServiceParams(): "+e.getMessage());
		}
	}
	
	public void updateProcessPending()  {
		try {
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.getActiveGroup();
			
			logger.info("Total de Procesos encontrados para activar: "+mapPg.size());
			
			if (mapPg.size()>0) {
				for(Map.Entry<String, PGPending> entry : mapPg.entrySet()) {
					logger.debug("Proceso leido desde Metadata: "+entry.getKey());
				}
			}
			
			//Actualiza la Tabla de status de proceso global
			logger.info("Actualizando Control de Procesos locales...");
			fc.updateMapProcControl(mapPg);
			
		} catch (Exception e) {
			logger.error("updateProcessPending: "+e.getMessage());
		}
	}

	public void updateProcessParams() {
		final String module = "updateProcessParams()";
		final String logmsg = module+" - ";
		
		try {
			/**
			 * Recorre la lista de procesos globales recuperando la configuracion de cada proceso como objeto asociado a params()
			 */
			//Map<String, ProcControl> mappc = new HashMap<>(gParams.getMapProcControl());
			logger.info(logmsg+"Buscando procesos PENDING que no tengan parametros asociados...");
			Map<String, ProcControl> mappc = gParams.getMapProcControl().entrySet().stream().filter(p -> p.getValue().getStatus().equals("PENDING")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			logger.info(logmsg+"Se encontraton "+mappc.size()+" procesos en estado PENDING");
			
			if (mappc.size()>0) {
			
				for(Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
					
					logger.info(logmsg+"Actualizando parametros a proceso: "+entry.getKey());
					
					//Busca params solo si aun lo lo ha hecho
					if (entry.getValue().getParam()==null) {
						
						String key = entry.getKey();
						Object params = new Object();
						
						logger.info(logmsg+"Buscando parametros en MD proceso :"+entry.getValue().getProcID());
						try {
							params = dc.getProcessParam(entry.getValue().getProcID(), entry.getValue().getTypeProc());

							if (params!=null) {
								logger.info(logmsg+"Actualizando Clase local con parametros encontrados...");
								fc.updateMapProcControl(key, params);

								logger.info(logmsg+"Buscando dependencia del proceso: "+entry.getValue().getProcID());
								try {
									List<Dependence> lstDep = new ArrayList<>();
									lstDep = dc.getProcDependences(entry.getValue().getGrpID(), entry.getValue().getProcID());
									
									if (lstDep.size()>0) {
										logger.info(logmsg+"Actualizando Dependencias del Proceso: "+entry.getValue().getProcID());
										fc.updateProcDependence(key, lstDep);
									} else {
										logger.info(logmsg+"El proceso "+entry.getValue().getProcID()+" no posee dependencias informadas");
									}
									
								} catch (Exception e) {
									logger.error(logmsg+"Error buscando dependencia del proceso: "+entry.getValue().getProcID()+" error: "+e.getMessage());
								}
							} else {
								logger.warn(logmsg+"No se encontraron parametros en Metadata para el proceso: "+entry.getKey());
								logger.info(logmsg+"Actualizando Proceso local con estado error...");
								fc.updateMapProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
								
								logger.info(logmsg+"Actualizando Metadata con estado error...");
								dc.updateProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
							}
							
						} catch (Exception e) {
							logger.error(logmsg+"Error buscando parametros en MD proceso: "+entry.getValue().getProcID()+" - "+e.getMessage());
						}
						
					} else {
						logger.info(logmsg+"Parametros Proceso: "+entry.getKey()+" han sido actualizados");
					}
				}
			} else {
				logger.info(logmsg+"No se encontraron procesos en estado PENDING");
			}
			
		} catch (Exception e) {
			logger.error(logmsg+"Erro general: "+e.getMessage());
		}
	}
	
	public void updateTaskFinished() throws Exception {
		final String module = "updateTaskFinished()";
		final String logmsg = module+" - ";

		try {
			logger.info(logmsg+"Recuperando todos los procesos actuales...");
			Map<String, ProcControl> mappc = gParams.getMapProcControl()
													.entrySet()
													.stream()
													.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			logger.info(logmsg+"Recuperando los Keys del mapProcControl actual...");
			Map<String, String> mapKeys = new TreeMap<>();
			for (Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
				String subKey = entry.getValue().getGrpID()+":"+entry.getValue().getNumSecExec();
				mapKeys.put(subKey, entry.getKey());
				logger.debug(logmsg+"subKey encontrado: "+subKey);
			}
			

			logger.info(logmsg+"Buscando por cada subKey si todos los procesos inscritos finalizaron...");
			for (Map.Entry<String, String> entry : mapKeys.entrySet()) {
				String subKey = entry.getKey();   //Solo trae el grpID:numSecExec
				
				boolean allFinished = true;
				boolean endError = false;
				boolean endWarn = false;
				String lastErrMesg = "";
				int lastErrCode = 0;
				
				for (Map.Entry<String, ProcControl> subEntry : mappc.entrySet()) {
					String tmpKey = subEntry.getValue().getGrpID()+":"+subEntry.getValue().getNumSecExec();
					if (subKey.equals(tmpKey)) {
						if (!subEntry.getValue().getStatus().equals("FINISHED")) {
							allFinished = false;
							break;
						} else {
							if (subEntry.getValue().getuStatus().equals("ERROR")) {
								lastErrMesg = subEntry.getValue().getErrMesg();
								lastErrCode = subEntry.getValue().getErrCode();
								endError = true;
							} else if (subEntry.getValue().getuStatus().equals("WARNING")) {
								lastErrMesg = subEntry.getValue().getErrMesg();
								lastErrCode = subEntry.getValue().getErrCode();
								endWarn = true;
							}
						}
					}
				}
				
				if (allFinished) {
					//Todos los procesos del Grupo terminaron
					logger.info(logmsg+"Todos los procesos del Key: "+subKey+" han finalizado");
					logger.info(logmsg+"Actualizando procControl en Metadata...");
					
					for (Map.Entry<String, ProcControl> subEntry : mappc.entrySet()) {
						String tmpKey = subEntry.getValue().getGrpID()+":"+subEntry.getValue().getNumSecExec();
						if (subKey.equals(tmpKey)) {
							logger.info(logmsg+"Actualizando en MD proceso: "+tmpKey);
							fc.updateMDProcControl(subEntry.getValue());
							logger.info(logmsg+"Proceso :"+subEntry.getKey()+" actualizado!");
						}
					}
					
					logger.info(logmsg+"Actualizando groupControl en Metadata...");
					
					if (endError) {
						fc.updateMDGroupControl(subKey, "FINISHED", "ERROR", lastErrCode, lastErrMesg);
					} else if (endWarn) {
						fc.updateMDGroupControl(subKey, "FINISHED", "WARNING", lastErrCode, lastErrMesg);
					} else {
						//SUCCESS
						fc.updateMDGroupControl(subKey, "FINISHED", "SUCESS", lastErrCode, lastErrMesg);
					}
					
					logger.info(logmsg+"Eliminando Procesos Finalizados...");
					for (Map.Entry<String, ProcControl> subEntry : mappc.entrySet()) {
						String tmpKey = subEntry.getValue().getGrpID()+":"+subEntry.getValue().getNumSecExec();
						if (subKey.equals(tmpKey)) {
							logger.info(logmsg+"Eliminando proceso finalizado: "+subEntry.getKey());
							fc.deleteProcControl(subEntry.getKey());
							fc.deleteTask(subEntry.getKey());
							logger.info(logmsg+"Proceso :"+subEntry.getKey()+" Eliminado!");
						}
					}
					
					
					
				} else {
					logger.info(logmsg+"SubKey :"+subKey+" no ha finalizado!");
				}
				
			}
			
		} catch (Exception e) {
			throw new Exception("updateTaskFinished(): "+e.getMessage());
		}
	}
	
	public void updateTask() {
		final String module = "updateTask()";
		final String logmsg = module+" - ";
		
		try {
			/**
			 * Crea las Task correspondientes
			 * Recupera todos los procesos en estado PENDING para asignar Task
			 */
			logger.info(logmsg+"Buscando procesos PENDING para asignar TASK...");
			Map<String, ProcControl> mappc = gParams.getMapProcControl().entrySet().stream().filter(p -> p.getValue().getStatus().equals("PENDING")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			logger.info(logmsg+"Total procesos PENDING: "+mappc.size());
			
			//Busca para cada Proceso encontrado una lista de servicios disponibles para ejecutar el proceso
			if (mappc.size()>0) {
				
				logger.info(logmsg+"Validando para cada proceso PENDING si existe servicio inscrito para Asignar TASK...");
				for(Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
					
					//Valida si dependencias del proceso ya terminaron
					logger.info(logmsg+"--> Validando Información para Key: "+entry.getKey());
					logger.info(logmsg+"--> Valida si dependencias del proceso han finalizado...");
					if (fc.isProcDependFinished(entry.getValue())) {
						
						logger.info(logmsg+"--> Dependencias del proceso han finalizado!");
						
						//Valida si hay servicios disponibles para ejecutar procesos del cliente y del TypeProc
						logger.info(logmsg+"--> Buscando Servicios disponibles...");
						
						List<String> lstServices = new ArrayList<>();
						lstServices = fc.getServiceAvailable(entry.getValue().getCliID(), entry.getValue().getTypeProc());
						
						//logger.info(logmsg+"--> Servicios Validos para ejecutar proceso "+entry.getKey()+" : "+lstServices.size());
							
						if (lstServices.size()>0) {
							
							logger.info(logmsg+"--> Existen "+lstServices.size()+" servicios para ejecutar proceso!");
							/**
							 * Debe seleccionar el servicio disponible de la lista encontrada si es que hay mas de uno
							 */
							logger.info(logmsg+"--> Asignando un Servicio al proceso...");
							
							String srvID;
							if (lstServices.size()==0) {
								srvID = lstServices.get(0);
							} else {
								//Hay mas servicios disponibles
								//Seleccionar uno en base a algun criterio
								srvID = lstServices.get(0);
							}
							
							logger.info(logmsg+"--> Se ha asignado el servicio: "+srvID+ " al proceso: "+entry.getKey());
							
							//Se crea la Task asignandole un srvID y se cambia el status a READY
	
							logger.info(logmsg+"--> Actualizando el MapTask Global...");
							
							//Todos los procesos en estado PENDING que se encuentran en la mapProcControl
							//es porque no se les ha asiganado un Task de Ejecución.
							
							fc.addNewTask(entry.getValue(),srvID);
							
						} else {
							logger.info("--> No hay servcios disponibles para ejecutar proceso: "+entry.getKey());
						}
					} else {
						logger.info(logmsg+"--> Dependencias del proceso "+entry.getValue().getProcID()+" no han finalizado!");
					}
				}
			} else {
				logger.warn("No hay procesos en estado PENDING para asignar TASK");
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
	
    public void getMonParams() throws Exception {
    	DataAccess da = new DataAccess(gParams);
    	
    	try {
    		String resp = da.getDBParams();
    		JSONArray ja = new JSONArray(resp);
    		
    		for(int i=0; i<ja.length(); i++) {
    			MonParams mp = (MonParams) mylib.serializeJSonStringToObject(ja.get(i).toString(), MonParams.class);
    			gParams.getMapMonParams().put(mp.getMonID(), mp);
    			logger.info("monID: "+mp.getMonID()+" "+mylib.serializeObjectToJSon(mp, false));
    		}
    		
    	} catch (Exception e) {
    		throw new Exception("No es posible leer parametros de inicio desde Metadata: "+e.getMessage());
    	} finally {
			if (da.isConnected()) {
				da.close(); 
			}

    	}
    }

	
}
