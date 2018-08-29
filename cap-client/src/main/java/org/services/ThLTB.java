package org.services;

import org.apache.log4j.Logger;
import org.model.LoadTable;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThLTB extends Thread {
	final String className = "thLTB";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThLTB(GlobalParams m, Task task) {
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

			mylog.info("Iniciando Ejecución de Carga de Datos...");
			
			mylog.info("Se Cargará processID: "+task.getProcID());
			
			mylog.info("Parseando parametros del LTB...");
			LoadTable ltb = (LoadTable) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Load Desc: "+ltb.getLtbDesc());
			mylog.info("Load Fixed: "+ltb.getLtbLoadFixed());
			mylog.info("Table Name: "+ltb.getLtbTableName());
			mylog.info("Load FileName: "+mylib.parseFnParam(ltb.getLtbFileName()));
			

			mylog.info("Instanciando Clase ServiceoLTB...");
			ServiceLTB serviceLtb = new ServiceLTB(gParams, ltb, mylog);

//			mylog.info("Generando Parsea de Parametros de OSP...");
//			serviceOsp.genParsedOspParams();
//
//			mylog.info("Ejecutando Store Procedure...");
//			if (serviceOsp.execute()) {
//				fc.updateStatusSuccessTask(task.getTaskkey());
//				mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
//			} else {
//				fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
//				mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
//			}
//		
//			fc.removeUsedThreadProc(task.getTypeProc());
//			mylog.info("Finalizando Ejecución de Store Procedure");
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
			case "LTB":
				String strLtb = mylib.serializeObjectToJSon(task.getParam(), false);
				LoadTable ltb = (LoadTable) mylib.serializeJSonStringToObject(strLtb, LoadTable.class);
				response = ltb;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
