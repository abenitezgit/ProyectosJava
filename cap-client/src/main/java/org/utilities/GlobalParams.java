package org.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.model.AppConfig;
import org.model.Service;
import org.model.Task;


public class GlobalParams {

	//Clase con parametros del servicio
	private Service service = new Service();
	private AppConfig appConfig = new AppConfig();
	
	private Map<String, Task> mapTask = new HashMap<>();
	
	private Map<String,String> mapThread = new HashMap<>();
	
	private ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
	private ScheduledExecutorService executorThProcess = Executors.newSingleThreadScheduledExecutor();
	private ScheduledExecutorService executorThSync = Executors.newSingleThreadScheduledExecutor();

	//Getter and Setter
	
	public Service getService() {
		return service;
	}
	public Map<String, String> getMapThread() {
		return mapThread;
	}
	public void setMapThread(Map<String, String> mapThread) {
		this.mapThread = mapThread;
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
	public ScheduledExecutorService getExecutorThMain() {
		return executorThMain;
	}
	public void setExecutorThMain(ScheduledExecutorService executorThMain) {
		this.executorThMain = executorThMain;
	}
	public ScheduledExecutorService getExecutorThProcess() {
		return executorThProcess;
	}
	public void setExecutorThProcess(ScheduledExecutorService executorThProcess) {
		this.executorThProcess = executorThProcess;
	}
	public ScheduledExecutorService getExecutorThSync() {
		return executorThSync;
	}
	public void setExecutorThSync(ScheduledExecutorService executorThSync) {
		this.executorThSync = executorThSync;
	}
	public AppConfig getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
}
