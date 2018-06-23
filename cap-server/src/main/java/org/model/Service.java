package org.model;

import java.util.HashMap;
import java.util.Map;

public class Service {
	
	/**
	 Esta clase Service conversa los valores de parametros de un servicio.
	 Se asocia a un Map Global llamadao mapService cuyo key es el srvID
	 
	 El Object srvTypeProc se usa solo para recibir información de Metadata para posteriormente actualizar
	 esta información con el mapTypeProc y mapCli en cada Service
	 
	 */
	
	private String srvID;
	private String srvDesc;
	private int enable;
	private Object srvTypeProc;
	private int orderBalance;
	private int pctBalance;
	private int txpMain;
	private String fecStatus;   //este dato es actualizado por el cap-client
	
	private Map<String, TypeProc> mapTypeProc = new HashMap<>();
	private Map<String, String> mapCli = new HashMap<>();

	//Getter and Setter
	
	public String getSrvID() {
		return srvID;
	}
	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}
	public String getSrvDesc() {
		return srvDesc;
	}
	public void setSrvDesc(String srvDesc) {
		this.srvDesc = srvDesc;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public Object getSrvTypeProc() {
		return srvTypeProc;
	}
	public void setSrvTypeProc(Object srvTypeProc) {
		this.srvTypeProc = srvTypeProc;
	}
	public int getOrderBalance() {
		return orderBalance;
	}
	public void setOrderBalance(int orderBalance) {
		this.orderBalance = orderBalance;
	}
	public int getPctBalance() {
		return pctBalance;
	}
	public void setPctBalance(int pctBalance) {
		this.pctBalance = pctBalance;
	}
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public String getFecStatus() {
		return fecStatus;
	}
	public void setFecStatus(String fecStatus) {
		this.fecStatus = fecStatus;
	}
	public Map<String, TypeProc> getMapTypeProc() {
		return mapTypeProc;
	}
	public void setMapTypeProc(Map<String, TypeProc> mapTypeProc) {
		this.mapTypeProc = mapTypeProc;
	}
	public Map<String, String> getMapCli() {
		return mapCli;
	}
	public void setMapCli(Map<String, String> mapCli) {
		this.mapCli = mapCli;
	}

}
