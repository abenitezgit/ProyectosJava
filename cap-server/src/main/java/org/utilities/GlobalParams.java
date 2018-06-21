package org.utilities;

import java.util.HashMap;
import java.util.Map;

import org.model.Info;
import org.model.MonParams;
import org.model.ProcControl;
import org.model.Service;
import org.model.Task;

public class GlobalParams {
	
	//Load from External Params
	private String monID;
	private String pathConfig;
	private String fileConfig;
	
	//Map de monParams recibidos desde Metadata
	//key = monID
	//MonParams = Clase MonParams.class con parametros del monID correspondiente
	Map<String, MonParams> mapMonParams = new HashMap<>();
	
	//Map para validar si los threads están levantados o no
	Map<String, Boolean> mapThreadRunnig = new HashMap<>();
	
	private Map<String, Task> mapTask = new HashMap<>();
	private Map<String, Service> mapService = new HashMap<>();
	private Map<String, ProcControl> mapProcControl = new HashMap<>();
	
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
	public String getMonID() {
		return monID;
	}
	public void setMonID(String monID) {
		this.monID = monID;
	}
	public Map<String, MonParams> getMapMonParams() {
		return mapMonParams;
	}
	public void setMapMonParams(Map<String, MonParams> mapMonParams) {
		this.mapMonParams = mapMonParams;
	}
	public Map<String, Boolean> getMapThreadRunnig() {
		return mapThreadRunnig;
	}
	public void setMapThreadRunnig(Map<String, Boolean> mapThreadRunnig) {
		this.mapThreadRunnig = mapThreadRunnig;
	}
	public Map<String, Task> getMapTask() {
		return mapTask;
	}
	public void setMapTask(Map<String, Task> mapTask) {
		this.mapTask = mapTask;
	}
	public Map<String, Service> getMapService() {
		return mapService;
	}
	public void setMapService(Map<String, Service> mapService) {
		this.mapService = mapService;
	}
	public Map<String, ProcControl> getMapProcControl() {
		return mapProcControl;
	}
	public void setMapProcControl(Map<String, ProcControl> mapProcControl) {
		this.mapProcControl = mapProcControl;
	}
}
