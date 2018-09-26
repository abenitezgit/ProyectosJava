package org.services;

import org.apache.log4j.Logger;
import org.model.Mov;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThMOV extends Thread {
	final String className = "thMOV";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThMOV(GlobalParams m, Task task) {
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

			mylog.info("Iniciando Ejecución de Data Moving...");
			
			mylog.info("Se ejecutara Task: "+task.getProcID());
			
			mylog.info("Parseando parametros del MOV...");
			Mov mov = (Mov) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Mov Desc: "+mov.getMovDesc());
			mylog.info("Source table name: "+mov.getsTbName());
			mylog.info("Dest table name: "+mov.getdTbName());

			mylog.info("Instanciando Clase ServiceoMOV...");
			ServiceMOV sMov = new ServiceMOV(mov, mylog);

			mylog.info("Ejecutando Data Moving...");
			if (sMov.execute()) {
				fc.updateStatusSuccessTask(task.getTaskkey(), sMov.getTxResult());
				mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
			} else {
				fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido", sMov.getTxResult());
				mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
			}
		
			fc.removeUsedThreadProc(task.getTypeProc());
			mylog.info("Finalizando Ejecución de Data Moving");
		} catch (Exception e) {
			try {
				fc.updateStatusErrorTask(task.getTaskkey(), 90, e.getMessage().toString());
				fc.removeUsedThreadProc(task.getTypeProc());
				mylog.error("Exception error en Ejecución de Data Moving: "+e.getMessage().toString());
			} catch (Exception er) {}
		} 
    }
    
    
	private Object parsingTaskParams(Task task) throws Exception {
		try {
			Object response = null;
			
			switch (task.getTypeProc()) {
			case "MOV":
				String strMov = mylib.serializeObjectToJSon(task.getParam(), false);
				Mov mov = (Mov) mylib.serializeJSonStringToObject(strMov, Mov.class);
				response = mov;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
