package org.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.model.AgeGroup;
import org.model.AppConfig;
import org.model.Group;
import org.model.LogMessage;
import org.model.MonParams;
import org.model.ProcControl;
import org.model.Service;
import org.model.Task;

public class GlobalParams {
	
	//Map de monParams recibidos desde Metadata
	//key = monID
	//MonParams = Clase MonParams.class con parametros del monID correspondiente
	private AppConfig appConfig = new AppConfig();
	Map<String, MonParams> mapMonParams = new HashMap<>();
	
	//Map para validar si los threads están levantados o no
	Map<String, Boolean> mapThreadRunnig = new HashMap<>();
	
	private Map<String, Task> mapTask = new HashMap<>();
	private Map<String, Service> mapService = new HashMap<>();
	private Map<String, ProcControl> mapProcControl = new HashMap<>();
	private Map<String, ProcControl> mapProcHistory = new HashMap<>();
	private Map<String, String> mapAssignedService = new HashMap<>();
	private Map<String, Group> mapGroupParam = new HashMap<>();
	private Map<String, Integer> mapAgeGroupCount = new TreeMap<>();
	private Map<String, List<AgeGroup>> mapAgeGroup = new TreeMap<>();
	private LinkedList<ProcControl> lstForceCancelProcess = new LinkedList<>();
	
	ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
	
	private long logID;
	private long messageID;
	private LinkedList<LogMessage> linkedLog = new LinkedList<>();
	
	//Flow Control
	private boolean swFindNewGroup = true;
	private boolean swAssignNewTask = true;
	
	//Getter and Setter
	
	
	public Map<String, MonParams> getMapMonParams() {
		return mapMonParams;
	}
	public LinkedList<ProcControl> getLstForceCancelProcess() {
		return lstForceCancelProcess;
	}
	public void setLstForceCancelProcess(LinkedList<ProcControl> lstForceCancelProcess) {
		this.lstForceCancelProcess = lstForceCancelProcess;
	}
	public Map<String, List<AgeGroup>> getMapAgeGroup() {
		return mapAgeGroup;
	}
	public void setMapAgeGroup(Map<String, List<AgeGroup>> mapAgeGroup) {
		this.mapAgeGroup = mapAgeGroup;
	}
	public Map<String, Integer> getMapAgeGroupCount() {
		return mapAgeGroupCount;
	}
	public void setMapAgeGroupCount(Map<String, Integer> mapAgeGroupCount) {
		this.mapAgeGroupCount = mapAgeGroupCount;
	}
	public boolean isSwAssignNewTask() {
		return swAssignNewTask;
	}
	public void setSwAssignNewTask(boolean swAssignNewTask) {
		this.swAssignNewTask = swAssignNewTask;
	}
	public boolean isSwFindNewGroup() {
		return swFindNewGroup;
	}
	public void setSwFindNewGroup(boolean swFindNewGroup) {
		this.swFindNewGroup = swFindNewGroup;
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
	public AppConfig getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	public ScheduledExecutorService getExecutorThMain() {
		return executorThMain;
	}
	public void setExecutorThMain(ScheduledExecutorService executorThMain) {
		this.executorThMain = executorThMain;
	}
	public Map<String, String> getMapAssignedService() {
		return mapAssignedService;
	}
	public void setMapAssignedService(Map<String, String> mapAssignedService) {
		this.mapAssignedService = mapAssignedService;
	}
	public Map<String, Group> getMapGroupParam() {
		return mapGroupParam;
	}
	public void setMapGroupParam(Map<String, Group> mapGroupParam) {
		this.mapGroupParam = mapGroupParam;
	}
	public long getMessageID() {
		return messageID;
	}
	public void setMessageID(long messageID) {
		this.messageID = messageID;
	}
	public long getLogID() {
		return logID;
	}
	public void setLogID(long logID) {
		this.logID = logID;
	}
	public LinkedList<LogMessage> getLinkedLog() {
		return linkedLog;
	}
	public void setLinkedLog(LinkedList<LogMessage> linkedLog) {
		this.linkedLog = linkedLog;
	}
	public Map<String, ProcControl> getMapProcHistory() {
		return mapProcHistory;
	}
	public void setMapProcHistory(Map<String, ProcControl> mapProcHistory) {
		this.mapProcHistory = mapProcHistory;
	}
}
