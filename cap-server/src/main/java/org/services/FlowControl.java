package org.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.PGPending;
import org.model.ProcControl;
import org.model.Service;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


public class FlowControl {
	Logger logger = Logger.getLogger("FlowControl");
	GlobalParams gParams;
	Rutinas mylib = new Rutinas();
	
	public FlowControl() {
	}
	
	public FlowControl(GlobalParams m) {
		gParams = m;
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
	
	public List<String> isServiceAvailable(String cliID, String typeProc) {
		final String module = "isServiceAvailable()";
		final String logmsg = module+" - ";
		List<String> lstServices = new ArrayList<>();
		
		try {
			
			logger.info("Total de Servicios Registrados: "+gParams.getMapService().size());
			
			if (gParams.getMapService().size()>0) {
				
				for(Map.Entry<String, Service> entry : gParams.getMapService().entrySet()) {
					
					Calendar cal = Calendar.getInstance();
					Date fecNow = cal.getTime();
					Date fecUpdate = entry.getValue().getFecUpdate();
					int txpMain = entry.getValue().getTxpMain();
					
					logger.info(logmsg+"Validando Servicio: "+entry.getKey());
					logger.info(logmsg+"Ultima hora de actualizacion: "+fecUpdate);
					
					//Valida si el servicio se ha respotado en txpMain
					if (mylib.getMinuteDiff(fecNow, fecUpdate)<=txpMain) {
						
						logger.info(logmsg+"Servicio "+entry.getKey()+" se encuentra activo!");
						logger.info(logmsg+"Recuperando paramtros del servicio...");
						
						//Obtiene los clientes y tipos de procesos que puede ejecutar
						JSONObject jo = new JSONObject(entry.getValue().getSrvTypeProc());
						JSONArray lstTypeProc = jo.getJSONArray("lstProc");
						JSONArray lstCli = jo.getJSONArray("lstCli");
						
						logger.info(logmsg+"Tipo de Procesos autorizados: "+lstTypeProc.toString());
						logger.info(logmsg+"Clientes autorizados: "+lstCli.toString());
	
						if (isContainValue(lstTypeProc, "typeProc", typeProc)) {
							if (isContainValue(lstCli, cliID)) {
								lstServices.add(entry.getKey());
							}
						}
					}
				}
			} else {
				logger.info(logmsg+"No hay servicios registrados para ejecutar procesos!");
			}
			return lstServices;
		} catch (Exception e) {
			logger.error(logmsg+"Error general: "+e.getMessage());
			return lstServices;
		}
	}
	
	public void updateMapProcControl(String key, String status, int errCode, String errMesg) {
		try {
			//Actualiza status MapProcControl Global
			if (gParams.getMapProcControl().containsKey(key)) {
				switch (status) {
				case "SUCCESS":
					gParams.getMapProcControl().get(key).setStatus("FINISHED");
					gParams.getMapProcControl().get(key).setuStatus(status);
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
					gParams.getMapProcControl().get(key).setErrCode(errCode);
					break;
				case "ERROR":
					gParams.getMapProcControl().get(key).setStatus("FINISHED");
					gParams.getMapProcControl().get(key).setuStatus(status);
					gParams.getMapProcControl().get(key).setFecUpdate(mylib.getDate());
					gParams.getMapProcControl().get(key).setErrCode(errCode);
					gParams.getMapProcControl().get(key).setErrMesg(errMesg);
					break;
				default:
					gParams.getMapProcControl().get(key).setStatus(status);
					gParams.getMapProcControl().get(key).setuStatus(status);
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
	
//	public void setEnablePrimaryThread() {
//		gParams.getMapEnableThreadControl().put("thProcess", true);
//		gParams.getMapEnableThreadControl().put("thListener", true);
//		gParams.getMapEnableThreadControl().put("thSync", true);
//	}
//	
//	public void setEnableSecondaryThread() {
//		gParams.getMapEnableThreadControl().put("thProcess", false);
//		gParams.getMapEnableThreadControl().put("thListener", true);
//		gParams.getMapEnableThreadControl().put("thSync", true);
//	}
//
//	public boolean isEnableThread(String thName) {
//		try {
//			return gParams.getMapEnableThreadControl().get(thName);
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	
//	public void setEnableThread(String thName) {
//		try {
//			if (gParams.getMapEnableThreadControl().containsKey(thName)) {
//				gParams.getMapEnableThreadControl().put(thName, true);
//			}
//		} catch (Exception e) {
//			logger.error("setEnableThread: "+e.getMessage());
//		}
//	}
//
//	public void setDisableThread(String thName) {
//		try {
//			if (gParams.getMapEnableThreadControl().containsKey(thName)) {
//				gParams.getMapEnableThreadControl().put(thName, false);
//			}
//		} catch (Exception e) {
//			logger.error("setEnableThread: "+e.getMessage());
//		}
//	}
	
	public int getListenerPort() {
		if (gParams.getInfo().getMonRol().equals("PRIMARY")) {
			return gParams.getInfo().getMonPort();
		} else {
			return gParams.getInfo().getSmonPort();
		}
	}
}
