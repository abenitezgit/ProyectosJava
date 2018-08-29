package org.services;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.model.Service;
import org.model.Task;
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
		
		mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());
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
	
	public void executeTask() throws Exception {
		try {
			Map<String, Task> mapTask = gParams.getMapTask()
					.entrySet()
					.stream()
					.filter(p -> p.getValue().getStatus().equals("READY"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			for (Map.Entry<String, Task> entry : mapTask.entrySet()) {
				String typeProc = entry.getValue().getTypeProc();
				switch(typeProc) {
					case "OSP":
						if (fc.isExistFreeThread(typeProc)) {
							Thread thOsp = new ThOSP(gParams, entry.getValue());
							fc.addUsedThreadProc(typeProc);
							fc.updateStatusTask(entry.getKey(),"RUNNING");
							thOsp.run();
							
						} else {
							logger.info("Esperando por Free Thread OSP...");
						}
						break;
					case "ETB":
						if (fc.isExistFreeThread(typeProc)) {
							Thread thETB = new ThETB(gParams, entry.getValue());
							fc.addUsedThreadProc(typeProc);
							fc.updateStatusTask(entry.getKey(),"RUNNING");
							thETB.run();
							
						} else {
							logger.info("Esperando por Free Thread ETB...");
						}
						break;
					case "FTP":
						if (fc.isExistFreeThread(typeProc)) {
							Thread thFTP = new ThFTP(gParams, entry.getValue());
							fc.addUsedThreadProc(typeProc);
							fc.updateStatusTask(entry.getKey(),"RUNNING");
							thFTP.run();
							
						} else {
							logger.info("Esperando por Free Thread FTP...");
						}
						break;
					case "MOV":
						if (fc.isExistFreeThread(typeProc)) {
							Thread thMOV = new ThMOV(gParams, entry.getValue());
							fc.addUsedThreadProc(typeProc);
							fc.updateStatusTask(entry.getKey(),"RUNNING");
							thMOV.run();
							
						} else {
							logger.info("Esperando por Free Thread MOV...");
						}
						break;
				}
			}
			
		} catch (Exception e) {
			throw new Exception("executeTask(): "+e.getMessage());
		}
	}
	
	public void showTaskProcess() throws Exception {
		for (Map.Entry<String, Task> entry : gParams.getMapTask().entrySet()) {
			logger.info("TaskID: "+entry.getKey()+" "+entry.getValue().getStatus()+" "+entry.getValue().getFecIns()+" "+entry.getValue().getFecUpdate());
		}
	}
	
	public void syncTaskProcess() throws Exception {
		try {
			switch (gParams.getAppConfig().getConnectTypeMon()) {
				case "SOCKET":
					if (ss.syncTaskProcess(gParams.getAppConfig().getSrvID())) {
						String strTaskProcess = ss.getSocketResponse();
						if (!mylib.isNullOrEmpty(strTaskProcess) && !strTaskProcess.equals("{}")) {
							fc.updateTaskProcess(strTaskProcess);
						} else {
							logger.info("No hay nuevos Task para agregar");
						}
					} else {
						throw new Exception("syncTaskProcess(): unable to syncService");
					}
					break;
				case "URL":
					break;
			}
			
		} catch (Exception e) {
			throw new Exception("syncTaskProcess(): "+e.getMessage());
		}
	}
	
	public void syncServiceParams(String srvID) throws Exception {
		try {
			
			switch (gParams.getAppConfig().getConnectTypeMon()) {
			case "SOCKET":
					if (ss.getServiceParams(srvID)) {
						String strService = ss.getSocketResponse();
						fc.updateService(strService);
					} else {
						throw new Exception("syncServiceParams(): unable to syncService");
					}
				break;
			case "URL":
				break;
			}
			
		} catch (Exception e) {
			throw new Exception("syncServiceParams(): "+e.getMessage());
		}
	}
	

}
