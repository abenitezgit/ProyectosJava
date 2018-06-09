package com.fileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class ReadConfig implements IFileConfig {
	Logger logger = Logger.getLogger(ReadConfig.class);
	boolean status=false;
	JSONObject joMap = new JSONObject();
	
	@Override
	public void loadFileConfig(String path, String fileConfig, List<String> parameters) {
		try {
			logger.info("cargando valores...");
			
			Properties p = new Properties();
			
			p.load(new FileInputStream(path+"/"+fileConfig));
			
			for (String param : parameters) {
				joMap.put(param, p.getProperty(param));
			}
			status=true;
		} catch (IOException  e) {
			logger.error("Error: "+e.getMessage());
		}
	}

	@Override
	public boolean loadStatus() {
		return status;
	}

	@Override
	public JSONObject getValues() {
		return joMap;
	}


}
