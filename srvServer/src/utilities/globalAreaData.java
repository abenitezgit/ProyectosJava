/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.ActiveTypeProc;
import dataClass.AssignedTypeProc;
import dataClass.PoolProcess;
import dataClass.ServiceInfo;
import dataClass.ServiceStatus;
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
    /**
     * Clase de Logger4
     */
    Logger logger = Logger.getLogger("globalAreaData");
    
    /**
     * Clases de Estructuras de Datos
     */
    List<AssignedTypeProc> lstAssignedTypeProc = new ArrayList<>();
    List<ActiveTypeProc> lstActiveTypeProc = new ArrayList<>();
    List<PoolProcess> lstPoolProcess = new ArrayList<>();
    ServiceInfo serviceInfo = new ServiceInfo();
    ServiceStatus serviceStatus = new ServiceStatus();

    /**
     * Getter And Setter Area
     * @return 
     */
    public List<PoolProcess> getLstPoolProcess() {
        return lstPoolProcess;
    }

    public synchronized void setLstPoolProcess(List<PoolProcess> lstPoolProcess) {
        this.lstPoolProcess = lstPoolProcess;
    }
    
    public List<AssignedTypeProc> getLstAssignedTypeProc() {
        return lstAssignedTypeProc;
    }

    public synchronized void setLstAssignedTypeProc(List<AssignedTypeProc> lstAssignedTypeProc) {
        this.lstAssignedTypeProc = lstAssignedTypeProc;
    }

    public List<ActiveTypeProc> getLstActiveTypeProc() {
        return lstActiveTypeProc;
    }

    public synchronized void setLstActiveTypeProc(List<ActiveTypeProc> lstActiveTypeProc) {
        this.lstActiveTypeProc = lstActiveTypeProc;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public synchronized void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public synchronized void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    
    //Metodos de Acceso a los Datos
    //
    public int getPosTypeProc(String typeProc) {
        int posFound=0;
        if (lstAssignedTypeProc!=null) {
            int numItems= lstAssignedTypeProc.size();
            if (numItems!=0) {
                for (int i=0; i<numItems; i++) {
                    if (lstAssignedTypeProc.get(i).getTypeProc().equals(typeProc)) {
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

    public synchronized void setStatusFinished(PoolProcess poolProcess) {
        try {
            PoolProcess newPoolProcess = poolProcess;
            String typeProc = poolProcess.getTypeProc();
            String procID = poolProcess.getProcID();
            
            ActiveTypeProc activeTypeProc;
            
            serviceStatus.setNumProcRunning(serviceStatus.getNumProcRunning()-1);
            serviceStatus.setNumProcFinished(serviceStatus.getNumProcFinished()+1);

            int index = getLstActiveTypeProc().indexOf(typeProc);
            if (index!=-1) {
                int usedTypeActive = getLstActiveTypeProc().get(index).getUsedThread();
                
                activeTypeProc = new ActiveTypeProc();
                activeTypeProc.setTypeProc(typeProc);
                activeTypeProc.setUsedThread(usedTypeActive-1);

                getLstActiveTypeProc().set(index, activeTypeProc);
            }
            
            index = getLstPoolProcess().indexOf(procID);
            if (index!=-1) {
                newPoolProcess.setStatus("Finished");
                getLstPoolProcess().set(index, newPoolProcess);
            } else {
                logger.error("El proceso: "+procID+" ya no está disponible...");
            }
        } catch (Exception e) {
            logger.error("Error en setStatusFinished: "+e.getMessage());
        }
    }
    
    public synchronized void setStatusRunning(PoolProcess poolProcess) {
        try {
            PoolProcess newPoolProcess = poolProcess;
            String typeProc = poolProcess.getTypeProc();
            String procID = poolProcess.getProcID();
            
            ActiveTypeProc activeTypeProc;
            
            serviceStatus.setNumProcRunning(serviceStatus.getNumProcRunning()+1);
            serviceStatus.setNumProcSleeping(serviceStatus.getNumProcSleeping()-1);

            int index = getLstActiveTypeProc().indexOf(typeProc);
            if (index!=-1) {
                int usedTypeActive = getLstActiveTypeProc().get(index).getUsedThread();
                
                activeTypeProc = new ActiveTypeProc();
                activeTypeProc.setTypeProc(typeProc);
                activeTypeProc.setUsedThread(usedTypeActive+1);

                getLstActiveTypeProc().set(index, activeTypeProc);
            } else {
                activeTypeProc = new ActiveTypeProc();
                activeTypeProc.setTypeProc(typeProc);
                activeTypeProc.setUsedThread(1);

                getLstActiveTypeProc().add(activeTypeProc);
            }
            
            index = getLstPoolProcess().indexOf(procID);
            if (index!=-1) {
                newPoolProcess.setStatus("Running");
                getLstPoolProcess().set(index, newPoolProcess);
            } else {
                logger.error("El proceso: "+procID+" ya no está disponible...");
            }
        } catch (Exception e) {
            logger.error("Error en setStatusRunning: "+e.getMessage());
        }
    
    }

    //Procimientos y/p Metodos uilitarios
    //
    public int updateRunningPoolProcess(JSONObject ds) {
        return 1;
        /*
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
        */
    }
    
    public int getFreeThreadProcess(String typeProc) {
        try {
            int numItems = lstAssignedTypeProc.size();
            for (int i=0; i<numItems; i++) {
                if (lstAssignedTypeProc.get(i).getTypeProc().equals(typeProc)) {
                    int numItemsActive = lstActiveTypeProc.size();
                    for (int j=0; j<numItemsActive; j++) {
                        if (lstActiveTypeProc.get(i).getTypeProc().equals(typeProc)) {
                            return lstAssignedTypeProc.get(i).getMaxThread()-lstActiveTypeProc.get(j).getUsedThread();
                        }
                    }
                    break;
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public int getFreeThreadServices() {
        try {
            return serviceStatus.getNumProcMax()-serviceStatus.getNumProcRunning();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public globalAreaData() {
            Properties fileConf = new Properties();
            
            try {
                logger.info("Iniciando globalAreaData...");

                //Parametros del File Properties
                //
                
                fileConf.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvServer/src/utilities/srvServer.properties"));

                serviceInfo.setSrvID(fileConf.getProperty("srvID"));
                serviceInfo.setTxpMain(Integer.valueOf(fileConf.getProperty("txpMain")));
                serviceInfo.setAuthKey(fileConf.getProperty("authKey"));
                serviceInfo.setMonPort(Integer.valueOf(fileConf.getProperty("monPort")));
                serviceInfo.setMonPortBack(Integer.valueOf(fileConf.getProperty("monPortBack")));
                serviceInfo.setSrvHost(fileConf.getProperty("srvHost"));
                serviceInfo.setSrvMonHost(fileConf.getProperty("srvMonHost"));
                serviceInfo.setSrvMonHostBack(fileConf.getProperty("srvMonHostBack"));
                serviceInfo.setSrvPort(Integer.valueOf(fileConf.getProperty("srvPort")));
                
                serviceStatus.setSrvID(fileConf.getProperty("srvID"));
                serviceStatus.setNumProcMax(Integer.valueOf(fileConf.getProperty("numProcMax")));
                serviceStatus.setSrvEnable(1);
                serviceStatus.setSrvActive(true);
                serviceStatus.setIsActivePrimaryMonHost(true);
                serviceStatus.setIsSocketServerActive(false);
                serviceStatus.setIsConnectMonHost(false);
                serviceStatus.setIsAssignedTypeProc(false);
                serviceStatus.setNumProcRunning(0);
                serviceStatus.setNumProcSleeping(0);
                serviceStatus.setNumProcFinished(0);
                serviceStatus.setNumTotalExec(0);
                serviceStatus.setSrvHost(serviceInfo.getSrvHost());
                serviceStatus.setSrvPort(serviceInfo.getSrvPort());
                
                //Extrae Fecha de Hoy
                //
                    Date today;
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    today = new Date();

                serviceStatus.setSrvStartTime(today);
                serviceStatus.setSrvLoadParam(true);
                
                logger.info("Se ha iniciado correctamente la globalAreaData...");
                
            } catch (IOException | NumberFormatException e) {
                serviceStatus.setSrvLoadParam(false);
                logger.error(" Error general: "+e.getMessage());
            }
    }
}
