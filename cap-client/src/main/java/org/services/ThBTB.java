package org.services;

import org.apache.log4j.Logger;
import org.model.BackTable;
import org.model.ExpTable;
import org.model.Ftp;
import org.model.Osp;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThBTB extends Thread {
	final String className = "thBTB";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThBTB(GlobalParams m, Task task) {
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

			mylog.info("Iniciando Ejecución de Backup de Datos...");
			
			mylog.info("Se ejecutara Backup: "+task.getProcID());
			
			mylog.info("Parseando parametros del BTB...");
			BackTable btb = (BackTable) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Back Desc: "+btb.getBtbDesc());
			mylog.info("Back TableName: "+btb.getBtbTableName());
			mylog.info("Back FileName: "+btb.getBtbFileName());

			mylog.info("Instanciando Clase ServiceoBTB...");
			ServiceBTB serviceBtb = new ServiceBTB(gParams, btb, mylog);
			
			mylog.info("Seteando fecha de Proceso...");
			String numSecExec = taskID.split(":")[1];
			serviceBtb.setFecTask(mylib.getDate(numSecExec, "yyyyMMddHHmm"));

			mylog.info("Ejecutando Backup...");
			
			if (serviceBtb.execute()) {
				fc.updateStatusSuccessTask(task.getTaskkey());
				mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
			} else {
				fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
				mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
			}
		
			fc.removeUsedThreadProc(task.getTypeProc());
			mylog.info("Finalizando Ejecución de Backup de Datos");
		} catch (Exception e) {
			try {
				fc.updateStatusErrorTask(task.getTaskkey(), 90, e.getMessage().toString());
				fc.removeUsedThreadProc(task.getTypeProc());
				mylog.error("Exception error en Ejecución de Backup de Datos: "+e.getMessage().toString());
			} catch (Exception er) {}
		} 
    }
    
    
	private Object parsingTaskParams(Task task) throws Exception {
		try {
			Object response = null;
			
			switch (task.getTypeProc()) {
				case "BTB":
					String strBtb = mylib.serializeObjectToJSon(task.getParam(), false);
					BackTable btb = (BackTable) mylib.serializeJSonStringToObject(strBtb, BackTable.class);
					response = btb;
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
