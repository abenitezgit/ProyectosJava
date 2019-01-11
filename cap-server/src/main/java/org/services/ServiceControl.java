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
				case "forceCancelProcess":
					params = data.getJSONObject("params");
					response = addForceCancelProcess(params);
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean addForceCancelProcess(JSONObject params) {
		try {
			String grpID = params.getString("grpID");
			String numSecExec = params.getString("numSecExec");
			String procID = params.getString("procID");
			
			ProcControl pc = new ProcControl();
			pc.setGrpID(grpID);
			pc.setNumSecExec(numSecExec);
			pc.setProcID(procID);
			
			if (gParams.getLstForceCancelProcess().size()==0) {
				gParams.getLstForceCancelProcess().add(pc);
			} else {
				boolean isExist = false;
				for (int i=0; i<gParams.getLstForceCancelProcess().size(); i++) {
					if (gParams.getLstForceCancelProcess().get(i).getGrpID().equals(grpID) && 
							gParams.getLstForceCancelProcess().get(i).getNumSecExec().equals(numSecExec) &&
							gParams.getLstForceCancelProcess().get(i).getProcID().equals(procID)) {
						isExist = true;
					}
				}
				if (!isExist) {
					gParams.getLstForceCancelProcess().add(pc);
				}
			}
			
			return true;
		} catch (Exception e) {
			logger.error("addForceCancelProcess(): "+e.getMessage());
			return false;
		}
	}
	
	public List<Map<String,Object>> getDashboard() throws Exception {
		try {
			List<Map<String,Object>> lstRows = new ArrayList<>();
			List<Map<String,Object>> lstGroups = new ArrayList<>();
			List<Map<String,Object>> lstProcs = new ArrayList<>();
			Map<String,Object> mapTotales = dc.getCountGroups();
			
			int numRunningGroups = fc.getCountRunningGroups();
			int numRunningProcs = fc.getCountRunningProcs();
			
			JSONObject params = new JSONObject();
			params.put("grpID", "*");
			params.put("uStatus", "*");
			params.put("cliID", "*");
			lstGroups = dc.getDBresources("getDBgroupControl", params);
			
			params = new JSONObject();
			params.put("grpID", "GRP00572");
			params.put("numSecExec", "*");
			params.put("procID", "*");
			lstProcs = dc.getDBresources("getDBprocControl", params);
			
			mapTotales.put("numRunningGroups", numRunningGroups);
			mapTotales.put("numRunningProcs", numRunningProcs);
			mapTotales.put("lastGroups", lstGroups);
			mapTotales.put("lastProcs", lstProcs);
			
			lstRows.add(mapTotales);
			
			return lstRows;
		} catch (Exception e) {
			throw new Exception(""+e.getMessage());
		}
	}
	
	public Object getDBrequest(JSONObject data) throws Exception {
		try {
			String method = data.getString("method");
			JSONObject params = data.getJSONObject("params");
			
			Object response = null;
			
			switch (method) {
				case "getDashboard":
					response = getDashboard();
					break;
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
				case "getCmbClient":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbCategory":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbUser":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbDbase":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbServer":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbFtp":
					response = dc.getDBresources(method, params);
					break;
				case "getCmbMov":
					List<Map<String,Object>> myMovs = dc.getDBresources(method, params);
					List<Map<String,Object>> myMovsResp = new ArrayList<>();
					
					if (myMovs.size()==1) {
						Map<String,Object> myMov = myMovs.get(0);
						List<Map<String,Object>> movParams = dc.getDBresources("getCmbMovParams", params);
						myMov.put("movParams", movParams);
						myMovsResp.add(myMov);
						response = myMovsResp;
					} else {
						response = myMovs;
					}
					
					break;
				case "getCmbOsp":
					List<Map<String,Object>> myOsps = dc.getDBresources(method, params);
					List<Map<String,Object>> myOspsResp = new ArrayList<>();
					
					if (myOsps.size()==1) {
						Map<String,Object> myOsp = myOsps.get(0);
						List<Map<String,Object>> ospParams = dc.getDBresources("getCmbOspParams", params);
						myOsp.put("ospParams", ospParams);
						myOspsResp.add(myOsp);
						response = myOspsResp;
					} else {
						response = myOsps;
					}
					
					break;
				case "getCmbEtb":
					List<Map<String,Object>> myEtbs = dc.getDBresources(method, params);
					List<Map<String,Object>> myEtbsResp = new ArrayList<>();
					
					if (myEtbs.size()==1) {
						Map<String,Object> myEtb = myEtbs.get(0);
						List<Map<String,Object>> etbParams = dc.getDBresources("getCmbEtbParams", params);
						myEtb.put("etbParams", etbParams);
						myEtbsResp.add(myEtb);
						response = myEtbsResp;
					} else {
						response = myEtbs;
					}
					
					break;
				case "getCmbLtb":
					List<Map<String,Object>> myLtbs = dc.getDBresources(method, params);
					List<Map<String,Object>> myLtbsResp = new ArrayList<>();
					
					if (myLtbs.size()==1) {
						Map<String,Object> myLtb = myLtbs.get(0);
						List<Map<String,Object>> ltbParams = dc.getDBresources("getCmbLtbParams", params);
						myLtb.put("lbtParams", ltbParams);
						myLtbsResp.add(myLtb);
						response = myLtbsResp;
					} else {
						response = myLtbs;
					}
					
					break;
				case "getCmbBack":
					response = dc.getDBresources(method, params);
					break;
 				case "getCmbOspParams":
					response = dc.getDBresources(method, params);
					break;
 				case "getCmbLtbParams":
					response = dc.getDBresources(method, params);
					break;
 				case "getCmbEtbParams":
					response = dc.getDBresources(method, params);
					break;
 				case "getCmbMovParams":
					response = dc.getDBresources(method, params);
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
				case "getFtp":
					response = dc.getDBresources(method, params);
					break;
				case "getBack":
					response = dc.getDBresources(method, params);
					break;
				case "getOsp":
					response = dc.getDBresources(method, params);
					break;
				case "getMov":
					response = dc.getDBresources(method, params);
					break;
				case "getEtb":
					response = dc.getDBresources(method, params);
					break;
				case "getLtb":
					response = dc.getDBresources(method, params);
					break;
				case "getOspParam":
					response = dc.getDBresources(method, params);
					break;
				case "getMovParam":
					response = dc.getDBresources(method, params);
					break;
				case "getEtbParam":
					response = dc.getDBresources(method, params);
					break;
				case "getLtbParam":
					response = dc.getDBresources(method, params);
					break;
				case "getAdmGroup":
					response = dc.getDBresources(method, params);
					break;
				case "getAgeGroupStat":
					response = dc.getAgeGroupStat();
					break;
				case "getAgeGroupWeek":
					response = dc.getAgeGroupWeek();
					break;
				case "getAgeGroupDayHourAlert":
					int vDay = params.getInt("day");
					int vHour = params.getInt("hour");
					response = dc.getAgeGroupDayHourAlert2(vDay, vHour);
					break;
				case "getActualGroupDayHour":
					int vDay2 = params.getInt("day");
					int vHour2 = params.getInt("hour");
					response = dc.getActualGroupDayHour(vDay2, vHour2);
					break;
				case "getAgeStatDetails":
					response = dc.getAgeStatDetails(params);
					break;
				case "addGroup":
					response = dc.addDBresources(method, params);
					break;
				case "addDiary":
					response = dc.addDBresources(method, params);
					break;
				case "addSchedule":
					response = dc.addDBresources(method, params);
					break;
				case "addSchedDiary":
					response = dc.addDBresources(method, params);
					break;
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
				case "addUser":
					response = dc.addDBresources(method, params);
					break;
				case "addServer":
					response = dc.addDBresources(method, params);
					break;
				case "addMov":
					response = dc.addDBresources(method, params);
					break;
				case "addLtb":
					response = dc.addDBresources(method, params);
					break;
				case "addLtbParam":
					response = dc.addDBresources(method, params);
					break;
				case "addEtb":
					response = dc.addDBresources(method, params);
					break;
				case "addEtbParam":
					response = dc.addDBresources(method, params);
					break;
				case "addMovParam":
					response = dc.addDBresources(method, params);
					break;
				case "addFtp":
					response = dc.addDBresources(method, params);
					break;
				case "addOsp":
					response = dc.addDBresources(method, params);
					break;
				case "addOspParam":
					response = dc.addDBresources(method, params);
					break;
				case "addBack":
					response = dc.addDBresources(method, params);
					break;
				case "addGroupActiveManual":
					String param = params.getString("grpID");
					response = addGroupActiveManual(param);
					break;
				case "addGroupActiveManualActual":
					response = addGroupActiveManualActual(params);
					break;
				case "addProcActiveManualActual":
					response = addProcActiveManualActual(params);
					break;
				case "addProcActiveManual":
					response = addProcActiveManual(params);
					break;
				case "deleteOsp":
					response = dc.delDBresources(method, params);
					break;
				case "deleteOspParam":
					response = dc.delDBresources(method, params);
					break;
				case "deleteMov":
					response = dc.delDBresources(method, params);
					break;
				case "deleteMovParam":
					response = dc.delDBresources(method, params);
					break;
				case "deleteEtb":
					response = dc.delDBresources(method, params);
					break;
				case "deleteEtbParam":
					response = dc.delDBresources(method, params);
					break;
				case "deleteLtb":
					response = dc.delDBresources(method, params);
					break;
				case "deleteLtbParam":
					response = dc.delDBresources(method, params);
					break;
				case "deleteFtp":
					response = dc.delDBresources(method, params);
					break;
				case "deleteBack":
					response = dc.delDBresources(method, params);
					break;
				case "deleteGroup":
					response = dc.delDBresources(method, params);
					break;
				case "deleteProcGroup":
					response = dc.delDBresources(method, params);
					break;
				case "deleteDependence":
					response = dc.delDBresources(method, params);
					break;
				case "deleteGroupControl":
					JSONObject joGroup = (JSONObject) params;
					fc.deleteForceProcControl(joGroup.getString("grpID"), joGroup.getString("numSecExec"), joGroup.getString("procID"));
					response = dc.deleteDBresources(method, params);
					break;
				case "deleteClient":
					response = dc.delDBresources(method, params);
					break;
				case "deleteCategory":
					response = dc.delDBresources(method, params);
					break;
				case "deleteUser":
					response = dc.delDBresources(method, params);
					break;
				case "deleteServer":
					response = dc.delDBresources(method, params);
					break;
				case "deleteDbID":
					response = dc.delDBresources(method, params);
					break;
				case "updateBack":
					response = dc.updateDBresources(method, params);
					break;
				case "updateEtb":
					response = dc.updateDBresources(method, params);
					break;
				case "updateEtbParam":
					response = dc.updateDBresources(method, params);
					break;
				case "updateFtp":
					response = dc.updateDBresources(method, params);
					break;
				case "updateLtb":
					response = dc.updateDBresources(method, params);
					break;
				case "updateLtbParam":
					response = dc.updateDBresources(method, params);
					break;
				case "updateOsp":
					response = dc.updateDBresources(method, params);
					break;
				case "updateOspParam":
					response = dc.updateDBresources(method, params);
					break;
				case "updateMov":
					response = dc.updateDBresources(method, params);
					break;
				case "updateMovParam":
					response = dc.updateDBresources(method, params);
					break;
				case "updateProcGroup":
					response = dc.updateDBresources(method, params);
					break;
				case "updateDependence":
					response = dc.updateDBresources(method, params);
					break;
				default:
					logger.error("method: "+method+" no es reconocido");
					throw new Exception("getDBRequest(): method: "+method+" no es reconocido");
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("getDBRequest(): "+e.getMessage());
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

	public boolean addGroupActiveManualActual(JSONObject params)  {
		try {
			String grpID = params.getString("grpID");
			String numSecExec = params.getString("numSecExec");
			
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.addGroupActiveManualActual(grpID,numSecExec);
			
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
			logger.error("addGroupActiveManualActual: "+e.getMessage());
			return false;
		}
	}

	public boolean addProcActiveManualActual(JSONObject params)  {
		try {
			String grpID = params.getString("grpID");
			String numSecExec = params.getString("numSecExec");
			String procID = params.getString("procID");
			
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.addProcActiveManualActual(grpID,numSecExec,procID);
			
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
			logger.error("addProcActiveManualActual(): "+e.getMessage());
			return false;
		}
	}

	public boolean addProcActiveManual(JSONObject params)  {
		try {
			String grpID = params.getString("grpID");
			String procID = params.getString("procID");
			
			Map<String, PGPending> mapPg = new HashMap<>();
			//Extrea los Procesos de Grupos que fueron inscritos recientemente 
			//y que tienen status PENDIENTE
			mapPg = dc.addProcActiveManual(grpID,procID);
			
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
			logger.error("addProcActiveManual(): "+e.getMessage());
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
					
					//Busca params solo si aun lo lo ha hecho
					if (entry.getValue().getParam()==null) {
						
						String key = entry.getKey();
						Object params = new Object();
					
						logger.info(logmsg+"Actualizando parametros a proceso: "+entry.getKey());
						
						//Entra en un Try/Exception para que busque para cada proceso encontrado y reporte error individualmente
						try {
							params = dc.getProcessParam(entry.getValue().getProcID(), entry.getValue().getTypeProc());

							logger.info(logmsg+"Actualizando Clase local con parametros encontrados...");
							fc.updateMapProcControl(key, params);

							//Buscando Dependencias del Proceso
							logger.info(logmsg+"Buscando dependencia del proceso: "+entry.getValue().getProcID());
							try {
								List<Dependence> lstDep = new ArrayList<>();
								lstDep = dc.getProcDependences(entry.getValue().getGrpID(), entry.getValue().getProcID());
								
								if (lstDep.size()>0) {
									fc.updateProcDependence(key, lstDep);
								} else {
									logger.debug(logmsg+"El proceso "+entry.getValue().getProcID()+" no posee dependencias informadas");
								}
								
							} catch (Exception e) {
								logger.error(logmsg+"Error buscando dependencia del proceso: "+entry.getValue().getProcID()+" error: "+e.getMessage());
							}
							
						} catch (Exception e) {
							logger.warn(logmsg+"No se encontraron parametros en Metadata para el proceso: "+entry.getKey());
							logger.info(logmsg+"Actualizando Proceso local con estado error...");
							fc.updateMapProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
							
							logger.info(logmsg+"Actualizando Metadata con estado error...");
							dc.updateProcControl(key, "ERROR", 99, "Proceso: "+key+" no se encuentra en Metadata");
						}
						
					} else {
						//logger.info(logmsg+"Proceso: "+entry.getKey()+" ya posee sus parametros actualizados");
					}
				}
			} else {
				//logger.info(logmsg+"No se encontraron procesos en estado PENDING");
			}
			
		} catch (Exception e) {
			logger.error(logmsg+"updateProcessParams(): "+e.getMessage());
		}
	}
	
	public void forceCancelProcess() throws Exception {
		try {
			//Valida si Grupos o Procesos inscritos para ser cancelados
			if (gParams.getLstForceCancelProcess().size()>0) {
				while (!gParams.getLstForceCancelProcess().isEmpty()) {
				
					ProcControl pc = gParams.getLstForceCancelProcess().remove();
					
					//El grpID siempre debe venir pero el procID podría ser un * lo que significa
					//que se deben cancelar todos los procesos del grupo que estén pendientes para que no se asignen
					//nuevas Task
					if (pc.getProcID().equals("*")) {
						//Recupera todos los procesos del grupo que estén PENDING
						Map<String, ProcControl> mapProcPending = gParams.getMapProcControl()
																	.entrySet()
																	.stream()
																	.filter(p -> 
																			//p.getValue().getStatus().equals("PENDING") && 
																			p.getValue().getGrpID().equals(pc.getGrpID()) && 
																			p.getValue().getNumSecExec().equals(pc.getNumSecExec()))
																	.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
						if (mapProcPending.size()>0) {
							//Si el filtro retorna datos se procede a cancelar
							for (Map.Entry<String, ProcControl> entry : mapProcPending.entrySet()) {
								try {
									fc.updateForceCancelProcess(entry.getKey());
								} catch (Exception e) {
									logger.error("No es posible Cancelar proceso: "+entry.getKey()+" - "+e.getMessage());
								}
							}
						}

					} else {
						//Se cancelará solo un proceso del grupo
						String procKey = pc.getGrpID()+":"+pc.getNumSecExec()+":"+pc.getProcID();
						try {
							fc.updateForceCancelProcess(procKey);
						} catch (Exception e) {
							logger.error("No es posible Cancelar proceso: "+procKey+" - "+e.getMessage());
						}
					}
				}
			}
			
		} catch (Exception e) {
			throw new Exception("forceCancelProcess(): "+e.getMessage());
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
			}
			

			logger.info(logmsg+"Buscando por cada subKey si todos los procesos inscritos finalizaron...");
			for (Map.Entry<String, String> entry : mapKeys.entrySet()) {
				String subKey = entry.getKey();   //Solo trae el grpID:numSecExec
				
				boolean allFinished = true;
				boolean endError = false;
				boolean endWarn = false;
				boolean endCancel = false;
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
								if (fc.isProcCritical(subEntry.getValue())) {
									lastErrMesg = subEntry.getValue().getErrMesg();
									lastErrCode = subEntry.getValue().getErrCode();
									endError = true;
//									break;
								} else {
									endWarn = true;
								}
							} else if (subEntry.getValue().getuStatus().equals("WARNING")) {
								lastErrMesg = subEntry.getValue().getErrMesg();
								lastErrCode = subEntry.getValue().getErrCode();
								endWarn = true;
//								break;
							} else if (subEntry.getValue().getuStatus().equals("CANCEL")) {
								lastErrMesg = subEntry.getValue().getErrMesg();
								lastErrCode = subEntry.getValue().getErrCode();
								endCancel = true;
//								break;
							}  
//							else {
//								lastErrMesg = subEntry.getValue().getErrMesg();
//								lastErrCode = subEntry.getValue().getErrCode();								
//							}
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
					} else if (endCancel) {
						fc.updateMDGroupControl(subKey, "FINISHED", "CANCEL", lastErrCode, lastErrMesg);
					} else {
						//SUCCESS
						fc.updateMDGroupControl(subKey, "FINISHED", "SUCCESS", lastErrCode, lastErrMesg);
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
			
			logger.info(logmsg+"Total procesos PENDING para asignar Task: "+mappc.size());
			
			//Busca para cada Proceso encontrado una lista de servicios disponibles para ejecutar el proceso
			if (mappc.size()>0) {
				
				logger.info(logmsg+"Validando para cada proceso PENDING si existe servicio inscrito para Asignar TASK...");
				for(Map.Entry<String, ProcControl> entry : mappc.entrySet()) {
					
					try {
						//Valida si dependencias del proceso ya terminaron
						logger.info(logmsg+"--> Validando Dependencias del Proceso: "+entry.getKey());
						if (fc.isProcDependFinished(entry.getValue())) {
							
							logger.info(logmsg+"--> Dependencias del proceso "+entry.getKey()+"han finalizado!");
							
							//Valida si hay servicios disponibles para ejecutar procesos del cliente y del TypeProc
							logger.info(logmsg+"--> Buscando Servicios disponibles...");
							
							List<String> lstServices = new ArrayList<>();
							lstServices = fc.getServiceAvailable(entry.getValue().getCliID(), entry.getValue().getTypeProc());
							
							//logger.info(logmsg+"--> Servicios Validos para ejecutar proceso "+entry.getKey()+" : "+lstServices.size());
								
							if (lstServices.size()>0) {
								
								logger.debug(logmsg+"--> Existen "+lstServices.size()+" servicios para ejecutar proceso!");
								/**
								 * Debe seleccionar el servicio disponible de la lista encontrada si es que hay mas de uno
								 */
								logger.debug(logmsg+"--> Asignando un Servicio al proceso...");
								
								String srvID = fc.assignService(lstServices, entry.getValue().getGrpID(), entry.getValue().getTypeProc());
								
								logger.info(logmsg+"--> Se ha asignado el servicio: "+srvID+ " al proceso: "+entry.getKey());
								
								//Se crea la Task asignandole un srvID y se cambia el status a READY
		
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
