package ecc.model;

import java.util.ArrayList;
import java.util.List;

public class DataRequest {
	String ani;
	String dnis;
	String fechaDesde; //Formato YYYYMMDDHHMISS
	String fechaHasta; //Formato YYYYMMDDHHMISS
	String tableName;
	String connid;
	String uniqueid;
	String agente;
	int limit;   //Limite de registros a retornar 0: unlimited
	List<String> lstSkill = new ArrayList<>();
	
	//Getter ans Setter
	
	public String getConnid() {
		return connid;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getAgente() {
		return agente;
	}
	public void setAgente(String agente) {
		this.agente = agente;
	}
	public void setConnid(String connid) {
		this.connid = connid;
	}
	public String getAni() {
		return ani;
	}
	public void setAni(String ani) {
		this.ani = ani;
	}
	public String getDnis() {
		return dnis;
	}
	public void setDnis(String dnis) {
		this.dnis = dnis;
	}
	public List<String> getLstSkill() {
		return lstSkill;
	}
	public void setLstSkill(List<String> lstSkill) {
		this.lstSkill = lstSkill;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
