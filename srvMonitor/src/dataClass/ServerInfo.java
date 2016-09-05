/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;
/**
 *
 * @author andresbenitez
 */
public class ServerInfo {
    String srvID;
    String srvDesc;
    int srvEnable;
    int txpMain;
    int txpKeep;
    int txpSocket;
    int txpAgendas;
    int txpETL;
    int srvPort;
    int ageShowHour;
    int ageGapMinute;
    String authKey;
    String driver;
    String connString;
    String dbType;
    String dbOraHost;
    int dbOraPort;
    String dbOraUser;
    String dbOraPass;
    String dbOraDBNAme;
    String dbSqlHost;
    int dbSqlPort;
    String dbSqlUser;
    String dbSqlPass;
    String dbSqlDBName;
    String dbSqlDBInstance;
    String dbmyHost;
    String dbmyUser;
    String dbmyPass;
    String dbmyDbName;
    int dbmyPort;
    
    public ServerInfo() {
    }
    
    public ServerInfo(String srvID, String srvDesc, int srvEnable) {
        this.srvID = srvID;
        this.srvDesc = srvDesc;
        this.srvEnable = srvEnable;
    }

    public String getDbmyHost() {
        return dbmyHost;
    }

    public void setDbmyHost(String dbmyHost) {
        this.dbmyHost = dbmyHost;
    }

    public String getDbmyUser() {
        return dbmyUser;
    }

    public void setDbmyUser(String dbmyUser) {
        this.dbmyUser = dbmyUser;
    }

    public String getDbmyPass() {
        return dbmyPass;
    }

    public void setDbmyPass(String dbmyPass) {
        this.dbmyPass = dbmyPass;
    }

    public String getDbmyDbName() {
        return dbmyDbName;
    }

    public void setDbmyDbName(String dbmyDbName) {
        this.dbmyDbName = dbmyDbName;
    }

    public int getDbmyPort() {
        return dbmyPort;
    }

    public void setDbmyPort(int dbmyPort) {
        this.dbmyPort = dbmyPort;
    }
    
    public int getAgeShowHour() {
        return ageShowHour;
    }

    public void setAgeShowHour(int ageShowHour) {
        this.ageShowHour = ageShowHour;
    }

    public int getAgeGapMinute() {
        return ageGapMinute;
    }

    public void setAgeGapMinute(int ageGapMinute) {
        this.ageGapMinute = ageGapMinute;
    }

    public int getTxpKeep() {
        return txpKeep;
    }

    public void setTxpKeep(int txpKeep) {
        this.txpKeep = txpKeep;
    }

    public int getTxpSocket() {
        return txpSocket;
    }

    public void setTxpSocket(int txpSocket) {
        this.txpSocket = txpSocket;
    }

    public int getTxpAgendas() {
        return txpAgendas;
    }

    public void setTxpAgendas(int txpAgendas) {
        this.txpAgendas = txpAgendas;
    }

    public int getTxpETL() {
        return txpETL;
    }

    public void setTxpETL(int txpETL) {
        this.txpETL = txpETL;
    }

    public int getTxpMain() {
        return txpMain;
    }

    public void setTxpMain(int txpMain) {
        this.txpMain = txpMain;
    }

    public int getSrvPort() {
        return srvPort;
    }

    public void setSrvPort(int srvPort) {
        this.srvPort = srvPort;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getConnString() {
        return connString;
    }

    public void setConnString(String connString) {
        this.connString = connString;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbOraHost() {
        return dbOraHost;
    }

    public void setDbOraHost(String dbOraHost) {
        this.dbOraHost = dbOraHost;
    }

    public int getDbOraPort() {
        return dbOraPort;
    }

    public void setDbOraPort(int dbOraPort) {
        this.dbOraPort = dbOraPort;
    }

    public String getDbOraUser() {
        return dbOraUser;
    }

    public void setDbOraUser(String dbOraUser) {
        this.dbOraUser = dbOraUser;
    }

    public String getDbOraPass() {
        return dbOraPass;
    }

    public void setDbOraPass(String dbOraPass) {
        this.dbOraPass = dbOraPass;
    }

    public String getDbOraDBNAme() {
        return dbOraDBNAme;
    }

    public void setDbOraDBNAme(String dbOraDBNAme) {
        this.dbOraDBNAme = dbOraDBNAme;
    }

    public String getDbSqlHost() {
        return dbSqlHost;
    }

    public void setDbSqlHost(String dbSqlHost) {
        this.dbSqlHost = dbSqlHost;
    }

    public int getDbSqlPort() {
        return dbSqlPort;
    }

    public void setDbSqlPort(int dbSqlPort) {
        this.dbSqlPort = dbSqlPort;
    }

    public String getDbSqlUser() {
        return dbSqlUser;
    }

    public void setDbSqlUser(String dbSqlUser) {
        this.dbSqlUser = dbSqlUser;
    }

    public String getDbSqlPass() {
        return dbSqlPass;
    }

    public void setDbSqlPass(String dbSqlPass) {
        this.dbSqlPass = dbSqlPass;
    }

    public String getDbSqlDBName() {
        return dbSqlDBName;
    }

    public void setDbSqlDBName(String dbSqlDBName) {
        this.dbSqlDBName = dbSqlDBName;
    }

    public String getDbSqlDBInstance() {
        return dbSqlDBInstance;
    }

    public void setDbSqlDBInstance(String dbSqlDBInstance) {
        this.dbSqlDBInstance = dbSqlDBInstance;
    }

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

    public int getSrvEnable() {
        return srvEnable;
    }

    public void setSrvEnable(int srvEnable) {
        this.srvEnable = srvEnable;
    }
}
