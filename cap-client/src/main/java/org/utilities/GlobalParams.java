package org.utilities;

import java.util.HashMap;
import java.util.Map;

import org.model.Config;
import org.model.Info;
import org.model.Service;
import org.model.Task;


public class GlobalParams {
	
	//Load from External Params
	private String srvID;
	private String pathConfig;
	private String fileConfig;
	

	//Clase con parametros del servicio
	private Service service = new Service();
	private Config cfgParams = new Config();
	
	//Map para validar si los threads están levantados o no
	Map<String, Boolean> mapThreadRunnig = new HashMap<>();
	
	private Map<String, Task> mapTask = new HashMap<>();
	
	private Info info;

	//Getter and Setter
	
	
	public String getPathConfig() {
		return pathConfig;
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
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public String getSrvID() {
		return srvID;
	}
	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}
	public Map<String, Boolean> getMapThreadRunnig() {
		return mapThreadRunnig;
	}
	public void setMapThreadRunnig(Map<String, Boolean> mapThreadRunnig) {
		this.mapThreadRunnig = mapThreadRunnig;
	}
	public Config getCfgParams() {
		return cfgParams;
	}
	public void setCfgParams(Config cfgParams) {
		this.cfgParams = cfgParams;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public Map<String, Task> getMapTask() {
		return mapTask;
	}
	public void setMapTask(Map<String, Task> mapTask) {
		this.mapTask = mapTask;
	}
}
