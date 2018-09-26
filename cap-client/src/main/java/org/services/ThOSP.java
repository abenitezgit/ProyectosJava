package org.services;

import org.apache.log4j.Logger;
import org.model.Osp;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThOSP extends Thread {
	final String className = "thOSP";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThOSP(GlobalParams m, Task task) {
		gParams = m;
		fc = new FlowControl(gParams);
		this.task = task;
		this.taskID = task.getTaskkey();
		mylog = new MyLogger(className, taskID);
		logger = mylog.getLogger();
	}
	
    @Override
	public void run() {
		try {
			//Set LogLevel
			mylib.setLevelLogger(logger, gParams.getAppConfig().getLog4jLevel());

			mylog.info("Iniciando Ejecución de Store Procedure...");
			
			mylog.info("Se ejecutara SP: "+task.getProcID());
			
			mylog.info("Parseando parametros del OSP...");
			Osp osp = (Osp) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Osp Name: "+osp.getOspName());
			mylog.info("Osp Desc: "+osp.getOspDesc());
			mylog.info("Osp Cliente: "+osp.getCliDesc());
			mylog.info("Osp DbName: "+osp.getDbName());
			mylog.info("Osp Server IP: "+osp.getServerIP());
			mylog.info("Osp LoginID: "+osp.getDbLoginUser());
			mylog.info("Osp ownerID: "+osp.getDbOwnerUser());

			mylog.info("Instanciando Clase ServiceoOSP...");
			ServiceOSP serviceOsp = new ServiceOSP(osp, mylog);

			mylog.info("Generando Parsea de Parametros de OSP...");
			serviceOsp.genParsedOspParams();

			mylog.info("Seteando fecha de Proceso...");
			String numSecExec = taskID.split(":")[1];
			serviceOsp.setFecTask(mylib.getDate(numSecExec, "yyyyMMddHHmm"));
			
			mylog.info("Ejecutando Store Procedure...");
			if (serviceOsp.execute()) {
				fc.updateStatusSuccessTask(task.getTaskkey());
				mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
			} else {
				fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
				mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
			}
		
			fc.removeUsedThreadProc(task.getTypeProc());
			mylog.info("Finalizando Ejecución de Store Procedure");
		} catch (Exception e) {
			try {
				fc.updateStatusErrorTask(task.getTaskkey(), 90, e.getMessage().toString());
				fc.removeUsedThreadProc(task.getTypeProc());
				mylog.error("Exception error en Ejecución de Store Procedure: "+e.getMessage().toString());
			} catch (Exception er) {}
		} 
    }
    
    
	private Object parsingTaskParams(Task task) throws Exception {
		try {
			Object response = null;
			
			switch (task.getTypeProc()) {
			case "OSP":
				String strOsp = mylib.serializeObjectToJSon(task.getParam(), false);
				Osp osp = (Osp) mylib.serializeJSonStringToObject(strOsp, Osp.class);
				response = osp;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
