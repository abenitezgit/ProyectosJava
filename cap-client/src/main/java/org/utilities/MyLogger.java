package org.utilities;

import org.apache.log4j.Logger;

public class MyLogger {
	Logger logger;
	private String className;
	private String prefix;
	
	
	public MyLogger(String className, String prefix) {
		this.logger = Logger.getLogger(className);
		this.prefix = prefix;
	}

	public Logger getLogger() {
		return this.logger;
	}
	
	public void info(String message) {
		logger.info("["+prefix+"] - "+message);
	}
	
	public void error(String message) {
		logger.error("["+prefix+"] - "+message);
	}
	
	public void debug(String message) {
		logger.debug("["+prefix+"] - "+message);
	}

	public void warn(String message) {
		logger.warn("["+prefix+"] - "+message);
	}

	public String getClassName() {
		return className;
	}
}
