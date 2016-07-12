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
    private String srvMonHostBack;
    private boolean isRegisterService;
    private boolean isActivePrimaryMonHost;
    private boolean isConnectMonHost;
    

    
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

    public boolean isIsConnectMonHost() {
        return isConnectMonHost;
    }

    public void setIsConnectMonHost(boolean isConnectMonHost) {
        this.isConnectMonHost = isConnectMonHost;
    }

    public String getSrvMonHostBack() {
        return srvMonHostBack;
    }

    public void setSrvMonHostBack(String srvMonHostBack) {
        this.srvMonHostBack = srvMonHostBack;
    }

    public boolean isIsActivePrimaryMonHost() {
        return isActivePrimaryMonHost;
    }

    public void setIsActivePrimaryMonHost(boolean isActivePrimaryMonHost) {
        this.isActivePrimaryMonHost = isActivePrimaryMonHost;
    }
    
    public boolean isIsRegisterService() {
        return isRegisterService;
    }

    public void setIsRegisterService(boolean isRegisterService) {
        this.isRegisterService = isRegisterService;
    }
    
    public List<JSONObject> getPoolProcess() {
        return poolProcess;
    }

    public void setPoolProcess(List<JSONObject> poolProcess) {
        this.poolProcess = poolProcess;
    }
    
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
    
    //Procimientos y/p Metodos uilitarios
    //
    public int updateRunningPoolProcess(JSONObject ds) {
        try {
            String procID;
            String typeProc;
            String status;
            int numItems;
            int numTypeProcLst;
            int myNumExec;
            int myNumProcExec;
            int myNumTotalExec;
            
            typeProc = ds.getString("typeProc");
            procID = ds.getString("procID");
            
            //System.out.println("typeProc: " + typeProc);
            //System.out.println("procID: " + procID);
            
            numItems = poolProcess.size();
            numTypeProcLst = assignedTypeProc.size();
            
            System.out.println("numItemsPool: " + numItems);
            System.out.println("numTypeProcLst: " + numTypeProcLst);
            
            if (numItems>0) {
                for (int i=0; i<numItems; i++) {
                    System.out.println("poolList 1: " + poolProcess.toString());
                    if (poolProcess.get(i).getString("procID").equals(procID)) {
                        poolProcess.get(i).put("status","Running");
                        poolProcess.get(i).put("startDate", "HOY");
                        
                        System.out.println("poolList 2: " + poolProcess.toString());
                        
                        for (int j=0; j<numTypeProcLst; j++) {
                            if (assignedTypeProc.get(j).getString("typeProc").equals(typeProc)) {
                                myNumExec = assignedTypeProc.get(j).getInt("usedThread") + 1;
                                assignedTypeProc.get(j).put("usedThread", myNumExec);
                                
                                myNumProcExec = getNumProcExec(); myNumProcExec++;
                                myNumTotalExec = getNumTotalExec(); myNumTotalExec++;
                                
                                numProcExec = myNumProcExec;
                                numTotalExec = myNumTotalExec;
                            }
                        }
                        
                    }
                }
            } else {
                System.out.println("no hay items en poolprocess para actualizar");
            }
            
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
    
    public boolean isExistPoolProcess(String procID) {
        try {
            //Busca el procID en lista de pool de procesos
            int numProc = poolProcess.size();
            boolean findProcID= false;
            for (int i=0; i<numProc; i++) {
                if (poolProcess.get(i).getString("procID").equals(procID)) {
                    findProcID = true;
                }
            }
            return findProcID;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int addPoolProcess (JSONObject itemData) {
        try {
            poolProcess.add(itemData);
            return 0;
        } catch (Exception e) {
            return 1;
        }
    
    }
    
    public boolean isExistFreeThreadProcess(String typeProc) {
        try {
            boolean result = false;
            int maxThreadProc;
            int usedThreadProc;
            int numItems;
            
            numItems = getAssignedTypeProc().size();
            
            for (int i=0; i<numItems; i++) {
                if (getAssignedTypeProc().get(i).getString("typeProc").equals(typeProc)) {
                    maxThreadProc = getAssignedTypeProc().get(i).getInt("maxThread") ;
                    usedThreadProc = getAssignedTypeProc().get(i).getInt("usedThread");
                    result = usedThreadProc < maxThreadProc;
                    System.out.println("max: "+ maxThreadProc);
                    System.out.println("used: "+ usedThreadProc);
                }
            }

            return result;
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    public boolean isExistFreeThreadServices() {
        try {
            int maxThreadServices;
            int maxThreadProcess;
            System.out.println("dentro");
            maxThreadProcess = getNumProcExec();
            maxThreadServices = getNumProcMax();
            
            return maxThreadProcess < maxThreadServices;
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
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
                srvMonHostBack = fileConf.getProperty("srvMonHostBack");
                isActivePrimaryMonHost = fileConf.getProperty("activePrimaryMonHost").equals("true");
                
                
                //Setea Parametros iniciales
                //
                isConnectMonHost = false;
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
