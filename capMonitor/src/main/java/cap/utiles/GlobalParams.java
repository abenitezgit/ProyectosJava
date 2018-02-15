package cap.utiles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import cap.model.InfoConfig;
import cap.model.ProcControl;
import cap.model.Service;
import cap.model.Task;

public class GlobalParams {
	private String pathConfig;
	private String fileConfig;
	private Logger logger;
	private InfoConfig info = new InfoConfig();
	
	//Process Control
	private Map<String, Boolean> mapEnableThreadControl = new HashMap<>();
	private Map<String, ScheduledExecutorService> mapExecutors = new HashMap<>();
	private Map<String, ProcControl> mapProcControl = new HashMap<>();
	private Map<String, Task> mapTask = new HashMap<>();
	private Map<String, Service> mapService = new HashMap<>();
	private boolean mainAvtive;
	
	//DataBase Controller
	private boolean dbValid;

	//Getter and Setter

	
	public String getPathConfig() {
		return pathConfig;
	}
	public Map<String, Service> getMapService() {
		return mapService;
	}
	public void setMapService(Map<String, Service> mapService) {
		this.mapService = mapService;
	}
	public Map<String, Task> getMapTask() {
		return mapTask;
	}
	public void setMapTask(Map<String, Task> mapTask) {
		this.mapTask = mapTask;
	}
	public Map<String, ProcControl> getMapProcControl() {
		return mapProcControl;
	}
	public void setMapProcControl(Map<String, ProcControl> mapProcControl) {
		this.mapProcControl = mapProcControl;
	}
	public Map<String, ScheduledExecutorService> getMapExecutors() {
		return mapExecutors;
	}
	public void setMapExecutors(Map<String, ScheduledExecutorService> mapExecutors) {
		this.mapExecutors = mapExecutors;
	}
	public boolean isMainAvtive() {
		return mainAvtive;
	}
	public void setMainAvtive(boolean mainAvtive) {
		this.mainAvtive = mainAvtive;
	}
	public Map<String, Boolean> getMapEnableThreadControl() {
		return mapEnableThreadControl;
	}
	public void setMapEnableThreadControl(Map<String, Boolean> mapEnableThreadControl) {
		this.mapEnableThreadControl = mapEnableThreadControl;
	}
	public boolean isDbValid() {
		return dbValid;
	}
	public void setDbValid(boolean dbValid) {
		this.dbValid = dbValid;
	}
	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}
	public String getFileConfig() {
		return fileConfig;
	}
	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public InfoConfig getInfo() {
		return info;
	}
	public void setInfo(InfoConfig info) {
		this.info = info;
	}
	
}
