/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class globalAreaData {
    
    //Parametros globales del servicio
    //
    private String srvName;          //Identificador unico del Servicio
    private String txpMain;          //Tiempo de ciclo del timerTask principal
    private String srvPort;          //Puerto del Socket Server     
    private String srvStart;         //Fecha Inicio del Servicio
    private boolean srvGetParam;     //Indica si recibio o no los parametros de configuracion de servicios
    private boolean srvActive;       //indice si Servicio se encuentra activo o no
    private String monPort;
    private boolean srvLoadParam;
    private String authKey;
    private String Driver;
    private String ConnString;

    
    //Variables Operacionales
    //Parametros de los Tipos de Procesos que puede ejecutar el servicio
    //formato json:
    //{"typeProc":"ETL","procID":"ETL00001","startTime":"2016-06-16 10:00:00","status":"Finished",
    // "endTime":"2016-06-10 20:00:00"}
    //private List<JSONObject> activeTypeProc = new ArrayList<>();
    
    
    //Parametros de los procesos asignados a servicios registrados
    //formato json:
    //{"srvName":"srv00001","srvActive":1,"procAssigned":[{"":""}]}
    private List<JSONObject> assignedServiceTypeProc = new ArrayList<>();
    
    //Parametros de los Servicios que se registran
    //formato json:
    //{"srvName":"srv00000","srvStart":"2016-06-16 10:00:00","numTotalExec":100,
    //  "srvPort":"9090","numProcMax":"30","numProcExec":0,
    //  "procActive":[],"procAssigned":[] }
    private List<JSONObject> serviceStatus = new ArrayList<>();
    
       
    //Parametros de Control Online
    //
    private int numProcExec;         //Numero de procesos en ejecucion
    private int numProcMax;          //Maximo numero permitido de procesos simultaneos
    private int numTotalExec;        //Total de procesos ejecutados desde el startup
    private int numSrvActives;       //Numero de servicios (servers) Activos
    
    
    //Parametros de Acceso a Metada
    //
    private String dbType;
    private boolean isMetadataConnect;
    private Connection metadataConnection;
    
    //Parametros ORA Metadata
    private String dbORAHost;
    private String dbORAPort;
    private String dbORAUser;
    private String dbORAPass;
    private String dbORADbName;
    
    //Parametros SQL Metadata
    private String dbSQLHost;
    private String dbSQLPort;
    private String dbSQLUser;
    private String dbSQLPass;
    private String dbSQLInstance;
    private String dbSQLDbName;
    


    //Declarion de Metodos de GET / SET

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public String getTxpMain() {
        return txpMain;
    }

    public void setTxpMain(String txpMain) {
        this.txpMain = txpMain;
    }

    public String getSrvPort() {
        return srvPort;
    }

    public void setSrvPort(String srvPort) {
        this.srvPort = srvPort;
    }

    public String getSrvStart() {
        return srvStart;
    }

    public void setSrvStart(String srvStart) {
        this.srvStart = srvStart;
    }

    public boolean isSrvGetParam() {
        return srvGetParam;
    }

    public void setSrvGetParam(boolean srvGetParam) {
        this.srvGetParam = srvGetParam;
    }

    public boolean isSrvActive() {
        return srvActive;
    }

    public void setSrvActive(boolean srvActive) {
        this.srvActive = srvActive;
    }

    public String getMonPort() {
        return monPort;
    }

    public void setMonPort(String monPort) {
        this.monPort = monPort;
    }

    public boolean isSrvLoadParam() {
        return srvLoadParam;
    }

    public void setSrvLoadParam(boolean srvLoadParam) {
        this.srvLoadParam = srvLoadParam;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String Driver) {
        this.Driver = Driver;
    }

    public String getConnString() {
        return ConnString;
    }

    public void setConnString(String ConnString) {
        this.ConnString = ConnString;
    }

    public List<JSONObject> getAssignedServiceTypeProc() {
        return assignedServiceTypeProc;
    }

    public void setAssignedServiceTypeProc(List<JSONObject> assignedServiceTypeProc) {
        this.assignedServiceTypeProc = assignedServiceTypeProc;
    }

    public List<JSONObject> getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(List<JSONObject> serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    
    public int getNumProcExec() {
        return numProcExec;
    }

    public void setNumProcExec(int numProcExec) {
        this.numProcExec = numProcExec;
    }

    public int getNumProcMax() {
        return numProcMax;
    }

    public void setNumProcMax(int numProcMax) {
        this.numProcMax = numProcMax;
    }

    public int getNumTotalExec() {
        return numTotalExec;
    }

    public void setNumTotalExec(int numTotalExec) {
        this.numTotalExec = numTotalExec;
    }

    public int getNumSrvActives() {
        return numSrvActives;
    }

    public void setNumSrvActives(int numSrvActives) {
        this.numSrvActives = numSrvActives;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public boolean isIsMetadataConnect() {
        return isMetadataConnect;
    }

    public void setIsMetadataConnect(boolean isMetadataConnect) {
        this.isMetadataConnect = isMetadataConnect;
    }

    public Connection getMetadataConnection() {
        return metadataConnection;
    }

    public void setMetadataConnection(Connection metadataConnection) {
        this.metadataConnection = metadataConnection;
    }

    public String getDbORAHost() {
        return dbORAHost;
    }

    public void setDbORAHost(String dbORAHost) {
        this.dbORAHost = dbORAHost;
    }

    public String getDbORAPort() {
        return dbORAPort;
    }

    public void setDbORAPort(String dbORAPort) {
        this.dbORAPort = dbORAPort;
    }

    public String getDbORAUser() {
        return dbORAUser;
    }

    public void setDbORAUser(String dbORAUser) {
        this.dbORAUser = dbORAUser;
    }

    public String getDbORAPass() {
        return dbORAPass;
    }

    public void setDbORAPass(String dbORAPass) {
        this.dbORAPass = dbORAPass;
    }

    public String getDbORADbName() {
        return dbORADbName;
    }

    public void setDbORADbName(String dbORADbName) {
        this.dbORADbName = dbORADbName;
    }

    public String getDbSQLHost() {
        return dbSQLHost;
    }

    public void setDbSQLHost(String dbSQLHost) {
        this.dbSQLHost = dbSQLHost;
    }

    public String getDbSQLPort() {
        return dbSQLPort;
    }

    public void setDbSQLPort(String dbSQLPort) {
        this.dbSQLPort = dbSQLPort;
    }

    public String getDbSQLUser() {
        return dbSQLUser;
    }

    public void setDbSQLUser(String dbSQLUser) {
        this.dbSQLUser = dbSQLUser;
    }

    public String getDbSQLPass() {
        return dbSQLPass;
    }

    public void setDbSQLPass(String dbSQLPass) {
        this.dbSQLPass = dbSQLPass;
    }

    public String getDbSQLInstance() {
        return dbSQLInstance;
    }

    public void setDbSQLInstance(String dbSQLInstance) {
        this.dbSQLInstance = dbSQLInstance;
    }

    public String getDbSQLDbName() {
        return dbSQLDbName;
    }

    public void setDbSQLDbName(String dbSQLDbName) {
        this.dbSQLDbName = dbSQLDbName;
    }
    
    
    //Procedimientos y/o metodos Utilizarios
    //
        public int addItemServiceStatus(JSONObject param) {
        try {
            serviceStatus.add(param);
            return 0;
        } catch (Exception e) {
            System.out.println("add: "+e.getMessage());
            return 1;
        }
    }
    
    public int deleteItemServiceStatus(String key) {
        try {
            int numItems = serviceStatus.size();
            for (int i=0; i<numItems; i++) {
                if (serviceStatus.get(i).get("srvName").toString().equals(key)) {
                    serviceStatus.remove(i);
                }
            }
            return 0;
        } catch (Exception e) {
            System.out.println("del: "+e.getMessage());
            return 1;
        }
    }

    public globalAreaData() {
        Properties fileConf = new Properties();

        try {

            //Parametros del File Properties
            //
            fileConf.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvMonitor/src/utilities/srvMonitor.properties"));

            srvName = fileConf.getProperty("srvName");
            txpMain = fileConf.getProperty("txpMain");
            srvPort = fileConf.getProperty("srvPort");
            monPort = fileConf.getProperty("monPort");
            numProcMax = Integer.valueOf(fileConf.getProperty("numProcMax"));
            authKey = fileConf.getProperty("authKey");
            Driver = fileConf.getProperty("Driver");
            ConnString = fileConf.getProperty("ConnString");
            
            //Recupera Parametros de la Metadata
            dbType = fileConf.getProperty("dbType");
            
            if (dbType.equals("ORA")) {
                //Recupera Paramteros ORA
                dbORAHost = fileConf.getProperty("dbORAHost");
                dbORAPort = fileConf.getProperty("dbORAPort");
                dbORAUser = fileConf.getProperty("dbORAUser");
                dbORAPass = fileConf.getProperty("dbORAPass");
                dbORADbName = fileConf.getProperty("dbORADbName");
            }
            
            if (dbType.equals("SQL")) {
                //Recupera Paramteros ORA
                dbSQLHost = fileConf.getProperty("dbSQLHost");
                dbSQLPort = fileConf.getProperty("dbSQLPort");
                dbSQLUser = fileConf.getProperty("dbSQLUser");
                dbSQLPass = fileConf.getProperty("dbSQLPass");
                dbSQLDbName = fileConf.getProperty("dbSQLDbName");
                dbSQLInstance = fileConf.getProperty("dbSQLInstance");
            }

            //Setea Parametros iniciales
            //
            numProcExec = 0;
            numTotalExec = 0;

            srvGetParam = false;
            srvActive = true;
            isMetadataConnect = false;

            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(formatter.getTimeZone());
            today = new Date();
            srvStart = formatter.format(today);                
            srvLoadParam = true;
            
        } catch (IOException | NumberFormatException e) {
            srvLoadParam = false;
            System.out.println("Error: "+e.getMessage());
        }
    }
    
}
