package org.utilities;

import com.rutinas.Rutinas;

public class MyUtils {
	GlobalParams gParams;
	Rutinas mylib = new Rutinas();
	
	public MyUtils(GlobalParams m) {
		this.gParams = m;
	}

	public String getLocalPath(String path) throws Exception {
		try {
			//Default path
			String localPath=gParams.getAppConfig().getWorkPath();
			
			if (!mylib.isNullOrEmpty(path)) {
				if (!path.toUpperCase().equals("LOCAL")) {
					localPath = path;
					localPath.replace("\\", "/");
				}
			}
			
			if (localPath.endsWith("/")) {
				localPath = localPath.substring(0,localPath.length()-1);
			}
			
			return localPath;
		} catch (Exception e) {
			throw new Exception("getLocalpath(): "+e.getMessage());
		}
	}
	
	public String getFormatedPath(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0,path.length()-1);
		}
		return path;
	}


}
