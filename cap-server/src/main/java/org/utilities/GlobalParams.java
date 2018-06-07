package org.utilities;

import java.util.HashMap;
import java.util.Map;

import org.model.Info;
import org.model.MonParams;

public class GlobalParams {
	
	//Load from External Params
	private String monID;
	private String pathConfig;
	private String fileConfig;
	
	//Map de monParams recibidos desde Metadata
	Map<String, MonParams> mapMonParams = new HashMap<>();
	Map<String, Boolean> mapThreadRunnig = new HashMap<>();
	

	
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
}
