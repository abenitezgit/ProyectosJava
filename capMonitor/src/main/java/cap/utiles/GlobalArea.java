package cap.utiles;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cap.model.Info;
import cap.model.Module;

public class GlobalArea {
	private String pathConfig;
	private String fileConfig;
	private String logConfig;
	private Logger logger;
	private String serviceName;
	private Info info = new Info();
	private Map<String, Module> mapModule = new HashMap<>();
	
	//Constructor
	public GlobalArea(String vServiceName) {
		fileConfig = "capMonitor.properties";
		logConfig = "log4j.properties";
		setServiceName(vServiceName);
		logger = Logger.getLogger(vServiceName);
	}

	//Getter and Setter
	
	public String getFileConfig() {
		return fileConfig;
	}
	
	public Map<String, Module> getMapModule() {
		return mapModule;
	}

	public void setMapModule(Map<String, Module> mapModule) {
		this.mapModule = mapModule;
	}

	public String getLogConfig() {
		return logConfig;
	}

	public void setLogConfig(String logConfig) {
		this.logConfig = logConfig;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}

	//Metodos públicos
	
	
}
