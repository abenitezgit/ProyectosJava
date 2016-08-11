/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;
import dataClass.ActiveTypeProc;
import dataClass.PoolProcess;
import java.util.ArrayList;
import java.util.List;
import utilities.globalAreaData;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thRunProcess extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    Logger logger = Logger.getLogger("thRunProcess");
    
    //Carga constructor para inicializar los datos
    public thRunProcess(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
        
    @Override
    public void run() {
        Timer timerMain = new Timer("thSubRunProcess");
        timerMain.schedule(new mainKeepTask(), 1000, 5000);
    }
    
    
    static class mainKeepTask extends TimerTask {
    
        public mainKeepTask() {
        }

        @Override
        public void run() {
            System.out.println("Inicia thSubRunProcess");
            
            /*
            Valida Ejecucion de Procesos Sleeping (Encolados)
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
            
            List<pesoAssigned> lstPesoAssigned = new ArrayList<>();

            long numTotalAssignedProc = gDatos.getLstAssignedTypeProc().size();
            if (numTotalAssignedProc!=0) {
                for (int i=0; i<numTotalAssignedProc; i++) {
                    String typeProc = gDatos.getLstAssignedTypeProc().get(i).getTypeProc();
                    int priority = gDatos.getLstAssignedTypeProc().get(i).getPriority();
                    float count = gDatos.getLstAssignedTypeProc().stream().filter(p -> p.getPriority()==priority).count();
                    float peso = count/numTotalAssignedProc*100;
                    pesoAssigned pesoAssigned = new pesoAssigned(typeProc, (int) peso);
                    lstPesoAssigned.add(pesoAssigned);
                }
            }
            
            for (int i=0; i<lstPesoAssigned.size(); i++) {
                System.out.println("typeProc: "+lstPesoAssigned.get(i).getTypeProc());
                System.out.println("perso: "+lstPesoAssigned.get(i).getPeso());
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
            
            List<pesoActive> lstPesoActive = new ArrayList<>();
            
            ActiveTypeProc activeType = new ActiveTypeProc();
            activeType.setTypeProc("OSP");
            activeType.setUsedThread(1);
            
            gDatos.getLstActiveTypeProc().add(activeType);
            activeType = new ActiveTypeProc();
            
            activeType.setTypeProc("ETL");
            activeType.setUsedThread(2);

            gDatos.getLstActiveTypeProc().add(activeType);
            activeType = new ActiveTypeProc();

            activeType.setTypeProc("FTP");
            activeType.setUsedThread(1);
            
            gDatos.getLstActiveTypeProc().add(activeType);
            
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
            
            for (int i=0; i<lstPesoActive.size(); i++) {
                System.out.println("typeProcAct: "+lstPesoActive.get(i).getTypeProc());
                System.out.println("pesoAct: "+lstPesoActive.get(i).getPeso());
            }
            
            /*
            Actualiza Estadisticas de Procesos AA
            */
            gDatos.getLstActiveTypeProc().clear();
            
            ActiveTypeProc activeTypeProc;
            List<ActiveTypeProc> lstActiveTypeProc;
            
            List<PoolProcess> lstRunning = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Running")).collect(Collectors.toList());
            List<PoolProcess> lstSleeping = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Sleeping")).collect(Collectors.toList());
            List<PoolProcess> lstFinished = gDatos.getLstPoolProcess().stream().filter(p -> p.getStatus().equals("Finished")).collect(Collectors.toList());
            
            gDatos.getServiceStatus().setNumProcRunning(lstRunning.size());
            gDatos.getServiceStatus().setNumProcSleeping(lstSleeping.size());
            gDatos.getServiceStatus().setNumProcFinished(lstFinished.size());
            
            int numItemsRunning = lstRunning.size();
            for (int i=0; i<numItemsRunning; i++) {
                int index = gDatos.getLstActiveTypeProc().indexOf(lstRunning.get(i).getTypeProc());
                if (index!=0) {
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
    }
}
