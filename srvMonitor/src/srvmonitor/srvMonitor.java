/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import utilities.globalAreaData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import utilities.srvRutinas;
import org.apache.log4j.Logger;

/**
 *
 * @author andresbenitez
 */
public class srvMonitor {
    static globalAreaData gDatos;
    static srvRutinas gSub;
    
    //Carga Clase log4
    static Logger logger = Logger.getLogger("srvMonitor");
    
    public srvMonitor() {
        /*
            El constructor solo se ejecuta cuando la clase es instanciada desde otra.
            Cuando la clase posee un main() principal de ejecución, el constructor  no
            es considerado.
        */        
    }   

    public static void main(String[] args) throws IOException {
        try {
            //Instancia las Clases
            logger.info("Iniciando srvMonitor...");
            gDatos  = new globalAreaData();
            gSub = new srvRutinas(gDatos);

            if (gDatos.getServerStatus().isIsLoadParam()) {
                if (gDatos.getServerStatus().isIsLoadRutinas()) {
                    Timer mainTimer = new Timer("thMain");
                    mainTimer.schedule(new mainTimerTask(), 2000, gDatos.getServerInfo().getTxpMain());
                    logger.info("Scheduling MainTask cada: "+ gDatos.getServerInfo().getTxpMain()/1000 + " segundos");
                    logger.info("Server: "+ gDatos.getServerInfo().getSrvID());
                    logger.info("Listener Port: " + gDatos.getServerInfo().getSrvPort());
                    logger.info("Metadata Type: " + gDatos.getServerInfo().getDbType());
                    logger.info("Maximo Procesos: " +  gDatos.getServerStatus().getNumProcMax());
                } else {
                    logger.error("Error cargando Rutinas...abortando modulo.");
                }
            } else { 
                logger.error("Error cargando globalAreaData...abortando modulo.");
            }
        } catch (Exception e) {
            logger.error("Error en modulo principal: "+e.getMessage()+ " ");
        }
    }
    
    static class mainTimerTask extends TimerTask {
        //Declare los Thread de cada proceso
        //
        Thread thSocket;
        Thread thKeep;
        Thread thAgendas;
        Thread thETL;
        
        //Constructor de la clase
        public mainTimerTask() {
        }

        @Override
        public void run() { 
            logger.info("Ejecutando MainTimerTask...");
            /*
            Buscando Thread Activos
            */
            boolean thMonitorFound=false;
            boolean thKeepFound= false;
            boolean thAgendaFound = false;
            boolean thSubKeepFound = false;
            boolean thETLFound = false;
            List<String> lstThreadActivos = new ArrayList<>();
            lstThreadActivos.clear();

            logger.info("Revisando Threads de Modulos Activos...");
            //Thread tr = Thread.currentThread();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            //System.out.println("Current Thread: "+tr.getName()+" ID: "+tr.getId());
            for ( Thread t : threadSet){
                //System.out.println("Thread :"+t+":"+"state:"+t.getState()+" ID: "+t.getId());
                if (t.getName().equals("thMonitorSocket")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thMonitorFound=true;
                }
                if (t.getName().equals("thKeepAlive")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thKeepFound=true;
                }
                if (t.getName().equals("thGetActiveGroups")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thAgendaFound=true;
                }
                if (t.getName().equals("thSubKeep")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thSubKeepFound=true;
                }
                if (t.getName().equals("thETL")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thETLFound=true;
                }
            }
            
            //Resultado de Busqueda
            if (!thMonitorFound) {
                gDatos.getServerStatus().setIsSocketServerActive(false);
            } else {
                gDatos.getServerStatus().setIsSocketServerActive(true);
            }

            if (!thKeepFound) {
                gDatos.getServerStatus().setIsKeepAliveActive(false);
            } else {
                gDatos.getServerStatus().setIsKeepAliveActive(true);
            }

            if (!thSubKeepFound) {
                gDatos.getServerStatus().setIsKeepAliveActive(false);
            } else {
                gDatos.getServerStatus().setIsKeepAliveActive(true);
            }

            if (!thAgendaFound) {
                gDatos.getServerStatus().setIsGetAgendaActive(false);
            } else {
                gDatos.getServerStatus().setIsGetAgendaActive(true);
            }
            
            if (!thETLFound) {
                gDatos.getServerStatus().setIsThreadETLActive(false);
            } else {
                gDatos.getServerStatus().setIsThreadETLActive(true);
            }
            
            /**
             * Informa Threads encontrados
             */
            if (!lstThreadActivos.isEmpty()) {
                for (int i=0; i<lstThreadActivos.size(); i++) {
                    logger.info("Se ha encontrado el thread: "+lstThreadActivos.get(i));
                }
            } else {
                logger.info("No hay Threads de Modulos internos activos.");
            }

            /*
            Se aplicara validacion modular de procesos, ya que se encuentran en un bucle infinito.
            */
            
            //Levanta Socket Server
            //
            try {
                if (!gDatos.getServerStatus().isIsSocketServerActive()) {
                //if (!thSocket.isAlive()) {
                    thSocket = new thMonitorSocket(gDatos);
                    thSocket.setName("thMonitorSocket");
                    gDatos.getServerStatus().setIsSocketServerActive(true);
                    thSocket.start();
                    logger.info("Iniciando thMonitor Server....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsSocketServerActive(false);
                logger.error("Error al Iniciar socket monitor server "+ thSocket.getName() + " : "+e.getMessage());
            }
            
            //Levanta KeepAlive
            //
            try {
                if (!gDatos.getServerStatus().isIsKeepAliveActive()) {
                    thKeep = new thKeepAliveServices(gDatos);
                    thKeep.setName("thKeepAlive");
                    //System.out.println(thKeep.getId());
                    gDatos.getServerStatus().setIsKeepAliveActive(true);
                    logger.info(" Iniciando thread KeepAlive....");
                    thKeep.start();
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsKeepAliveActive(false);
                //System.out.println("Error. "+e.getMessage());
                logger.error("Error al Iniciar thread: "+ thKeep.getName());
            }
            
            //Levanta Thread para revision de Agendas y Procesos que deberan ejecutarse
            //
            
//            if (gDatos.getServerStatus().isIsValMetadataConnect()) {
//                //Levanta Thread Busca Agendas Activas
//                //
//                try {
//                    if (!gDatos.getServerStatus().isIsGetAgendaActive()) {
//                        thAgendas = new thGenActiveGroups(gDatos);  
//                        thAgendas.setName("thGetActiveGroups");
//                        gDatos.getServerStatus().setIsGetAgendaActive(true);
//                        thAgendas.start();
//                        logger.info(" Iniciando thGetActiveGroups....normal...");
//                    } 
//                } catch (Exception e) {
//                    gDatos.getServerStatus().setIsGetAgendaActive(false);
//                    //System.out.println("Error. "+e.getMessage());
//                    logger.error("Error al Iniciar Thread Agendas "+ thAgendas.getName());
//                }
//            } else {
//                logger.warn("No es posble conectarse a MetaData...");
//            }

            //Levanta Thread para revision de Procesos de ETL que deberán ejecutarse
            //
            
            if (gDatos.getServerStatus().isIsValMetadataConnect()) {
                try {
                    if (!gDatos.getServerStatus().isIsThreadETLActive()) {
                        thETL = new thGetETL(gDatos);  
                        thETL.setName("thGetETL");
                        gDatos.getServerStatus().setIsThreadETLActive(true);
                        thETL.start();
                        logger.info(" Iniciando thGetETL....normal...");
                    } 
                } catch (Exception e) {
                    gDatos.getServerStatus().setIsThreadETLActive(false);
                    //System.out.println("Error. "+e.getMessage());
                    logger.error("Error al Iniciar Thread ETL "+ thETL.getName());
                }
            } else {
                logger.warn("No es posble conectarse a MetaData...");
            }
        }
    }
}
