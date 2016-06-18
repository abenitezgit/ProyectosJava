/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

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
    
    //Parametros globales del servicio
    //
    private String srvName;          //Identificador unico del Servicio
    private String txpMain;          //Tiempo de ciclo del timerTask principal
    private String srvPort;          //Puerto del Socket Server     
    private String srvStart;         //Fecha Inicio del Servicio
    private String srvHost;
    private boolean srvGetTypeProc;     //Indica si recibio o no los parametros de configuracion de servicios
    private boolean srvActive;       //indice si Servicio se encuentra activo o no
    private boolean srvLoadParam;
    private String monPort;
    private String monPortBack;
    private String authKey;
    private String srvMonHost;

    
    //Variables Operacionales
    List<String> activeTypeProc = new ArrayList<>();
    
    //Parametros para recibir assigned Process
    //format json:
    //{"typeProc":"ETL","priority":"1","maxThread":"10"}
    List<JSONObject> assignedTypeProc = new ArrayList<>();
    
    //Lista para dejar historial de ejecuciones de los procesos
    //mientras está en memoria
    List<String> executedTypeProc = new ArrayList<>();
    
    //Lista para almacenar el pool de ejeciciones a realizar
    //
    List<JSONObject> poolProcess = new ArrayList<>();
       
    //Parametros de Control Online
    //
    private int numProcExec;         //Numero de procesos en ejecucion
    private int numProcMax;          //Maximo numero permitido de procesos simultaneos
    private int numTotalExec;        //Total de procesos ejecutados desde el startup
    
    //Metodos de Acceso a los Datos
    //

    public List<String> getExecutedTypeProc() {
        return executedTypeProc;
    }

    public void setExecutedTypeProc(List<String> executedTypeProc) {
        this.executedTypeProc = executedTypeProc;
    }

    public List<JSONObject> getAssignedTypeProc() {
        return assignedTypeProc;
    }

    public void setAssignedTypeProc(List<JSONObject> assignedTypeProc) {
        this.assignedTypeProc = assignedTypeProc;
    }

    public boolean isSrvGetTypeProc() {
        return srvGetTypeProc;
    }

    public void setSrvGetTypeProc(boolean srvGetTypeProc) {
        this.srvGetTypeProc = srvGetTypeProc;
    }

    public String getSrvMonHost() {
        return srvMonHost;
    }

    public void setSrvMonHost(String srvMonHost) {
        this.srvMonHost = srvMonHost;
    }

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

    public String getSrvHost() {
        return srvHost;
    }

    public void setSrvHost(String srvHost) {
        this.srvHost = srvHost;
    }

    public boolean isSrvActive() {
        return srvActive;
    }

    public void setSrvActive(boolean srvActive) {
        this.srvActive = srvActive;
    }

    public boolean isSrvLoadParam() {
        return srvLoadParam;
    }

    public void setSrvLoadParam(boolean srvLoadParam) {
        this.srvLoadParam = srvLoadParam;
    }

    public String getMonPort() {
        return monPort;
    }

    public void setMonPort(String monPort) {
        this.monPort = monPort;
    }

    public String getMonPortBack() {
        return monPortBack;
    }

    public void setMonPortBack(String monPortBack) {
        this.monPortBack = monPortBack;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public List<String> getActiveTypeProc() {
        return activeTypeProc;
    }

    public void setActiveTypeProc(List<String> activeTypeProc) {
        this.activeTypeProc = activeTypeProc;
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
    
    
    public globalAreaData() {
            Properties fileConf = new Properties();
            
            try {

                //Parametros del File Properties
                //
                fileConf.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvServer/src/utilities/srvServer.properties"));

                srvName = fileConf.getProperty("srvName");
                txpMain = fileConf.getProperty("txpMain");
                srvPort = fileConf.getProperty("srvPort");
                monPort = fileConf.getProperty("monPort");
                monPortBack = fileConf.getProperty("monPortBack");
                numProcMax = Integer.valueOf(fileConf.getProperty("numProcMax"));
                srvHost = fileConf.getProperty("srvHost");
                authKey = fileConf.getProperty("authKey");
                srvMonHost = fileConf.getProperty("srvMonHost");
                
                
                //Setea Parametros iniciales
                //
                numProcExec = 0;
                numTotalExec = 0;
                
                srvGetTypeProc = false;
                srvActive = true;

                
                //Extrae Fecha de Hoy
                //
                Date today;
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(formatter.getTimeZone());
                today = new Date();
                srvStart = formatter.format(today);                
                
                srvLoadParam= true;
                
            } catch (IOException | NumberFormatException e) {
                srvLoadParam = false;
                System.out.println("GA Error: "+e.getMessage());
            }
    }
}
