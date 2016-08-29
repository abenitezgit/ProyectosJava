/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;
import dataClass.ActiveTypeProc;
import dataClass.AssignedTypeProc;
import dataClass.PoolProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utilities.globalAreaData;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thRunProcess extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static Logger logger = Logger.getLogger("thRunProcess");
    
    //Carga constructor para inicializar los datos
    public thRunProcess(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
        
    @Override
    public void run() {
        Timer timerMain = new Timer("thSubRunProcess");
        timerMain.schedule(new mainKeepTask(), 5000, 7000);
    }
    
    
    static class mainKeepTask extends TimerTask {
        /*
        Crea las listas para asignaciones de Pesos especificos
        */
        List<pesoAssigned> lstPesoAssigned = new ArrayList<>();
        List<pesoActive> lstPesoActive = new ArrayList<>();

    
        public mainKeepTask() {
        }

        @Override
        public void run() {
            logger.info("Inicia thSubRunProcess");

            
            int numItemsPool = gDatos.getLstPoolProcess().size();
            
            if (numItemsPool>0) {
                updatePoolStatistics();
                
                int itemsAssigned = gDatos.getLstAssignedTypeProc().size();
                if (itemsAssigned>0) {
                    
                    /*
                    Calcula los pesos especificos de las listas asignadas y activas
                    Las lista a generarse son:
                    lstPesoAssigned
                    lstPesoActive
                    */
                    setPesoEspecifico();
                    updatePoolStatistics();
                    
                    /*
                    Ejcuta Procesos en estado Ready
                    */
                    List<PoolProcess> lstReadyProc = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Ready")).collect(Collectors.toList());

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

                    try {
                        logger.debug("Mapper lstReady: "+mapper.writeValueAsString(lstReadyProc));
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(thRunProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    if (!lstReadyProc.isEmpty()) {
                        if (gDatos.getFreeThreadServices()>0) {
                            int numSleep = lstReadyProc.size();
                            logger.debug("Procesos Sleeping: "+numSleep);
                            for (int i=0; i<numSleep; i++) {
                                if (gDatos.getFreeThreadProcess(lstReadyProc.get(i).getTypeProc())>0) {
                                    switch (lstReadyProc.get(i).getTypeProc()) {
                                        case "OSP":
                                            Thread thOSP = new thExecOSP(gDatos, lstReadyProc.get(i));
                                            //thOSP.setName("thExecOSP-"+lstSleepingProc.get(i).getProcID());
                                            thOSP.setName("OSPThread");
                                            gDatos.setStatusRunning(lstReadyProc.get(i));
                                            thOSP.start();
                                            break;
                                        case "OTX":
//                                            Thread thOTX = new thExecOTX(gDatos, lstReadyProc.get(i).getParams());
//                                            thOTX.setName("thExecOTX-"+lstReadyProc.get(i).getProcID());
//                                            gDatos.setStatusRunning(lstReadyProc.get(i));
//                                            thOTX.start();
                                            break;
                                        case "LOR":
//                                            Thread thLOR = new thExecLOR(gDatos, lstReadyProc.get(i).getParams());
//                                            thLOR.setName("thExecLOR-"+lstReadyProc.get(i).getProcID());
//                                            gDatos.setStatusRunning(lstReadyProc.get(i));
//                                            thLOR.start();
                                            break;
                                        case "FTP":
                                            Thread thFTP = new thExecFTP(gDatos, lstReadyProc.get(i));
                                            thFTP.setName("thExecFTP-"+lstReadyProc.get(i).getProcID());
                                            gDatos.setStatusRunning(lstReadyProc.get(i));
                                            thFTP.start();
                                            break;
                                        case "ETL":
                                            Thread thETL = new thExecETL(gDatos, lstReadyProc.get(i));
                                            thETL.setName("thExecETL-"+ lstReadyProc.get(i).getIntervalID());
                                            //gDatos.setStatusRunning(lstReadyProc.get(i));
                                            gDatos.updateStatusPoolProcess("ETL", lstReadyProc.get(i).getProcID(), "Running", lstReadyProc.get(i).getIntervalID());
                                            thETL.start();
                                            break;
                                        default:
                                            logger.info("No hay Rutinas de Ejecucion asiciadas a este tipo de proceso: "+lstReadyProc.get(i).getTypeProc());
                                            break;
                                    }
                                } else {
                                    logger.warn("No hay Threads libres del proceso: "+ lstReadyProc.get(i).getTypeProc() + " para ejecutar");
                                    logger.warn("Se marcaran los procesos con release para ser liberados");
                                    gDatos.updateReleasePool(lstReadyProc.get(i).getTypeProc());
                                }
                                updatePoolStatistics();
                            }
                        } else {
                            logger.warn("No hay Threads libres del Servicio para ejecutar");
                        }
                    } else {
                        logger.info("No hay procesos pendientes para ejecutar");
                    }
                
                } else {
                    logger.info("No hay tipos de procesos Asignados");
                }
            } else {
                logger.info("No hay procesos Ready para ser ejecutados en Pool de Ejecucion");
            }
            
            logger.info("Finalizando Thread thRunProcess.");
            
        }
        
        private void setPesoEspecifico() {
            /*
            Calcula peso especifico de lista de typeProc asignados
            */
            long numTotalAssignedProc = gDatos.getLstAssignedTypeProc().size();
            if (numTotalAssignedProc!=0) {
                for (int i=0; i<numTotalAssignedProc; i++) {
                    String typeProc = gDatos.getLstAssignedTypeProc().get(i).getTypeProc();
                    int priority = gDatos.getLstAssignedTypeProc().get(i).getPriority();
                    //float count = gDatos.getLstAssignedTypeProc().stream().filter(p -> p.getPriority()==priority).count();
                    
                    Stream<AssignedTypeProc> countd = gDatos.getLstAssignedTypeProc().stream().filter(p -> p.getPriority()==priority).distinct();
                    float count = countd.count();
                    
                    float peso = count/numTotalAssignedProc*100/priority;
                    pesoAssigned pesoAssigned = new pesoAssigned(typeProc, (int) peso);
                    lstPesoAssigned.add(pesoAssigned);
                }
            }
            
            /*
            Calcula perso especifico de lista de ActiveProc 
            */
            long numTotalActiveProc = gDatos.getLstActiveTypeProc().size();
            if (numTotalActiveProc!=0) {
                for (int i=0; i<numTotalActiveProc; i++) {
                    String typeProc = gDatos.getLstActiveTypeProc().get(i).getTypeProc();
                    int usedThread = gDatos.getLstActiveTypeProc().get(i).getUsedThread();
                    float sumThread = gDatos.getLstActiveTypeProc().stream().collect(Collectors.summingInt(p -> p.getUsedThread()));
                    float peso = usedThread/sumThread*100;
                    pesoActive pesoActive = new pesoActive(typeProc, (int) peso);
                    lstPesoActive.add(pesoActive);
                }
            }
            
            /*
            Ordena lista por peso de Mayor a menos
            */
            int numItems = lstPesoAssigned.size();
            if (numItems>1) {
                for (int i=0;i<numItems; i++ ) {
                    for (int j=0; j<numItems-1; j++) {
                        if (lstPesoAssigned.get(j).getPeso()<lstPesoAssigned.get(j+1).getPeso()) {
                            pesoAssigned pesoAssignedAux = new pesoAssigned(lstPesoAssigned.get(j).getTypeProc(), (int) lstPesoAssigned.get(j).getPeso());
                            lstPesoAssigned.set(j, lstPesoAssigned.get(j+1));
                            lstPesoAssigned.set(j+1, pesoAssignedAux);
                        }
                    }
                }
            }
        }
        
        private void updatePoolStatistics() {
            /*
            Actualiza Estadisticas de Procesos
            */
            
            ActiveTypeProc activeTypeProc;
            List<ActiveTypeProc> lstActiveTypeProc = new ArrayList<>();
            List<PoolProcess> lstRunning = new ArrayList<>();
            List<PoolProcess> lstSleeping = new ArrayList<>();
            List<PoolProcess> lstFinished = new ArrayList<>();

            gDatos.getLstActiveTypeProc().clear();
            lstActiveTypeProc.clear();
            lstRunning.clear();
            lstSleeping.clear();
            lstFinished.clear();
            
            lstRunning = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Running")).collect(Collectors.toList());
            lstSleeping = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Assigned")).collect(Collectors.toList());
            lstFinished = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Finished")).collect(Collectors.toList());
            
            gDatos.getServiceStatus().setNumProcRunning(lstRunning.size());
            gDatos.getServiceStatus().setNumProcSleeping(lstSleeping.size());
            gDatos.getServiceStatus().setNumProcFinished(lstFinished.size());
            
            
            /*
            Recorre la lista de Running recuperando por cada registro encontrado el typeProc
            y lo consulta en la lista de procesos activos para ir adicionando el contador
            */
            
            int numItemsRunning = lstRunning.size();
            for (int i=0; i<numItemsRunning; i++) {
                int index = gDatos.getIndexOfActiveTypeProc(lstRunning.get(i).getTypeProc());
                if (index!=-1) {
                    int usedTypeActive = gDatos.getLstActiveTypeProc().get(index).getUsedThread();
                    String typeProc = gDatos.getLstActiveTypeProc().get(index).getTypeProc();
                    
                    activeTypeProc = new ActiveTypeProc();
                    activeTypeProc.setTypeProc(typeProc);
                    activeTypeProc.setUsedThread(usedTypeActive+1);
                    
                    gDatos.getLstActiveTypeProc().set(index, activeTypeProc);
                } else {
                    activeTypeProc = new ActiveTypeProc();
                    activeTypeProc.setTypeProc(lstRunning.get(i).getTypeProc());
                    activeTypeProc.setUsedThread(1);
                    
                    gDatos.getLstActiveTypeProc().add(activeTypeProc);
                }
            }
        }
        
        /**
         * Data Class Internas
         */
        
        class pesoAssigned {
            String typeProc;
            int peso;

            public pesoAssigned(String typeProc, int peso) {
                this.typeProc = typeProc;
                this.peso = peso;
            }

            public String getTypeProc() {
                return typeProc;
            }

            public void setTypeProc(String typeProc) {
                this.typeProc = typeProc;
            }

            public float getPeso() {
                return peso;
            }

            public void setPeso(int peso) {
                this.peso = peso;
            }
        }
        
        class pesoActive {
            String typeProc;
            int peso;

            public pesoActive(String typeProc, int peso) {
                this.typeProc = typeProc;
                this.peso = peso;
            }

            public String getTypeProc() {
                return typeProc;
            }

            public void setTypeProc(String typeProc) {
                this.typeProc = typeProc;
            }

            public float getPeso() {
                return peso;
            }

            public void setPeso(int peso) {
                this.peso = peso;
            }
        }
    }
}
