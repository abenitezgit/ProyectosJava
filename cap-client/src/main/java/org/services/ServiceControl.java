package org.services;


import java.util.Map;

import org.apache.log4j.Logger;
import org.model.Service;
import org.model.TypeProc;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

public class ServiceControl {
	Logger logger = Logger.getLogger("ServiceControl");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	SocketService ss;
	FlowControl fc;
	
	public ServiceControl(GlobalParams m) {
		gParams = m;
		ss = new SocketService(gParams);
		fc = new FlowControl(gParams);
	}
	
	public void showSrvParams() {
		Service srv = gParams.getService();
		logger.info("Srv Enable: "+srv.getEnable());
		logger.info("Srv ID: "+srv.getSrvID());
		logger.info("Srv Desc: "+srv.getSrvDesc());
		logger.info("Srv Refresh Time: "+srv.getTxpMain());
		logger.info("Srv Last Update: "+srv.getFecStatus());
		logger.info("Srv order balance: "+srv.getOrderBalance());
		logger.info("Srv pct balance: "+srv.getPctBalance());
		logger.info("Srv strTypeProc: "+srv.getSrvTypeProc());
		
		for (Map.Entry<String, TypeProc> entry : gParams.getService().getMapTypeProc().entrySet()) {
			logger.info("Srv TypeProc: "+entry.getKey()+" Priority: "+entry.getValue().getPriority()+ " MaxThread: "+entry.getValue().getMaxThread()+" UsedTread: "+entry.getValue().getUsedThread());
		}
		
		for (Map.Entry<String, String> entry : gParams.getService().getMapCli().entrySet()) {
			logger.info("Srv Cli: "+entry.getKey());
		}
	}
	
	public void syncServiceParams(String srvID) throws Exception {
		try {
			String strService="";
			
			switch (gParams.getCfgParams().getConnectTypeMon()) {
			case "SOCKET":
					strService = ss.getServiceParams(srvID);
				break;
			case "URL":
				break;
			}
			
			fc.updateService(strService);
			
			logger.info("Variable global actualizada: "+mylib.serializeObjectToJSon(gParams.getService(), false));
		} catch (Exception e) {
			throw new Exception("getServiceParams: "+e.getMessage());
		}
	}

}
