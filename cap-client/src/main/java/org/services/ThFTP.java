package org.services;

import org.apache.log4j.Logger;
import org.model.Ftp;
import org.model.Task;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ThFTP extends Thread {
	final String className = "thFTP";
	
	Logger logger; 
	MyLogger mylog;
	
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	FlowControl fc;
	Task task;
	String taskID;
	
	
//	static Procedure myproc;
//	static APIRest apiRest = new APIRest();
	
	public ThFTP(GlobalParams m, Task task) {
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

			mylog.info("Iniciando Ejecución de FTP...");
			
			mylog.info("Se ejecutara FTP: "+task.getProcID());
			
			mylog.info("Parseando parametros del FTP...");
			Ftp ftp = (Ftp) parsingTaskParams(task);
			
			mylog.info("Task ID: "+task.getGrpKey());
			mylog.info("Ftp ID: "+ftp.getFtpID());
			mylog.info("Ftp Desc: "+ftp.getFtpDesc());

			mylog.info("Instanciando Clase ServiceoFTP...");
			ServiceFTP serviceFtp = new ServiceFTP(gParams, ftp, mylog);
			
			mylog.info("Seteando fecha de Proceso...");
			String numSecExec = taskID.split(":")[1];
			serviceFtp.setFecTask(mylib.getDate(numSecExec, "yyyyMMddHHmm"));
			
			if (ftp.getFtpAction().equals("UPLOAD")) {
				if (ftp.getFtpSecure()==1) {
					mylog.info("Ejecutando Secure Upload Ftp...");
					if (serviceFtp.sendSFtpFiles()) {
						fc.updateStatusSuccessTask(task.getTaskkey());
						mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
					} else {
						fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
						mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
					}
				} else {
					mylog.info("Ejecutando Normal Upload Ftp...");
					if (serviceFtp.sendFtpFiles()) {
						fc.updateStatusSuccessTask(task.getTaskkey());
						mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
					} else {
						fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
						mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
					}
				}
			} else {
				if (ftp.getFtpSecure()==1) {
					mylog.info("Ejecutando Secure Download Ftp...");
					if (serviceFtp.downSFtpFiles()) {
						fc.updateStatusSuccessTask(task.getTaskkey());
						mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
					} else {
						fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
						mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
					}
				} else {
					mylog.info("Ejecutando Normal Download Ftp...");
					if (serviceFtp.downFtpFiles()) {
						fc.updateStatusSuccessTask(task.getTaskkey());
						mylog.info("Termino SUCCESS Ejecucion TaskID: "+task.getTaskkey());
					} else {
						fc.updateStatusErrorTask(task.getTaskkey(),90,"Error desconocido");
						mylog.error("Termino ERROR Ejecucion TaskID: "+task.getTaskkey());
					}
				}
			}
		
			fc.removeUsedThreadProc(task.getTypeProc());
			mylog.info("Finalizando Ejecución de FTP");
		} catch (Exception e) {
			try {
				fc.updateStatusErrorTask(task.getTaskkey(), 90, e.getMessage().toString());
				fc.removeUsedThreadProc(task.getTypeProc());
				mylog.error("Exception error en Ejecución de FTP: "+e.getMessage().toString());
			} catch (Exception er) {}
		} 
    }
    
    
	private Object parsingTaskParams(Task task) throws Exception {
		try {
			Object response = null;
			
			switch (task.getTypeProc()) {
			case "FTP":
				String strFtp = mylib.serializeObjectToJSon(task.getParam(), false);
				Ftp ftp = (Ftp) mylib.serializeJSonStringToObject(strFtp, Ftp.class);
				response = ftp;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("parsingTaskParams(): "+e.getMessage());
		}
	}


}
