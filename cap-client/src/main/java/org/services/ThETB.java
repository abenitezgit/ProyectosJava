package org.services;

import org.apache.log4j.Logger;
import org.model.ExpTable;
import org.model.Ftp;
import org.model.Osp;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThETB extends Thread {
	final String className = "thETB";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThETB(GlobalParams m, Task task) {
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

			mylog.info("Iniciando Ejecución de Exportación de Datos...");
			
			mylog.info("Se ejecutara Exportación: "+task.getProcID());
			
			mylog.info("Parseando parametros del ETB...");
			ExpTable etb = (ExpTable) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Exp Desc: "+etb.getEtbDesc());
			mylog.info("Exp TableName: "+etb.getEtbTableName());
			mylog.info("Exp FileName: "+etb.getEtbFileName());

			mylog.info("Instanciando Clase ServiceoETB...");
			ServiceETB serviceEtb = new ServiceETB(gParams, etb, mylog);
			
			mylog.info("Seteando fecha de Proceso...");
			String numSecExec = taskID.split(":")[1];
			serviceEtb.setFecTask(mylib.getDate(numSecExec, "yyyyMMddHHmm"));


			mylog.info("Ejecutando Exportación...");
			
			if (serviceEtb.execute()) {
				fc.updateStatusSuccessTask(task.getTaskkey());
				mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
			} else {
				fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
				mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
			}
		
			fc.removeUsedThreadProc(task.getTypeProc());
			mylog.info("Finalizando Ejecución de Exportación de Datos");
		} catch (Exception e) {
			try {
				fc.updateStatusErrorTask(task.getTaskkey(), 90, e.getMessage().toString());
				fc.removeUsedThreadProc(task.getTypeProc());
				mylog.error("Exception error en Ejecución de Exportación de Datos: "+e.getMessage().toString());
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
					break;
				case "ETB":
					String strEtb = mylib.serializeObjectToJSon(task.getParam(), false);
					ExpTable etb = (ExpTable) mylib.serializeJSonStringToObject(strEtb, ExpTable.class);
					response = etb;
					break;
				case "FTP":
					String strFtp = mylib.serializeObjectToJSon(task.getParam(), false);
					Ftp ftp = (Ftp) mylib.serializeJSonStringToObject(strFtp, Ftp.class);
					response = ftp;
					break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
