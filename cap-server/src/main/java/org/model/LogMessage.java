package org.model;

import java.util.Date;

public class LogMessage {
	
	private long logID;
	private long messageID;
	private Date timesTamp;
	private String logType;
	private String appName;
	private String loggerName;
	private String moduleName;
	private String messageText;
	
	public long getMessageID() {
		return messageID;
	}
	public void setMessageID(long messageID) {
		this.messageID = messageID;
	}
	public Date getTimesTamp() {
		return timesTamp;
	}
	public void setTimesTamp(Date timesTamp) {
		this.timesTamp = timesTamp;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public long getLogID() {
		return logID;
	}
	public void setLogID(long logID) {
		this.logID = logID;
	}
}
