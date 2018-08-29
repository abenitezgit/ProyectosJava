package org.utilities;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.model.LogMessage;

public class MyLogger {
	GlobalParams gParams;
	Logger logger;
	private String className;
	private String prefix;
	long messageID;
	
	
	public MyLogger(GlobalParams m, String className, String prefix) {
		this.gParams = m;
		this.logger = Logger.getLogger(className);
		this.prefix = prefix;
		this.messageID = getMessageID();
		this.className = className;
	}

	public Logger getLogger() {
		return this.logger;
	}
	
	public void info(String message) {
		logger.info("["+prefix+"] - "+message);
		putNewLog("INFO",message);
	}
	
	public void error(String message) {
		logger.error("["+prefix+"] - "+message);
		putNewLog("ERROR",message);
	}
	
	public void debug(String message) {
		logger.debug("["+prefix+"] - "+message);
		putNewLog("DEBUG",message);
	}

	public void warn(String message) {
		logger.warn("["+prefix+"] - "+message);
		putNewLog("WARN",message);
	}

	public String getClassName() {
		return className;
	}
	
	private synchronized void putNewLog(String logType, String message) {
		LogMessage lgm = new LogMessage();
		
		lgm.setAppName(gParams.getAppConfig().getMonID());
		lgm.setLoggerName(className);
		lgm.setLogType(logType);
		lgm.setMessageID(messageID);
		lgm.setMessageText(message);
		lgm.setModuleName(prefix);
		lgm.setTimesTamp(new Date());
		lgm.setLogID(getLogID());
		
		gParams.getLinkedLog().add(lgm);
	}
	
	private synchronized long getMessageID() {
		long lastMessageID = gParams.getMessageID();
		long nextID = gParams.getMessageID()+1;
		gParams.setMessageID(nextID);
		return lastMessageID;
	}
	
	private synchronized long getLogID() {
		long lastLogID = gParams.getLogID();
		long nextID = gParams.getLogID()+1;
		gParams.setLogID(nextID);
		return lastLogID;
	}
}
