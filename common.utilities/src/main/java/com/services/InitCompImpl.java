package com.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.rutinas.Rutinas;

public class InitCompImpl implements IInitComponents {
	Rutinas mylib = new Rutinas();
	Logger logger;
	
	public InitCompImpl(Object clase) {
		logger = Logger.getLogger(clase.getClass());
	}

	@Override
	public JSONObject getResourceConfig(List<String> params) {
		try {
			//Lee archivo de configuración
			ClassLoader classLoader = getClass().getClassLoader();
			logger.info("classLoader: "+classLoader.getResource("config.properties").getPath());
			File file = new File(classLoader.getResource("config.properties").getFile());
			
			Properties conf = new Properties();
			conf.load(new FileInputStream(file));
			
			JSONObject joParams = new JSONObject();
			
			for (int i=0; i<params.size(); i++) {
				String param = params.get(i);
				joParams.put(param, mylib.nvlString(conf.getProperty(param)));
			}
			
			return joParams;
		} catch (Exception e) {
			logger.error("getResourceConfig: "+e.getMessage());
			return null;
		}

	}

	@Override
	public JSONObject getAppConfig(String path, String fileConfig, String cloudName, List<String> params) {
		try {
			Properties conf = new Properties();
			
			conf.load(new FileInputStream(path+"/"+fileConfig));
			
			JSONObject joParams = new JSONObject();
			
			for (String param : params) {
				joParams.put(param, mylib.nvlString(conf.getProperty(cloudName+"."+param)));
			}
			
			return joParams;
		} catch (Exception e) {
			logger.error("getAppConfig: "+e.getMessage());
			return null;
		}

	}

}
