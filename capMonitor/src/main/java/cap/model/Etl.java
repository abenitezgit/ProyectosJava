/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresbenitez
 */
public class Etl {
    String etlID;
    String etlDesc;
    int etlEnable;
    String cliDesc;
    String fieldKey;
    String fieldType;
    int timeGap;
    int timeGen;
    int timePeriod;
    String unitMeasure;
    int whereActive;
    String queryBody;
    String sTbName;
    String dTbName;
    String sIp;
    String sDbDesc;
    String sDbName;
    String sDbType;
    String sDbPort;
    String sDbInstance;
    String sDbConf;
    String sDbJDBC;
    String sUserName;
    String sUserPass;
    String sUserType;
    String dIp;
    String dDbDesc;
    String dDbName;
    String dDbType;
    String dDbPort;
    String dDbInstance;
    String dDbConf;
    String dDbJDBC;
    String dUserName;
    String dUserPass;
    String dUserType;
    String numSecExec;
    
    List<EtlMatch> lstEtlMatch = new ArrayList<>();

    //Getter and Setter
    
	public String getEtlID() {
		return etlID;
	}

	public void setEtlID(String etlID) {
		this.etlID = etlID;
	}

	public String getEtlDesc() {
		return etlDesc;
	}

	public void setEtlDesc(String etlDesc) {
		this.etlDesc = etlDesc;
	}

	public int getEtlEnable() {
		return etlEnable;
	}

	public void setEtlEnable(int etlEnable) {
		this.etlEnable = etlEnable;
	}

	public String getCliDesc() {
		return cliDesc;
	}

	public void setCliDesc(String cliDesc) {
		this.cliDesc = cliDesc;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public int getTimeGap() {
		return timeGap;
	}

	public void setTimeGap(int timeGap) {
		this.timeGap = timeGap;
	}

	public int getTimeGen() {
		return timeGen;
	}

	public void setTimeGen(int timeGen) {
		this.timeGen = timeGen;
	}

	public int getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getUnitMeasure() {
		return unitMeasure;
	}

	public void setUnitMeasure(String unitMeasure) {
		this.unitMeasure = unitMeasure;
	}

	public int getWhereActive() {
		return whereActive;
	}

	public void setWhereActive(int whereActive) {
		this.whereActive = whereActive;
	}

	public String getQueryBody() {
		return queryBody;
	}

	public void setQueryBody(String queryBody) {
		this.queryBody = queryBody;
	}

	public String getsTbName() {
		return sTbName;
	}

	public void setsTbName(String sTbName) {
		this.sTbName = sTbName;
	}

	public String getdTbName() {
		return dTbName;
	}

	public void setdTbName(String dTbName) {
		this.dTbName = dTbName;
	}

	public String getsIp() {
		return sIp;
	}

	public void setsIp(String sIp) {
		this.sIp = sIp;
	}

	public String getsDbDesc() {
		return sDbDesc;
	}

	public void setsDbDesc(String sDbDesc) {
		this.sDbDesc = sDbDesc;
	}

	public String getsDbName() {
		return sDbName;
	}

	public void setsDbName(String sDbName) {
		this.sDbName = sDbName;
	}

	public String getsDbType() {
		return sDbType;
	}

	public void setsDbType(String sDbType) {
		this.sDbType = sDbType;
	}

	public String getsDbPort() {
		return sDbPort;
	}

	public void setsDbPort(String sDbPort) {
		this.sDbPort = sDbPort;
	}

	public String getsDbInstance() {
		return sDbInstance;
	}

	public void setsDbInstance(String sDbInstance) {
		this.sDbInstance = sDbInstance;
	}

	public String getsDbConf() {
		return sDbConf;
	}

	public void setsDbConf(String sDbConf) {
		this.sDbConf = sDbConf;
	}

	public String getsDbJDBC() {
		return sDbJDBC;
	}

	public void setsDbJDBC(String sDbJDBC) {
		this.sDbJDBC = sDbJDBC;
	}

	public String getsUserName() {
		return sUserName;
	}

	public void setsUserName(String sUserName) {
		this.sUserName = sUserName;
	}

	public String getsUserPass() {
		return sUserPass;
	}

	public void setsUserPass(String sUserPass) {
		this.sUserPass = sUserPass;
	}

	public String getsUserType() {
		return sUserType;
	}

	public void setsUserType(String sUserType) {
		this.sUserType = sUserType;
	}

	public String getdIp() {
		return dIp;
	}

	public void setdIp(String dIp) {
		this.dIp = dIp;
	}

	public String getdDbDesc() {
		return dDbDesc;
	}

	public void setdDbDesc(String dDbDesc) {
		this.dDbDesc = dDbDesc;
	}

	public String getdDbName() {
		return dDbName;
	}

	public void setdDbName(String dDbName) {
		this.dDbName = dDbName;
	}

	public String getdDbType() {
		return dDbType;
	}

	public void setdDbType(String dDbType) {
		this.dDbType = dDbType;
	}

	public String getdDbPort() {
		return dDbPort;
	}

	public void setdDbPort(String dDbPort) {
		this.dDbPort = dDbPort;
	}

	public String getdDbInstance() {
		return dDbInstance;
	}

	public void setdDbInstance(String dDbInstance) {
		this.dDbInstance = dDbInstance;
	}

	public String getdDbConf() {
		return dDbConf;
	}

	public void setdDbConf(String dDbConf) {
		this.dDbConf = dDbConf;
	}

	public String getdDbJDBC() {
		return dDbJDBC;
	}

	public void setdDbJDBC(String dDbJDBC) {
		this.dDbJDBC = dDbJDBC;
	}

	public String getdUserName() {
		return dUserName;
	}

	public void setdUserName(String dUserName) {
		this.dUserName = dUserName;
	}

	public String getdUserPass() {
		return dUserPass;
	}

	public void setdUserPass(String dUserPass) {
		this.dUserPass = dUserPass;
	}

	public String getdUserType() {
		return dUserType;
	}

	public void setdUserType(String dUserType) {
		this.dUserType = dUserType;
	}

	public String getNumSecExec() {
		return numSecExec;
	}

	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}

	public List<EtlMatch> getLstEtlMatch() {
		return lstEtlMatch;
	}

	public void setLstEtlMatch(List<EtlMatch> lstEtlMatch) {
		this.lstEtlMatch = lstEtlMatch;
	}
    
    
}
