/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.dcAssignedTypeProc;
import dataClass.dcPoolProcess;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class globalAreaData {
    //Carga Clase log4
    Logger logger = Logger.getLogger("globalAreaData");
    
    //Carga Clases de Datos
    
    List<dcAssignedTypeProc> lstTypeProc = new ArrayList<>();
    List<dcPoolProcess> lstPoolProcess = new ArrayList<>();
    
    //Parametros globales del servicio
    //
    private String srvID;          //Identificador unico del Servicio
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
    private boolean isSocketServerActive;
    
        
    /**
     * Parametros para recibir assigned Process
     * format json:
     * {"typeProc":"ETL","priority":"1","maxThread":"10","usedThread":"0"}
     */
    
    List<JSONObject> assignedTypeProc = new ArrayList<>();
    
    
    //Lista para almacenar el pool de ejecuciones a realizar
    //
    List<JSONObject> poolProcess = new ArrayList<>();
       
    //Parametros de Control Online
    //
    private int numProcExec;         //Numero de procesos en ejecucion
    private int numProcMax;          //Maximo numero permitido de procesos simultaneos
    private int numTotalExec;        //Total de procesos ejecutados desde el startup
    
    //Metodos de Acceso a los Datos
    //
    public int getPosTypeProc(String typeProc) {
        int posFound=0;
        if (lstTypeProc!=null) {
            int numItems= lstTypeProc.size();
            if (numItems!=0) {
                for (int i=0; i<numItems; i++) {
                    if (lstTypeProc.get(i).getTypeProc().equals(typeProc)) {
                        posFound=i;
                    }
                }
                return posFound;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public List<dcAssignedTypeProc> getLstTypeProc() {
        return lstTypeProc;
    }

    public void setLstTypeProc(List<dcAssignedTypeProc> lstTypeProc) {
        this.lstTypeProc = lstTypeProc;
    }

    public List<dcPoolProcess> getLstPoolProcess() {
        return lstPoolProcess;
    }

    public void setLstPoolProcess(List<dcPoolProcess> lstPoolProcess) {
        this.lstPoolProcess = lstPoolProcess;
    }

    public boolean isIsSocketServerActive() {
        return isSocketServerActive;
    }

    public void setIsSocketServerActive(boolean isSocketServerActive) {
        this.isSocketServerActive = isSocketServerActive;
    }

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

    public String getSrvID() {
        return srvID;
    }

    public void setSrvID(String srvID) {
        this.srvID = srvID;
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
            System.out.println(" Error updateRunningPoolProcess: "+e.getMessage());
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
            System.out.println(" Error isExistPoolProcess: "+e.getMessage());
            return false;
        }
    }
    
    public int addPoolProcess (JSONObject itemData) {
        try {
            poolProcess.add(itemData);
            return 0;
        } catch (Exception e) {
            System.out.println(" Error addPoolProcess: "+e.getMessage());
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
                    try {
                        usedThreadProc = getAssignedTypeProc().get(i).getInt("usedThread");
                    } catch (Exception e) {
                        usedThreadProc = 0;
                    }
                    result = usedThreadProc < maxThreadProc;
                    logger.info(" maxThreadProc: "+maxThreadProc);
                    logger.info(" usedThreadProc: "+usedThreadProc);
                }
            }

            return result;
        
        } catch (Exception e) {
            logger.error(" Error isExistFreeThreadProcess: "+e.getMessage());
            return false;
        }
    }
    
    
    public boolean isExistFreeThreadServices() {
        try {
            int maxThreadServices;
            int maxThreadProcess;
            maxThreadProcess = getNumProcExec();
            maxThreadServices = getNumProcMax();
            
            return maxThreadProcess < maxThreadServices;
        
        } catch (Exception e) {
            logger.error(" Error isExistFreeThreadServices: "+e.getMessage());
            return false;
        }
    }

    
    public globalAreaData() {
            Properties fileConf = new Properties();
            
            try {
                logger.info(" Iniciando globalAreaData...");

                //Parametros del File Properties
                //
                fileConf.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvServer/src/utilities/srvServer.properties"));

                srvID   = fileConf.getProperty("srvID");
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
                isSocketServerActive = false;
                isConnectMonHost = false;
                isRegisterService = false;
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
                
                logger.info(" Se ha iniciado correctamente la globalAreaData...");
                
            } catch (IOException | NumberFormatException e) {
                srvLoadParam = false;
                logger.error(" Error general: "+e.getMessage());
            }
    }
}
