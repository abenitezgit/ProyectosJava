package org.utilities;

public class MyUtils {
	GlobalParams gParams;
	
	public MyUtils(GlobalParams m) {
		this.gParams = m;
	}
	
	public String getFilePath(String localPath) {
		String filePath=gParams.getAppConfig().getWorkPath();
		
//		if (!mylib.isNullOrEmpty(localPath)) {
//			filePath = localPath;
//		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0,filePath.length()-1);
		}
		return filePath;
	}


}
