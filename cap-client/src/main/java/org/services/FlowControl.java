package org.services;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.model.Service;
import org.model.Task;
import org.model.TypeProc;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class FlowControl {
	Logger logger = Logger.getLogger("FlowControl");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	public FlowControl(GlobalParams m) {
		gParams = m;
	}
	
	public synchronized void removeUsedThreadProc(String typeProc) throws Exception {
		try {
			int usedThread = gParams.getService().getMapTypeProc().get(typeProc).getUsedThread();
			usedThread--;
			gParams.getService().getMapTypeProc().get(typeProc).setUsedThread(usedThread);
			
		} catch (Exception e) {
			throw new Exception("removeUsedThreadProc(): "+e.getMessage());
		}
	}
	
	public synchronized void addUsedThreadProc(String typeProc) throws Exception {
		try {
			int usedThread = gParams.getService().getMapTypeProc().get(typeProc).getUsedThread();
			usedThread++;
			gParams.getService().getMapTypeProc().get(typeProc).setUsedThread(usedThread);
			
		} catch (Exception e) {
			throw new Exception("addUsedThreadProc(): "+e.getMessage());
		}
	}
	
	private synchronized void addTask(String key, Task task) {
		gParams.getMapTask().put(key, task);
	}

	
	public synchronized void updateStatusSuccessTask(String key) throws Exception {
		if (gParams.getMapTask().containsKey(key)) {
			gParams.getMapTask().get(key).setStatus("FINISHED");
			gParams.getMapTask().get(key).setuStatus("SUCCESS");
			gParams.getMapTask().get(key).setFecUpdate(mylib.getDate());
			gParams.getMapTask().get(key).setFecFinished(mylib.getDate());
		}
		
	}
	
	public synchronized void updateStatusErrorTask(String key, int errCode, String errMesg) throws Exception {
		if (gParams.getMapTask().containsKey(key)) {
			gParams.getMapTask().get(key).setStatus("FINISHED");
			gParams.getMapTask().get(key).setuStatus("ERROR");
			gParams.getMapTask().get(key).setFecUpdate(mylib.getDate());
			gParams.getMapTask().get(key).setFecFinished(mylib.getDate());
			gParams.getMapTask().get(key).setErrCode(errCode);
			gParams.getMapTask().get(key).setErrMesg(errMesg);
		}
		
	}
	
	public synchronized void updateStatusTask(String key, String status) throws Exception {
		try {
			if (gParams.getMapTask().containsKey(key)) {
				gParams.getMapTask().get(key).setStatus(status);
				gParams.getMapTask().get(key).setFecUpdate(mylib.getDate());
			}
			
		} catch (Exception e) {
			throw new Exception("updateStatusTask"+e.getMessage());
		}
	}
	
	public boolean isExistFreeThread(String typeProc) throws Exception {
		try {
			if (gParams.getService().getMapTypeProc().containsKey(typeProc)) {
				int usedThread = gParams.getService().getMapTypeProc().get(typeProc).getUsedThread();
				int maxThread = gParams.getService().getMapTypeProc().get(typeProc).getMaxThread();
				
				if (usedThread < maxThread) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception("isExistFreeThread(): "+e.getMessage());
		}
	}
	
	public void updateTaskProcess(String strMapTask) throws Exception {
		try {
			JSONObject jo = new JSONObject(strMapTask);
			String[] names = JSONObject.getNames(jo);
			
			if (names.length>0) {
				for (String key : names) {
					Task task = (Task) mylib.serializeJSonStringToObject(jo.get(key).toString(), Task.class);
					task.setFecUpdate(mylib.getDate());
					addTask(key, task);
				}
			}
		} catch (Exception e) {
			throw new Exception("updateTaskProcess(): "+e.getMessage());
		}
	}
	
	public void updateService(String strServiceNew) throws Exception {
		String logmsg = "updateService() - ";
		
		try {
			/*
			 * Se actualizaran solo los siguiente valores
			 * 	- mapTypeProc
			 * 	- mapCli
			 *  - pctBalance
			 *  - orderBalance
			 *  - enable
			 */
			
			logger.info(logmsg+"Actualizando parametros del servicio...");
			logger.info(logmsg+"Parametros recibidos: "+strServiceNew);
			
			Service serviceNew = (Service) mylib.serializeJSonStringToObject(strServiceNew, Service.class);
			Map<String, TypeProc> mapTypeProcNew = serviceNew.getMapTypeProc();

			Service service = gParams.getService();
			String strService = mylib.serializeObjectToJSon(service, false);
			logger.info(logmsg+"Parametros actuales: "+strService);
			
			Map<String, TypeProc> mapTypeProc = service.getMapTypeProc();
						
			//Update mapTypeProc
			for (Map.Entry<String, TypeProc> entry : mapTypeProcNew.entrySet()) {
				TypeProc tpNew = entry.getValue();
				
				if (mapTypeProc.containsKey(entry.getKey())) {
					TypeProc tp = mapTypeProc.get(tpNew.getTypeProc());
					tp.setMaxThread(tpNew.getMaxThread());
					tp.setPriority(tpNew.getPriority());
					
					mapTypeProc.put(entry.getKey(), tp);
				} else {
					mapTypeProc.put(entry.getKey(), tpNew);
				}
			}
			
			serviceNew.setMapTypeProc(mapTypeProc);
			
			gParams.setService(serviceNew);
			
			logger.info(logmsg+"Parametros del Servicio actualizados!");
		} catch (Exception e) {
			throw new Exception("updateSrvParams(): "+e.getMessage());
		}
	}

}
