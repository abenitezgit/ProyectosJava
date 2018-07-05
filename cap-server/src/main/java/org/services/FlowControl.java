package org.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.Dependence;
import org.model.PGPending;
import org.model.ProcControl;
import org.model.Service;
import org.model.Task;
import org.model.TypeProc;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class FlowControl {
	Logger logger = Logger.getLogger("FlowControl");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	DataAccess da;
	
	
	public FlowControl(GlobalParams m) {
		gParams = m;
		da = new DataAccess(gParams);
		mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
	}

	public void deleteTask(String key) throws Exception {
		try {
			gParams.getMapTask().remove(key);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deleteProcControl(String key) throws Exception {
		try {
			gParams.getMapProcControl().remove(key);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void updateTaskProcess(String strMapTask) throws Exception {
		try {
			if (!strMapTask.equals("{}")) {
				JSONObject jo = new JSONObject(strMapTask);
				String[] names = JSONObject.getNames(jo);
				
				if (names.length>0) {
					for (String key : names) {
						Task taskNew = (Task) mylib.serializeJSonStringToObject(jo.get(key).toString(), Task.class);
						
						if (gParams.getMapTask().containsKey(key)) {
							gParams.getMapTask().get(key).setStatus(taskNew.getStatus());
							gParams.getMapTask().get(key).setuStatus(taskNew.getuStatus());
							gParams.getMapTask().get(key).setErrCode(taskNew.getErrCode());
							gParams.getMapTask().get(key).setErrMesg(taskNew.getErrMesg());
							gParams.getMapTask().get(key).setFecFinished(taskNew.getFecFinished());
							gParams.getMapTask().get(key).setTxResult(taskNew.getTxResult());
							gParams.getMapTask().get(key).setTxSubTask(taskNew.getTxSubTask());
							
							updateMapProcControl(key, taskNew.getuStatus(), taskNew.getErrCode(), taskNew.getErrMesg());
						}
					}
				}
			} else {
				logger.info("MapTask recibido es nulo!");
			}
		} catch (Exception e) {
			throw new Exception("updateTaskProcess(): "+e.getMessage()+ " "+e.getClass());
		}
	}

	
	private synchronized void updateStatusMapTask(String key, String status) throws Exception {
		try {
			if (gParams.getMapTask().containsKey(key)) {
				gParams.getMapTask().get(key).setStatus(status);
				gParams.getMapTask().get(key).setFecUpdate(mylib.getDate());
				
				//updateMapProcControl(key, status, 0, "");
				logger.info("Task Update: "+key+" Status: "+gParams.getMapTask().get(key).getStatus());
			}
			
		} catch (Exception e) {
			throw new Exception("changeStatusMapTask(): "+e.getMessage());
		}
	}
	
	public Map<String, Task> getServiceMapTask(String srvID) throws Exception {
		try {
			Map<String, Task> mapTask = new HashMap<>();
			
			Map<String, Task> mapTaskTmp = gParams.getMapTask().entrySet().stream().filter(p -> p.getValue().getStatus().equals("ASSIGNED")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			for (Map.Entry<String, Task> entry : mapTaskTmp.entrySet()) {
				if (entry.getValue().getSrvID().equals(srvID)) {
					String key = entry.getValue().getGrpKey();
					mapTask.put(key, entry.getValue());
					updateStatusMapTask(key, "READY");
					updateMapProcControl(key, "READY", 0, "");
				}
			}
			logger.info("Total de Task por ser enviados: "+mapTask.size());
			return mapTask;
		} catch (Exception e) {
			throw new Exception(""+e.getMessage());
		}
	}

	private boolean isMapTaskContainProcID(String procID) throws Exception {
		try {
			boolean isExist = false;
			
			for (Map.Entry<String, Task> entry : gParams.getMapTask().entrySet()) {
				String[] keys = entry.getKey().split(":");
				if (keys[2].equals(procID) && !entry.getValue().getStatus().equals("FINISHED")) {
					isExist = true;
				}
			}
			return isExist;
		} catch (Exception e) {
			throw new Exception("isMapTaskContainProcID(): "+e.getMessage());
		}
	}
	
	public void updateMDGroupControl(String key, String status, String uStatus, int errCode, String errMesg) throws Exception {
		try {
			if (!da.updateGroupControl(key,status,uStatus,errCode,errMesg)) {
				logger.warn("El Grupo : "+key+" no ha podido ser actualizado en MD!");
				throw new Exception("El Grupo : "+key+" no ha podido ser actualizado en MD!");
			}
			
		} catch (Exception e) {
			throw new Exception("updateMDGroupControl(): "+e.getMessage());
		}
	}
	
	public void updateMDProcControl(ProcControl pc) throws Exception {
		try {
			if (!da.updateProcControl(pc)) {
				String key = pc.getGrpID()+":"+pc.getNumSecExec()+":"+pc.getProcID();
				logger.warn("El key : "+key+" no ha podido ser actualizado en MD!");
				throw new Exception("El key : "+key+" no ha podido ser actualizado en MD!");
			}
			
		} catch (Exception e) {
			throw new Exception("updateMDProcControl(): "+e.getMessage());
		}
	}
	
	public void addNewTask(ProcControl pc, String srvID) throws Exception {
		try {
			
			if (!isMapTaskContainProcID(pc.getProcID())) {
				String key = pc.getGrpID()+":"+pc.getNumSecExec()+":"+pc.getProcID();
				Task task = new Task();
				task.setFecIns(mylib.getDate());
				task.setFecUpdate(mylib.getDate());
				task.setGrpKey(key);
				task.setNumSecExec(pc.getNumSecExec());
				task.setParam(pc.getParam());
				task.setProcID(pc.getProcID());
				task.setSrvID(srvID);
				task.setStatus("ASSIGNED");
				task.setTaskkey(key);
				task.setTypeProc(pc.getTypeProc());
				
				gParams.getMapTask().put(key, task);
				
				updateMapProcControl(key, "ASSIGNED", 0, "");
			} else {
				logger.warn("El proceso "+pc.getProcID()+" ya se encuentra en el MapTask para ser ejecutado");
			}
		} catch (Exception e) {
			throw new Exception("updateMapTaskAssignedService"+e.getMessage());
		}
	}

	public boolean isProcDependFinished(ProcControl pc) throws Exception {
		final String logmsg = "isProcDependFinished() - ";
		try {
			boolean response = true;

			String grpID = pc.getGrpID();
			String numSecExec = pc.getNumSecExec();
			
			if (pc.getLstDependences().size() > 0) {
				for (Dependence entry : pc.getLstDependences()) {
					String procID = entry.getProcPadre();
					int critical = entry.getCritical();
					
					String key = grpID+":"+numSecExec+":"+procID;
					
					logger.info(logmsg+"-> Valida Status ProcID: "+key);
					
					if (gParams.getMapProcControl().containsKey(key)) {
						ProcControl pcS = gParams.getMapProcControl().get(key);
						
						logger.info(logmsg+"----> Status ProcID: "+key+" "+pcS.getStatus()+" "+pcS.getuStatus()+" c:"+critical );

						if (pcS.getStatus().equals("FINISHED")) {
							if (pcS.getuStatus().equals("SUCCESS")) {
								response = response && true;
							} else {
								if (critical==0) {
									response = response && true;
								} else {
									response = response && false;
								}
							}
						} else {
							response = response && false;
						}
						
					} else {
						//Ha ocurrido un problema ya que siempre debería encontrar un proceso
						response = response && false;
					}
				} // end for
			} else {
				return true;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception ("isProcDependFinished: "+e.getMessage());
		}
	}
	
	public void updateProcDependence(String key, List<Dependence> lstDep) throws Exception {
		try {
			if (gParams.getMapProcControl().containsKey(key)) {
				ProcControl pc = gParams.getMapProcControl().get(key);
				
				pc.setLstDependences(lstDep);
				
				gParams.getMapProcControl().put(key, pc);
			}
			
		} catch (Exception e) {
			throw new Exception ("updateProcDependence: "+e.getMessage());
		}
	}
	
	public void updateMapServiceFromMD(String response) throws Exception {
		final String logmsg = "updateMapServiceFromMD() - ";
		
		try {
			//Actualiza solo los siguientes valores
			//	- enable
			//	- mapTypeProc
			//	- mapCli
			//	- pctBalance
			//	- orderBalance
			
			Service serviceNew = (Service) mylib.serializeJSonStringToObject(response, Service.class);
			
			String srvID = serviceNew.getSrvID();
			
			String srvTypeProc = (String) serviceNew.getSrvTypeProc();
			JSONObject jo = new JSONObject(srvTypeProc);
			JSONArray jaProc = jo.getJSONArray("lstProc");
			JSONArray jaCli = jo.getJSONArray("lstCli");
			
			Map<String, TypeProc> mapTypeProcNew = new HashMap<>();
			Map<String, String> mapCliNew = new HashMap<>();
			
			logger.info(logmsg+"Generando mapProcNew desde datos recibidos...");
			for (int i=0; i<jaProc.length(); i++) {
				JSONObject joo = jaProc.getJSONObject(i);
				TypeProc tp = new TypeProc();
				tp.setMaxThread(joo.getInt("maxThread"));
				tp.setPriority(joo.getInt("priority"));
				tp.setTypeProc(joo.getString("typeProc"));
				tp.setUsedThread(0);
				mapTypeProcNew.put(tp.getTypeProc(), tp);
			}

			logger.info(logmsg+"Generando mapCliNew desde datos recibidos...");
			for (int i=0; i<jaCli.length(); i++) {
				String cli = jaCli.getString(i);
				mapCliNew.put(cli, cli);
			}
			
			Service service = new Service();
			
			logger.info(logmsg+"Validando si srvID recibido se encuentra en mapService local...");
			if (gParams.getMapService().containsKey(srvID)) {
				logger.info(logmsg+"srvID encontrado en mapService local, actualizando...");
				service = gParams.getMapService().get(srvID);
				
				service.setEnable(serviceNew.getEnable());
				service.setPctBalance(serviceNew.getPctBalance());
				service.setOrderBalance(serviceNew.getOrderBalance());
				logger.info(logmsg+"Se actualizaron los datos bases del servicio");
				
				//Actualizacion de MapTypeProc
				//Resguarda los usedTread actuales
				logger.info(logmsg+"Actualizando los TypeProc recibidos...");
				logger.info(logmsg+"Validando actualización de parametros actuales y nuevos");
				Map<String, TypeProc> mapTypeProc = gParams.getMapService().get(srvID).getMapTypeProc();
				
				for (Map.Entry<String, TypeProc> entry : mapTypeProcNew.entrySet()) {
					if (mapTypeProc.containsKey(entry.getKey())) {
						TypeProc tp = mapTypeProc.get(entry.getKey());
						tp.setMaxThread(entry.getValue().getMaxThread());
						tp.setPriority(entry.getValue().getPriority());
						mapTypeProc.put(entry.getKey(), tp);
					} else {
						TypeProc tp = entry.getValue();
						mapTypeProc.put(entry.getKey(), tp);
					}
				}
				
				logger.info(logmsg+"Eliminando typeProc que no fueron informados...");
				for (Map.Entry<String, TypeProc> entry : mapTypeProc.entrySet()) {
					if (!mapTypeProcNew.containsKey(entry.getKey())) {
						mapTypeProc.remove(entry.getKey());
					}
				}
				
				service.setMapTypeProc(mapTypeProc);
				logger.info(logmsg+"Tipos de proceso recibidos actualizados");
				
				
				//Actualizacion de MapCli
				logger.info(logmsg+"Actualizando clientes habilitados");
				service.setMapCli(mapCliNew);
				
				gParams.getMapService().put(srvID, service);
				logger.info(logmsg+"Servicio : "+srvID+" ha sido actualizado");
				
			} else {
				//Aun no existe mapService para este servicio y se creara
				logger.info(logmsg+"srvID recibido no se encuentra en mapService local, se agregara...");
				service.setEnable(serviceNew.getEnable());
				service.setFecStatus(mylib.getDateNow());
				service.setOrderBalance(serviceNew.getOrderBalance());
				service.setPctBalance(serviceNew.getPctBalance());
				service.setSrvDesc(serviceNew.getSrvDesc());
				service.setSrvID(srvID);
				service.setSrvTypeProc(serviceNew.getSrvTypeProc());
				service.setMapCli(mapCliNew);
				service.setMapTypeProc(mapTypeProcNew);
			
				gParams.getMapService().put(srvID, service);
				logger.info(logmsg+"Nuevo servicio : "+srvID+" se ha incorporado al mapService");
			}
			
		} catch (Exception e) {
			throw new Exception("updateMapServiceFromMD(): "+e.getMessage());
		}
	}
	
	public void updateMapServiceFromCC(Service serviceNew) throws Exception {
		//Actualiza solo los siguientes valores
		/*
		 * fecStatus
		 * typeProc:usedThread
		 */

		String logmsg = "updateMapServiceFromCC() - ";
		
		try {
			if (serviceNew.getMapTypeProc()!=null) {
				Map<String, TypeProc> mapTypeProcNew = serviceNew.getMapTypeProc();
				
				if (mapTypeProcNew.size()>0) {
					Service service = gParams.getMapService().get(serviceNew.getSrvID());
					
					//Actualiza datos del Servicio
					logger.info(logmsg+"Modifica fecha actualizacion del servicio...");
					if (!mylib.isNullOrEmpty(service.getFecStatus())) service.setFecStatus(serviceNew.getFecStatus());
								
					//Update mapTypeProc
					logger.info(logmsg+"Actualiza usedTread informados...");
					for (Map.Entry<String, TypeProc> entry : mapTypeProcNew.entrySet()) {
						TypeProc tpNew = entry.getValue();
						
						if (gParams.getMapService().get(serviceNew.getSrvID()).getMapTypeProc().containsKey(entry.getKey())) {
							TypeProc tp = gParams.getMapService().get(serviceNew.getSrvID()).getMapTypeProc().get(tpNew.getTypeProc());
							tp.setUsedThread(tpNew.getUsedThread());
							
							gParams.getMapService().get(serviceNew.getSrvID()).getMapTypeProc().put(entry.getKey(), tp);
						} else {
							gParams.getMapService().get(serviceNew.getSrvID()).getMapTypeProc().put(entry.getKey(), tpNew);
						}
					}
					logger.info(logmsg+"Parametros del Servicio actualizados!");
				} else {
					logger.warn(logmsg+"No hay tipos de procesos recibidos desde cap-client");
				}
			} else {
				logger.warn(logmsg+"No hay tipos de procesos recibidos desde cap-client");
			}
			
		} catch (Exception e) {
			throw new Exception("updateMapServiceFromCC(): "+e.getMessage());
		}
	}

	public String getStrProcControl() throws Exception {
		try {
			Map<String, ProcControl> mpc = new TreeMap<>();
			for (Map.Entry<String, ProcControl> entry : gParams.getMapProcControl().entrySet()) {
				ProcControl pc = new ProcControl();
				pc = entry.getValue();
				pc.setParam(null);
				mpc.put(entry.getKey(), pc);
			}
			String strResponse = null;
			strResponse = mylib.serializeObjectToJSon(mpc, false);
			return strResponse;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isContainValue(JSONArray lst, String variable, String value) {
		try {
			for (int i=0; i<lst.length(); i++) {
				JSONObject jo = lst.getJSONObject(i);
				if (jo.getString(variable).equals(value)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("isContainValue: "+e.getMessage());
			return false;
		}
	}
	
	public boolean isContainValue(JSONArray lst, String value) {
		try {
			for (int i=0; i<lst.length(); i++) {
				String cliID = (String) lst.get(i);
				if (cliID.equals(value)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("isContainValue: "+e.getMessage());
			return false;
		}
	}
	
	public List<String> getServiceAvailable(String cliID, String typeProc) {
		final String module = "getServiceAvailable()";
		final String logmsg = module+" - ";
		List<String> lstServices = new ArrayList<>();
		
		try {
			
			if (gParams.getMapService().size()>0) {
				
				for(Map.Entry<String, Service> entry : gParams.getMapService().entrySet()) {
					
					Calendar cal = Calendar.getInstance();
					Date fecNow = cal.getTime();
					Date fecUpdate = mylib.getDate(entry.getValue().getFecStatus(),"yyyy-MM-dd hh:mm:ss");
					int txpMain = entry.getValue().getTxpMain();
					
					//Valida si el servicio se ha respotado en txpMain
					if (mylib.getMinuteDiff(fecNow, fecUpdate)<=txpMain) {
						
						//Obtiene los clientes y tipos de procesos que puede ejecutar
						if (entry.getValue().getMapCli().containsKey(cliID)) {
							if (entry.getValue().getMapTypeProc().containsKey(typeProc)) {
								int maxThread = entry.getValue().getMapTypeProc().get(typeProc).getMaxThread();
								int usedThread = entry.getValue().getMapTypeProc().get(typeProc).getUsedThread();
								
								if (usedThread<maxThread) {
									if (getNumTaskByService(entry.getKey())<maxThread) {
										lstServices.add(entry.getKey());
									} else {
										logger.warn(logmsg+"El servicio "+entry.getKey()+" tiene todos sus thread max utilizados");
									}
								} else {
									logger.warn(logmsg+"El servicio "+entry.getKey()+" tiene todos sus thread used utilizados");
								}
							} else {
								logger.warn(logmsg+"El servicio "+entry.getKey()+" no tiene autorizacion para ejecutar tipos de proceso: "+typeProc);
							}
						} else {
							logger.warn(logmsg+"El servicio "+entry.getKey()+" no tiene autorizacion para ejecutar procesos del cliente: "+cliID);
						}
					} else {
						logger.warn(logmsg+"El servicio "+entry.getKey()+" no se ha registrado en los últimos "+entry.getValue().getTxpMain()+" minutos");
					}
				}
			} 
			
			return lstServices;
		} catch (Exception e) {
			logger.error(logmsg+"Exception error: "+e.getMessage());
			return lstServices;
		}
	}
	
	private int getNumTaskByService(String srvID) throws Exception {
		try {
			
			long numTask = gParams.getMapTask().entrySet()
											  .stream()
											  .filter(p -> (p.getValue().getSrvID().equals(srvID) && !p.getValue().getStatus().equals("FINISHED")))
											  .count();
								
			return (int) numTask;
		} catch (Exception e) {
			throw new Exception("getNumTaskByService(): "+e.getMessage());
		}
	}
	
	public void updateMapProcControl(String key, String uStatus, int errCode, String errMesg) {
		try {
			//Actualiza status MapProcControl Global
			if (gParams.getMapProcControl().containsKey(key)) {
				switch (uStatus) {
				case "SUCCESS":
					gParams.getMapProcControl().get(key).setStatus("FINISHED");
					gParams.getMapProcControl().get(key).setuStatus(uStatus);
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
					gParams.getMapProcControl().get(key).setErrCode(errCode);
					break;
				case "ERROR":
					gParams.getMapProcControl().get(key).setStatus("FINISHED");
					gParams.getMapProcControl().get(key).setuStatus(uStatus);
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
					gParams.getMapProcControl().get(key).setErrCode(errCode);
					gParams.getMapProcControl().get(key).setErrMesg(errMesg);
					break;
				case "READY":
					gParams.getMapProcControl().get(key).setStatus("READY");
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
				case "ASSIGNED":
					gParams.getMapProcControl().get(key).setStatus("ASSIGNED");
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
				default:
					gParams.getMapProcControl().get(key).setStatus(uStatus);
					gParams.getMapProcControl().get(key).setuStatus(uStatus);
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
					break;
				}
				
			}
			
		} catch (Exception e) {
			logger.error("updateMapProcControl: "+e.getMessage());
		}
	}
	
	public void updateMapProcControl(String key, Object params) {
		try {
			if (gParams.getMapProcControl().containsKey(key)) {
				gParams.getMapProcControl().get(key).setParam(params);
			}
		} catch (Exception e) {
			logger.error("updateMapProcControl: "+e.getMessage());
		}
	}
	
	public void updateMapProcControl(Map<String, PGPending> mapPg) {
		try {
			/**
			 * key del mapPg grpID:numSecExec:procid
			 */
			for(Map.Entry<String, PGPending> entry : mapPg.entrySet()) {
				if (!gParams.getMapProcControl().containsKey(entry.getKey())) {
					ProcControl pc = new ProcControl();
					pc.setFecIns(entry.getValue().getFecIns());
					pc.setFecUpdate(mylib.getDate());
					pc.setGrpID(entry.getValue().getGrpID());
					pc.setnOrder(entry.getValue().getnOrder());
					pc.setNumSecExec(entry.getValue().getNumSecExec());
					pc.setProcID(entry.getValue().getProcID());
					pc.setStatus(entry.getValue().getStatus());
					pc.setTypeProc(entry.getValue().getTypeProc());
					pc.setuStatus("PENDING");
					pc.setCliID(entry.getValue().getCliID());
					pc.setCliDesc(entry.getValue().getCliDesc());
					pc.setFecUpdate(mylib.getDate());
					
					gParams.getMapProcControl().put(entry.getKey(), pc);
				}
			}
			
			
		} catch (Exception e) {
			logger.error("updateMapProcControl: "+e.getMessage());
		}
	}
	
	public void updateProcessConfig(Map<String, PGPending> mapPg) {
		try {
			/**
			 * key del mapPg grpID:numSecExec:procid
			 */
			for(Map.Entry<String, PGPending> entry : mapPg.entrySet()) {
				if (gParams.getMapProcControl().containsKey(entry.getKey())) {
					
				}
			}
			
		} catch (Exception e) {
			logger.error("updateProcessConfig: "+e.getMessage());
		}
		
	}
}
