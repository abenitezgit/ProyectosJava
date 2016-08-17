/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.Agenda;
import dataClass.ETL;
import dataClass.Grupo;
import dataClass.Interval;
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
import java.util.stream.Collectors;

/**
 *
 * @author andresbenitez
 */
public class globalAreaData {    
    
    //Referencia Data Class
    private ETL etl = new ETL();
    private Agenda agenda = new Agenda();
    private ServerInfo serverInfo = new ServerInfo();
    private ServerStatus serverStatus = new ServerStatus();
    private ServiceStatus serviceStatus = new ServiceStatus();
    private List<ServiceStatus> lstServiceStatus = new ArrayList<>();
    private List<Agenda> lstShowAgendas = new ArrayList<>();
    private List<Agenda> lstActiveAgendas = new ArrayList<>();
    private List<Grupo> lstActiveGrupos = new ArrayList<>();
    private List<ETL> lstETLConf = new ArrayList<>();
    private List<Interval> lstInterval = new ArrayList<>();

    //Declarion de Metodos de GET / SET

    public ETL getEtl() {
        return etl;
    }

    public void setEtl(ETL etl) {
        this.etl = etl;
    }

    public List<Interval> getLstInterval() {
        return lstInterval;
    }

    public void setLstInterval(List<Interval> lstInterval) {
        this.lstInterval = lstInterval;
    }

    public List<ETL> getLstETLConf() {
        return lstETLConf;
    }

    public void setLstETLConf(List<ETL> lstETLConf) {
        this.lstETLConf = lstETLConf;
    }
    
    public synchronized void updateLstInterval(Interval interval) {
    
        List<Interval> lstTemp1 = this.lstInterval.stream().filter(p -> p.getETLID().equals(interval.getETLID())).collect(Collectors.toList());
        List<Interval> lstTemp2 = lstTemp1.stream().filter(p -> p.getIntervalID().equals(interval.getIntervalID())).collect(Collectors.toList());
        
        int numIntervals = lstTemp2.size();
        
        if (numIntervals==0) {
            lstInterval.add(interval);
        }
    }
    
    public synchronized void updateLstEtlConf(ETL etl) {
        
        int numItems = lstETLConf.size();
        boolean isFound = false;
        
        for (int i=0; i<numItems; i++) {
            if (lstETLConf.get(i).getETLID().equals(etl.getETLID())) {
                lstETLConf.set(i, etl);
                isFound = true;
            }
        }
        
        if (!isFound) {
            lstETLConf.add(etl);
        }
    }
    
    public synchronized void updateLstActiveAgendas(Agenda agenda) {
        
        List<Agenda> lstTempAgendas = this.lstActiveAgendas.stream().filter(p -> p.getAgeID().equals(agenda.getAgeID())).collect(Collectors.toList());
        
        int numTempAgendas = lstTempAgendas.size();
        boolean isSecFound = false;
        
        for (int i=0; i<numTempAgendas; i++) {
            if (lstTempAgendas.get(i).getNumSecExec()==agenda.getNumSecExec()) {
                isSecFound=true;
            }
        }
        
        if (!isSecFound) {
            this.lstActiveAgendas.add(agenda);
        }
    }

    public synchronized void updateLstActiveGrupos(Grupo grupo) {
        
        List<Grupo> lstTempGrupos = this.lstActiveGrupos.stream().filter(p -> p.getGrpID().equals(grupo.getGrpID())).collect(Collectors.toList());
        
        int numTempGrupos = lstTempGrupos.size();
        boolean isSecFound = false;
        
        for (int i=0; i<numTempGrupos; i++) {
            if (lstTempGrupos.get(i).getNumSecExec()==grupo.getNumSecExec()) {
                isSecFound = true;
            }
        }
        
        if (!isSecFound) {
            this.lstActiveGrupos.add(grupo);
        }
    }

    public List<Grupo> getLstActiveGrupos() {
        return lstActiveGrupos;
    }

    public void setLstActiveGrupos(List<Grupo> lstActiveGrupos) {
        this.lstActiveGrupos = lstActiveGrupos;
    }
    
    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public List<Agenda> getLstShowAgendas() {
        return lstShowAgendas;
    }

    public void setLstShowAgendas(List<Agenda> lstShowAgendas) {
        this.lstShowAgendas = lstShowAgendas;
    }

    public List<Agenda> getLstActiveAgendas() {
        return lstActiveAgendas;
    }

    public void setLstActiveAgendas(List<Agenda> lstActiveAgendas) {
        this.lstActiveAgendas = lstActiveAgendas;
    }

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
            serverStatus.setIsValMetadataConnect(false);
            serverStatus.setIsGetAgendaActive(false);
            serverStatus.setIsThreadETLActive(false);

            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.println(formatter.getTimeZone());
            today = new Date();
            
            serverStatus.setSrvStartTime(formatter.format(today));
            serverStatus.setSrvLoadParam(true);
            
            
        } catch (IOException | NumberFormatException e) {
            serverStatus.setSrvLoadParam(false);
            System.out.println("Error: "+e.getMessage());
        }
    }
    
}
