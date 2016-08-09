/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import utilities.globalAreaData;
import java.io.IOException;
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
        //Instancia las Clases
        gDatos  = new globalAreaData();
        gSub = new srvRutinas(gDatos);
                
        if (gDatos.getServerStatus().isSrvLoadParam()) {
            Timer mainTimer = new Timer("thMain");
            mainTimer.schedule(new mainTimerTask(), 2000, gDatos.getServerInfo().getTxpMain());
            logger.info("Scheduling MainTask cada: "+ gDatos.getServerInfo().getTxpMain()/1000 + " segundos");
            logger.info("Server: "+ gDatos.getServerInfo().getSrvID());
            logger.info("Listener Port: " + gDatos.getServerInfo().getSrvPort());
            logger.info("Metadata Type: " + gDatos.getServerInfo().getDbType());
            logger.info("Maximo Procesos: " +  gDatos.getServerStatus().getNumProcMax());
            logger.info("Estado Servicio: " + gDatos.getServerStatus().isSrvActive());
        } else { 
            logger.error("Error cargando globalAreaData");
        }
    }
    
    static class mainTimerTask extends TimerTask {
        //MetaData metadata = new MetaData(gDatos);
        //oracleDB oraConn = new oracleDB("oradb01", "oratest", "1521", "process", "proc01");
        
        //Declare los Thread de cada proceso
        //
        Thread thSocket = new thMonitorSocket(gDatos);
        Thread thKeep;
        Thread thAgendas;
        
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
            //Thread tr = Thread.currentThread();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            //System.out.println("Current Thread: "+tr.getName()+" ID: "+tr.getId());
            for ( Thread t : threadSet){
                //System.out.println("Thread :"+t+":"+"state:"+t.getState()+" ID: "+t.getId());
                if (t.getName().equals("thMonitorSocket")) {
                    thMonitorFound=true;
                }
                if (t.getName().equals("thKeepAlive")) {
                    thKeepFound=true;
                }
                if (t.getName().equals("thGetAgendas")) {
                    thAgendaFound=true;
                }
                if (t.getName().equals("thSubKeep")) {
                    thSubKeepFound=true;
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
            
            /*
            Se aplicara validacion modular de procesos, ya que se encuentran en un bucle infinito.
            */
            
            //Levanta Socket Server
            //
            try {
                if (!gDatos.getServerStatus().isIsSocketServerActive()) {
                //if (!thSocket.isAlive()) {
                    
                    thSocket.setName("thMonitorSocket");
                    gDatos.getServerStatus().setIsSocketServerActive(true);
                    thSocket.start();
                    logger.info("Iniciando thMonitor Server....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsSocketServerActive(false);
                //System.out.println("Error. "+e.getMessage());
                logger.error("Error al Iniciar socket monitor server "+ thSocket.getName());
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
            
            //Levanta Thread Busca Agendas Activas
            //
            try {
                if (!gDatos.getServerStatus().isIsGetAgendaActive()) {
                    thAgendas = new thGetAgendas(gDatos);  
                    thAgendas.setName("thGetAgendas");
                    gDatos.getServerStatus().setIsGetAgendaActive(true);
                    thAgendas.start();
                    logger.info(" Iniciando thGetAgendas....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsGetAgendaActive(false);
                //System.out.println("Error. "+e.getMessage());
                logger.error("Error al Iniciar Thread Agendas "+ thAgendas.getName());
            }
        }
    }
}
