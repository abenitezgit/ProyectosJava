package org.services;

import java.util.Map;

import org.apache.log4j.Logger;
import org.model.Service;
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
	
	public synchronized void addUsedThreadProc(String typeProc) throws Exception {
		try {
			int usedThread = gParams.getService().getMapTypeProc().get(typeProc).getUsedThread();
			usedThread++;
			gParams.getService().getMapTypeProc().get(typeProc).setUsedThread(usedThread);
			
		} catch (Exception e) {
			throw new Exception("updateMapTypeProc(): "+e.getMessage());
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
