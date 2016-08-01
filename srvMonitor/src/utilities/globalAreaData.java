/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.ServerStatus;
import dataClass.ServerInfo;
import dataClass.ServiceStatus;
import java.io.FileInputStream;
import java.io.IOException;
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
        
    //Referencia Data Class
    private ServerInfo serverInfo = new ServerInfo();
    private ServerStatus serverStatus = new ServerStatus();
    private ServiceStatus serviceStatus = new ServiceStatus();
    private List<ServiceStatus> lstServiceStatus = new ArrayList<>();

       
    /*
    Listas para Agendas24HH y AgendasPendientes
    */
    private List<JSONObject> lstShowAgendas = new ArrayList<>();
    
    private List<JSONObject> lstActiveAgendas = new ArrayList<>();
    

    //Declarion de Metodos de GET / SET

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public List<ServiceStatus> getLstServiceStatus() {
        return lstServiceStatus;
    }

    public void setLstServiceStatus(List<ServiceStatus> lstServiceStatus) {
        this.lstServiceStatus = lstServiceStatus;
    }
    
    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
    
    public List<JSONObject> getLstShowAgendas() {
        return lstShowAgendas;
    }

    public void setLstShowAgendas(List<JSONObject> lstShowAgendas) {
        this.lstShowAgendas = lstShowAgendas;
    }

    public List<JSONObject> getLstActiveAgendas() {
        return lstActiveAgendas;
    }

    public void setLstActiveAgendas(List<JSONObject> lstActiveAgendas) {
        this.lstActiveAgendas = lstActiveAgendas;
    }
    
    public void updateLstServiceStatus(ServiceStatus serviceStatus) {
        int numItems = lstServiceStatus.size();
        boolean itemFound = false;
        ServiceStatus myServiceStatus;
        
        for (int i=0; i<numItems; i++) {
            if (lstServiceStatus.get(i).getSrvID().equals(serviceStatus.getSrvID())) {
               myServiceStatus = lstServiceStatus.get(i);
               myServiceStatus.setSrvEnable(serviceStatus.getSrvEnable());
               myServiceStatus.setLstAssignedTypeProc(serviceStatus.getLstAssignedTypeProc());
               lstServiceStatus.set(i, myServiceStatus);
               itemFound = true;
            }
        }
        
        if (!itemFound) {
            lstServiceStatus.add(serviceStatus);
        }
    }

    public globalAreaData() {
        Properties fileConf = new Properties();

        try {

            //Parametros del File Properties
            //
            fileConf.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvMonitor/src/utilities/srvMonitor.properties"));

            serverInfo.setSrvID(fileConf.getProperty("srvID"));
            serverInfo.setTxpMain(Integer.valueOf(fileConf.getProperty("txpMain")));
            serverInfo.setSrvPort(Integer.valueOf(fileConf.getProperty("srvPort")));
            serverInfo.setAuthKey(fileConf.getProperty("authKey"));
            serverInfo.setDriver(fileConf.getProperty("driver"));
            serverInfo.setConnString(fileConf.getProperty("ConnString"));
            serverInfo.setDbType(fileConf.getProperty("dbType"));
                        
            if (serverInfo.getDbType().equals("ORA")) {
                //Recupera Paramteros ORA
                serverInfo.setDbOraHost(fileConf.getProperty("dbORAHost"));
                serverInfo.setDbOraPort(Integer.valueOf(fileConf.getProperty("dbORAPort")));
                serverInfo.setDbOraUser(fileConf.getProperty("dbORAUser"));
                serverInfo.setDbOraPass(fileConf.getProperty("dbORAPass"));
                serverInfo.setDbOraDBNAme(fileConf.getProperty("dbORADbName"));
            }
            
            if (serverInfo.getDbType().equals("SQL")) {
                //Recupera Paramteros ORA
                serverInfo.setDbSqlHost(fileConf.getProperty("dbSQLHost"));
                serverInfo.setDbSqlPort(Integer.valueOf(fileConf.getProperty("dbSQLPort")));
                serverInfo.setDbSqlUser(fileConf.getProperty("dbSQLUser"));
                serverInfo.setDbSqlPass(fileConf.getProperty("dbSQLPass"));
                serverInfo.setDbSqlDBName(fileConf.getProperty("dbSQLDbName"));
                serverInfo.setDbSqlDBInstance(fileConf.getProperty("dbSQLInstance"));
            }

            serverStatus.setSrvActive(true);
            serverStatus.setIsMetadataConnect(false);
            serverStatus.setIsGetAgendaActive(false);

            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(formatter.getTimeZone());
            today = new Date();
            
            serverStatus.setSrvStartTime(formatter.format(today));
            serverStatus.setSrvLoadParam(true);
            
        } catch (IOException | NumberFormatException e) {
            serverStatus.setSrvLoadParam(false);
            System.out.println("Error: "+e.getMessage());
        }
    }
    
}
