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
import org.model.LogMessage;
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
	
	public Object getMonRequest(JSONObject data) throws Exception {
		try {
			String method = data.getString("method");
			JSONObject params;
			
			Object response = null;
			String param = "";
			String param1 = "";
			
			switch (method) {
				case "getProcControl":
					params = data.getJSONObject("params");
					param = params.getString("status");
					param1 = params.getString("uStatus");
					response = fc.getProcControl(param,param1);
					break;
				case "getLog":
					response = fc.getLog();
					break;
				case "getServices":
					response = fc.getServices();
					break;
				case "updFinishedProcess":
					params = data.getJSONObject("params");
					response = fc.updateForceFinishedProcess(params);
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	public Object getDBrequest(JSONObject data) throws Exception {
		try {
			String method = data.getString("method");
			JSONObject params = data.getJSONObject("params");
			
			Object response = null;
			
			switch (method) {
				case "getDBGroup":
					response = dc.getDBresources(method, params);
					break;
				case "getDBprocGroup":
					List<Map<String,Object>> lstResponse = new ArrayList<>();
					
					Object responseMyProcs = dc.getDBresources(method, params);
					
					@SuppressWarnings("unchecked") 
					List<Map<String,Object>> myProcs = (List<Map<String, Object>>) responseMyProcs;
					for (int i=0; i<myProcs.size(); i++) {
						Map<String,Object> myProc = myProcs.get(i);
						Object responseMyDepsProc = dc.getProcDependences((String) myProc.get("grpID"), (String) myProc.get("procID"));
						@SuppressWarnings("unchecked")
						List<Map<String,Object>> myDepsProc = (List<Map<String, Object>>) responseMyDepsProc;
						myProc.put("depCount", myDepsProc.size());
						myProc.put("lstDeps", responseMyDepsProc);
						lstResponse.add(myProc);
					}
					
					response = lstResponse;
					
					break;
				case "getDBschedule":
					response = dc.getDBresources(method, params);
					break;
				case "getDBschedDiary":
					response = dc.getDBresources(method, params);
					break;
				case "getDBdiary":
					response = dc.getDBresources(method, params);
					break;
				case "getDBbase":
					response = dc.getDBresources(method, params);
					break;
				case "getDBgroupControl":
					response = dc.getDBresources(method, params);
					break;
				case "getDBprocControl":
					List<Map<String,Object>> lstResponse2 = new ArrayList<>();
					
					Object responseMyProcs2 = dc.getDBresources(method, params);
					
					@SuppressWarnings("unchecked") 
					List<Map<String,Object>> myProcs2 = (List<Map<String, Object>>) responseMyProcs2;
					for (int i=0; i<myProcs2.size(); i++) {
						Map<String,Object> myProc = myProcs2.get(i);
						Object responseMyDepsProc = dc.getProcDependences((String) myProc.get("grpID"), (String) myProc.get("procID"));
						@SuppressWarnings("unchecked")
						List<Map<String,Object>> myDepsProc = (List<Map<String, Object>>) responseMyDepsProc;
						myProc.put("depCount", myDepsProc.size());
						myProc.put("lstDeps", responseMyDepsProc);
						lstResponse2.add(myProc);
					}
					
					response = lstResponse2;

					break;
				case "getDBclient":
					response = dc.getDBresources(method, params);
					break;
				case "getDBcategory":
					response = dc.getDBresources(method, params);
					break;
				case "getDBprocDep":
					response = dc.getDBresources(method, params);
					break;
				case "getAgeGroupStat":
					response = dc.getAgeGroupStat();
					break;
				case "getAgeGroupDayHourAlert":
					int vDay = params.getInt("day");
					int vHour = params.getInt("hour");
					response = dc.getAgeGroupDayHourAlert(vDay, vHour);
					break;
				case "addGroup":
					response = dc.addDBresources(method, params);
				case "addProcGroup":
					response = dc.addDBresources(method, params);
					break;
				case "addClient":
					response = dc.addDBresources(method, params);
					break;
				case "addCategory":
					response = dc.addDBresources(method, params);
					break;
				case "addDBase":
					response = dc.addDBresources(method, params);
					break;
				case "addDependence":
					response = dc.addDBresources(method, params);
					break;
				case "addGroupActiveManual":
					String param = params.getString("grpID");
					response = addGroupActiveManual(param);
					break;
				case "deleteGroupControl":
					JSONObject joGroup = (JSONObject) params;
					fc.deleteForceProcControl(joGroup.getString("grpID"), joGroup.getString("numSecExec"), joGroup.getString("procID"));
					response = dc.deleteDBresources(method, params);
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void showMapProcControl() {
		logger.debug("Detalle de Procesos");
		for (Map.Entry<String, ProcControl> entry : gParams.getMapProcControl().entrySet()) {
			logger.debug("--> "+entry.getKey()+" "+entry.getValue().getStatus()+" "+entry.getValue().getuStatus()+" "+entry.getValue().getErrCode()+" "+entry.getValue().getErrMesg());
		}
	}
	
	public void showMapTask() throws Exception {
		logger.debug("Detalle de Task: ");
		for (Map.Entry<String, Task> entry : gParams.getMapTask().entrySet()) {
			logger.debug("Task: "+ entry.getKey()  +" --->"+mylib.serializeObjectToJSon(entry.getValue(), false));
			//logger.info("--> "+entry.getKey()+" "+entry.getValue().getStatus()+" "+entry.getValue().getuStatus()+" "+ entry.getValue().getSrvID()+ " "+ entry.getValue().getErrCode()+" "+entry.getValue().getErrMesg());
		}
	}
	
	public String syncTaskProcess(JSONObject data) throws Exception {
		try {
			
			String srvID = data.getString("srvID");
			String strMapTask = data.getString("mapTask");
			String strSendMapTask = "";
			
			//Actualizando mapTask con datos desde cap-client
			logger.info("Actualizando Task con datos desde cap-client: "+srvID);
			fc.updateTaskProcess(strMapTask);
			
			//Recuperando datos actualizados para enviar al cap-client
			
			logger.info("Preparando Task para ser enviado el cap-client: "+srvID);
			Map<String, Task> mapTask = fc.getServiceMapTask(srvID);
			
			if (mapTask.size()>0) {
				strSendMapTask = mylib.serializeObjectToJSon(mapTask, false);
			}
			
			return strSendMapTask;
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
	
	public boolean addGroupActiveManual(String grpID)  {
		try {
			
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.addGroupActiveManual(grpID);
			
			logger.info("Total de Procesos encontrados para activar manualmnente: "+mapPg.size());
			
			if (mapPg.size()>0) {
				for(Map.Entry<String, PGPending> entry : mapPg.entrySet()) {
					logger.debug("Proceso A ejecutar manualmente leido desde Metadata: "+entry.getKey());
				}
			}
			
			//Recupera parametros de los grupos encontrados
			fc.updateMapGroup(mapPg);
			
			//Actualiza la Tabla de status de proceso global
			logger.info("Actualizando Control de Procesos locales...");
			fc.updateMapProcControl(mapPg);

			return true;
		} catch (Exception e) {
			logger.error("addGroupActiveManual: "+e.getMessage());
			return false;
		}
	}
	
	public void findNewGroupProcess()  {
		try {
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.getActiveGroup();
			
			logger.info("Total de Procesos potenciales encontrados para activar: "+mapPg.size());
			
			if (mapPg.size()>0) {
				for(Map.Entry<String, PGPending> entry : mapPg.entrySet()) {
					logger.debug("Proceso leido desde Metadata: "+entry.getKey());
				}
			}
			
			//Recupera parametros de los grupos encontrados
			fc.updateMapGroup(mapPg);
			
			//Actualiza la Tabla de status de proceso global
			logger.info("Actualizando Control de Procesos locales...");
			fc.updateMapProcControl(mapPg);
			
		} catch (Exception e) {
			logger.error("findNewGroupProcess(): "+e.getMessage());
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
						logger.info(logmsg+"Proceso: "+entry.getKey()+" ya posee sus parametros actualizados");
					}
				}
			} else {
				logger.info(logmsg+"No se encontraron procesos en estado PENDING");
			}
			
		} catch (Exception e) {
			logger.error(logmsg+"updateProcessParams(): "+e.getMessage());
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
							} else {
								lastErrMesg = subEntry.getValue().getErrMesg();
								lastErrCode = subEntry.getValue().getErrCode();								
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
					
					logger.info(logmsg+"Eliminando asigación de servicio al grupo");
					if (gParams.getMapAssignedService().containsKey(subKey.split(":")[0])) {
						gParams.getMapAssignedService().remove(subKey.split(":")[0]);
					}
					
				} else {
					logger.info(logmsg+"SubKey :"+subKey+" no ha finalizado!");
				}
				
			}
			
		} catch (Exception e) {
			throw new Exception("updateTaskFinished(): "+e.getMessage());
		}
	}
	
	public void assignNewTask() {
		final String module = "assignNewTask()";
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
					
					try {
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
								
								String srvID = fc.assignService(lstServices, entry.getValue().getGrpID(), entry.getValue().getTypeProc());
								
	//							if (lstServices.size()==0) {
	//								srvID = lstServices.get(0);
	//							} else {
	//								//Hay mas servicios disponibles
	//								//Seleccionar uno en base a algun criterio
	//								srvID = lstServices.get(0);
	//							}
								
								logger.info(logmsg+"--> Se ha asignado el servicio: "+srvID+ " al proceso: "+entry.getKey());
								
								//Se crea la Task asignandole un srvID y se cambia el status a READY
		
								logger.info(logmsg+"--> Actualizando el MapTask Global...");
								
								//Todos los procesos en estado PENDING que se encuentran en la mapProcControl
								//es porque no se les ha asiganado un Task de Ejecución.
								try {
									fc.addNewTask(entry.getValue(),srvID);
								} catch (Exception e) {
									logger.error("No es posible crear Task para el prcoceso: "+entry.getValue().getProcID());
								}
								
							} else {
								logger.info("--> No hay servcios disponibles para ejecutar proceso: "+entry.getKey());
							}
						} else {
							logger.info(logmsg+"--> Dependencias del proceso "+entry.getValue().getProcID()+" no han finalizado!");
						}
					} catch (Exception e) {
						logger.error("No es posible determinar dependencias del proceso: "+entry.getValue().getProcID());
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
    
    public List<LogMessage> getLogMessage() throws Exception {
    	try {
    		List<LogMessage> lstLog = new ArrayList<>();
    		int numLogs = gParams.getLinkedLog().size();
    		
    		if (numLogs>1000) {
    			numLogs = 1000;
    		}
    		
    		for (int i=0; i<numLogs; i++) {
    			LogMessage lgm = gParams.getLinkedLog().removeFirst();
    			lstLog.add(lgm);
    		}
    		
			return lstLog;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
    }

    public void addMyLog(List<LogMessage> lstLog) throws Exception {
    	try {
    		for (LogMessage lgm : lstLog) {
    			dc.addMyLog(lgm);
    		}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
    }
	
}
