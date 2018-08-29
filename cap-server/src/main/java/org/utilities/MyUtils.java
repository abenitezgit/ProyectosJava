package org.utilities;

public class MyUtils {
	GlobalParams gParams;
	
	public MyUtils(GlobalParams m) {
		this.gParams = m;
	}
	
	
	public synchronized long getMessageID() {
		long lastMessageID = gParams.getMessageID();
		long nextID = gParams.getMessageID()+1;
		gParams.setMessageID(nextID);
		return lastMessageID;
	}
	
	public synchronized long getLogID() {
		long lastLogID = gParams.getLogID();
		long nextID = gParams.getLogID()+1;
		gParams.setLogID(nextID);
		return lastLogID;
	}

}
