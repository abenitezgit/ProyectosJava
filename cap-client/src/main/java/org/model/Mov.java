/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.model;

import java.util.ArrayList;
import java.util.List;

public class Mov {
    String movID;
    String movDesc;
    int enable;
    String cliID;
    String cliDesc;
    String sServerID;
    String sHostName;
    String sDomain;
    String sIP;
    String sServerDesc;
    String dServerID;
    String dHostName;
    String dDomain;
    String dIP;
    String dServerDesc;
    String sDbID;
    String sDbName;
    String sDbPort;
    String sDbType;
    String sDbInstance;
    String dDbID;
    String dDbName;
    String dDbPort;
    String dDbType;
    String dDbInstance;
    String sLoginID;
    String sLoginName;
    String sLoginPass;
    String dLoginID;
    String dLoginName;
    String dLoginPass;
    String sOwnerID;
    String sOwnerName;
    String sOwnerPass;
    String dOwnerID;
    String dOwnerName;
    String dOwnerPass;
    String sTbName;
    String dTbName;
    int sFieldUpdateActive;
    String sFieldUpdateName;
    String sFieldValueType;
    String sFieldValueRead;
    String sFieldValueUpdate;
    int whereActive;
    String whereBody;
    int appendable;
    int deleteWhereActive;
    String deleteWhereBody;
    int createDest;
    int maxRows;
    int maxRowsRange;
    int maxRowsError;
    int maxPctError;
    int rollbackError;
    int fecExtActive;
    int fecExtEpoch;
    String fecExtField;
    String fecExtIni;
    String fecExtFin;
    int fecExtIniIn;
    int fecExtFinIn;
    String sFieldUpdateKey;
    
    List<MovMatch> lstMovMatch = new ArrayList<>();

    //Getter and Setter
    //

    
	public String getMovID() {
		return movID;
	}

	public void setMovID(String movID) {
		this.movID = movID;
	}

	public String getMovDesc() {
		return movDesc;
	}

	public void setMovDesc(String movDesc) {
		this.movDesc = movDesc;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getCliID() {
		return cliID;
	}

	public void setCliID(String cliID) {
		this.cliID = cliID;
	}

	public String getCliDesc() {
		return cliDesc;
	}

	public void setCliDesc(String cliDesc) {
		this.cliDesc = cliDesc;
	}

	public String getsServerID() {
		return sServerID;
	}

	public void setsServerID(String sServerID) {
		this.sServerID = sServerID;
	}

	public String getsHostName() {
		return sHostName;
	}

	public void setsHostName(String sHostName) {
		this.sHostName = sHostName;
	}

	public String getsDomain() {
		return sDomain;
	}

	public void setsDomain(String sDomain) {
		this.sDomain = sDomain;
	}

	public String getsIP() {
		return sIP;
	}

	public void setsIP(String sIP) {
		this.sIP = sIP;
	}

	public String getsServerDesc() {
		return sServerDesc;
	}

	public void setsServerDesc(String sServerDesc) {
		this.sServerDesc = sServerDesc;
	}

	public String getdServerID() {
		return dServerID;
	}

	public void setdServerID(String dServerID) {
		this.dServerID = dServerID;
	}

	public String getdHostName() {
		return dHostName;
	}

	public void setdHostName(String dHostName) {
		this.dHostName = dHostName;
	}

	public String getdDomain() {
		return dDomain;
	}

	public void setdDomain(String dDomain) {
		this.dDomain = dDomain;
	}

	public String getdIP() {
		return dIP;
	}

	public void setdIP(String dIP) {
		this.dIP = dIP;
	}

	public String getdServerDesc() {
		return dServerDesc;
	}

	public void setdServerDesc(String dServerDesc) {
		this.dServerDesc = dServerDesc;
	}

	public String getsDbID() {
		return sDbID;
	}

	public void setsDbID(String sDbID) {
		this.sDbID = sDbID;
	}

	public String getsDbName() {
		return sDbName;
	}

	public void setsDbName(String sDbName) {
		this.sDbName = sDbName;
	}

	public String getsDbPort() {
		return sDbPort;
	}

	public void setsDbPort(String sDbPort) {
		this.sDbPort = sDbPort;
	}

	public String getsDbType() {
		return sDbType;
	}

	public void setsDbType(String sDbType) {
		this.sDbType = sDbType;
	}

	public String getsDbInstance() {
		return sDbInstance;
	}

	public void setsDbInstance(String sDbInstance) {
		this.sDbInstance = sDbInstance;
	}

	public String getdDbID() {
		return dDbID;
	}

	public void setdDbID(String dDbID) {
		this.dDbID = dDbID;
	}

	public String getdDbName() {
		return dDbName;
	}

	public void setdDbName(String dDbName) {
		this.dDbName = dDbName;
	}

	public String getdDbPort() {
		return dDbPort;
	}

	public void setdDbPort(String dDbPort) {
		this.dDbPort = dDbPort;
	}

	public String getdDbType() {
		return dDbType;
	}

	public void setdDbType(String dDbType) {
		this.dDbType = dDbType;
	}

	public String getdDbInstance() {
		return dDbInstance;
	}

	public void setdDbInstance(String dDbInstance) {
		this.dDbInstance = dDbInstance;
	}

	public String getsLoginID() {
		return sLoginID;
	}

	public void setsLoginID(String sLoginID) {
		this.sLoginID = sLoginID;
	}

	public String getsLoginName() {
		return sLoginName;
	}

	public void setsLoginName(String sLoginName) {
		this.sLoginName = sLoginName;
	}

	public String getsLoginPass() {
		return sLoginPass;
	}

	public void setsLoginPass(String sLoginPass) {
		this.sLoginPass = sLoginPass;
	}

	public String getdLoginID() {
		return dLoginID;
	}

	public void setdLoginID(String dLoginID) {
		this.dLoginID = dLoginID;
	}

	public String getdLoginName() {
		return dLoginName;
	}

	public void setdLoginName(String dLoginName) {
		this.dLoginName = dLoginName;
	}

	public String getdLoginPass() {
		return dLoginPass;
	}

	public void setdLoginPass(String dLoginPass) {
		this.dLoginPass = dLoginPass;
	}

	public String getsOwnerID() {
		return sOwnerID;
	}

	public void setsOwnerID(String sOwnerID) {
		this.sOwnerID = sOwnerID;
	}

	public String getsOwnerName() {
		return sOwnerName;
	}

	public void setsOwnerName(String sOwnerName) {
		this.sOwnerName = sOwnerName;
	}

	public String getsOwnerPass() {
		return sOwnerPass;
	}

	public void setsOwnerPass(String sOwnerPass) {
		this.sOwnerPass = sOwnerPass;
	}

	public String getdOwnerID() {
		return dOwnerID;
	}

	public void setdOwnerID(String dOwnerID) {
		this.dOwnerID = dOwnerID;
	}

	public String getdOwnerName() {
		return dOwnerName;
	}

	public void setdOwnerName(String dOwnerName) {
		this.dOwnerName = dOwnerName;
	}

	public String getdOwnerPass() {
		return dOwnerPass;
	}

	public void setdOwnerPass(String dOwnerPass) {
		this.dOwnerPass = dOwnerPass;
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

	public int getsFieldUpdateActive() {
		return sFieldUpdateActive;
	}

	public void setsFieldUpdateActive(int sFieldUpdateActive) {
		this.sFieldUpdateActive = sFieldUpdateActive;
	}

	public String getsFieldUpdateName() {
		return sFieldUpdateName;
	}

	public void setsFieldUpdateName(String sFieldUpdateName) {
		this.sFieldUpdateName = sFieldUpdateName;
	}

	public String getsFieldValueType() {
		return sFieldValueType;
	}

	public void setsFieldValueType(String sFieldValueType) {
		this.sFieldValueType = sFieldValueType;
	}

	public String getsFieldValueRead() {
		return sFieldValueRead;
	}

	public void setsFieldValueRead(String sFieldValueRead) {
		this.sFieldValueRead = sFieldValueRead;
	}

	public String getsFieldValueUpdate() {
		return sFieldValueUpdate;
	}

	public void setsFieldValueUpdate(String sFieldValueUpdate) {
		this.sFieldValueUpdate = sFieldValueUpdate;
	}

	public int getWhereActive() {
		return whereActive;
	}

	public void setWhereActive(int whereActive) {
		this.whereActive = whereActive;
	}

	public String getWhereBody() {
		return whereBody;
	}

	public void setWhereBody(String whereBody) {
		this.whereBody = whereBody;
	}

	public int getAppendable() {
		return appendable;
	}

	public void setAppendable(int appendable) {
		this.appendable = appendable;
	}

	public int getCreateDest() {
		return createDest;
	}

	public void setCreateDest(int createDest) {
		this.createDest = createDest;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public int getMaxRowsRange() {
		return maxRowsRange;
	}

	public void setMaxRowsRange(int maxRowsRange) {
		this.maxRowsRange = maxRowsRange;
	}

	public int getMaxRowsError() {
		return maxRowsError;
	}

	public void setMaxRowsError(int maxRowsError) {
		this.maxRowsError = maxRowsError;
	}

	public int getMaxPctError() {
		return maxPctError;
	}

	public void setMaxPctError(int maxPctError) {
		this.maxPctError = maxPctError;
	}

	public int getRollbackError() {
		return rollbackError;
	}

	public void setRollbackError(int rollbackError) {
		this.rollbackError = rollbackError;
	}

	public int getFecExtActive() {
		return fecExtActive;
	}

	public void setFecExtActive(int fecExtActive) {
		this.fecExtActive = fecExtActive;
	}

	public int getFecExtEpoch() {
		return fecExtEpoch;
	}

	public void setFecExtEpoch(int fecExtEpoch) {
		this.fecExtEpoch = fecExtEpoch;
	}

	public String getFecExtField() {
		return fecExtField;
	}

	public void setFecExtField(String fecExtField) {
		this.fecExtField = fecExtField;
	}

	public String getFecExtIni() {
		return fecExtIni;
	}

	public void setFecExtIni(String fecExtIni) {
		this.fecExtIni = fecExtIni;
	}

	public String getFecExtFin() {
		return fecExtFin;
	}

	public void setFecExtFin(String fecExtFin) {
		this.fecExtFin = fecExtFin;
	}

	public int getFecExtIniIn() {
		return fecExtIniIn;
	}

	public void setFecExtIniIn(int fecExtIniIn) {
		this.fecExtIniIn = fecExtIniIn;
	}

	public int getFecExtFinIn() {
		return fecExtFinIn;
	}

	public void setFecExtFinIn(int fecExtFinIn) {
		this.fecExtFinIn = fecExtFinIn;
	}

	public String getsFieldUpdateKey() {
		return sFieldUpdateKey;
	}

	public void setsFieldUpdateKey(String sFieldUpdateKey) {
		this.sFieldUpdateKey = sFieldUpdateKey;
	}

	public List<MovMatch> getLstMovMatch() {
		return lstMovMatch;
	}

	public void setLstMovMatch(List<MovMatch> lstMovMatch) {
		this.lstMovMatch = lstMovMatch;
	}

	public int getDeleteWhereActive() {
		return deleteWhereActive;
	}

	public void setDeleteWhereActive(int deleteWhereActive) {
		this.deleteWhereActive = deleteWhereActive;
	}

	public String getDeleteWhereBody() {
		return deleteWhereBody;
	}

	public void setDeleteWhereBody(String deleteWhereBody) {
		this.deleteWhereBody = deleteWhereBody;
	}

}
