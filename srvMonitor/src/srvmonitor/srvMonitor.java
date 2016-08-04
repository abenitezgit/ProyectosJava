/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import utilities.globalAreaData;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
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
            logger.info("Starting MainTask Schedule cada: "+ gDatos.getServerInfo().getTxpMain()/1000 + " segundos");
            logger.info("Server: "+ gDatos.getServerInfo().getSrvID());
            logger.info("Listener Port: " + gDatos.getServerInfo().getSrvPort());
            logger.info("Metadata Type: " + gDatos.getServerInfo().getDbType());
            logger.info("Maximo Procesos: " +  gDatos.getServerStatus().getNumProcMax());
            logger.info("Estado Servicio: " + gDatos.getServerStatus().isSrvActive());
        } else { 
            logger.error("Error cargando AreaData");
        }
    }
    
    static class mainTimerTask extends TimerTask {
        
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
                Thread tr = Thread.currentThread();
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                System.out.println("Current Thread: "+tr.getName()+" ID: "+tr.getId());
                for ( Thread t : threadSet){
                    System.out.println("Thread :"+t+":"+"state:"+t.getState()+" ID: "+t.getId());
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
            
            //Resulta Busqueda
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
                    logger.info(" Iniciando thMonitor Server....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsSocketServerActive(false);
                System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido levantar socket monitor server "+ thSocket.getName());
            }
            
            //Levanta KeepAlive
            //
            try {
                if (!gDatos.getServerStatus().isIsKeepAliveActive()) {
                //if (!thSocket.isAlive()) {
                    thKeep = new thKeepAliveServices(gDatos);
                    thKeep.setName("thKeepAlive");
                    System.out.println(thKeep.getId());
                    gDatos.getServerStatus().setIsKeepAliveActive(true);
                    logger.info(" Iniciando thread KeepAlive....normal...");
                    thKeep.start();
                } 
            } catch (Exception e) {
                gDatos.getServerStatus().setIsKeepAliveActive(false);
                System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido levantar thread: "+ thKeep.getName());
            }
            
            //Establece Conexión a Metadata
            if (!gDatos.getServerStatus().isIsMetadataConnect()) {
                if (gDatos.getServerInfo().getDbType().equals("ORA")) {
                    try {
                        Class.forName(gDatos.getServerInfo().getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.getServerStatus().setMetadataConnection(DriverManager.getConnection(gDatos.getServerInfo().getConnString(), gDatos.getServerInfo().getDbOraUser(), gDatos.getServerInfo().getDbOraPass()));
                        gDatos.getServerStatus().setIsMetadataConnect(true);
                        logger.info("conectado a metadata ORA...");
                    } catch (SQLException ex) {
                        logger.error("no es posible conectarse a metadata..."+ ex.getMessage());
                        gDatos.getServerStatus().setIsMetadataConnect(false);
                    }
                
                }
                if (gDatos.getServerInfo().getDbType().equals("SQL")) {
                    try {
                        Class.forName(gDatos.getServerInfo().getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.getServerStatus().setMetadataConnection(DriverManager.getConnection(gDatos.getServerInfo().getConnString()));
                        gDatos.getServerStatus().setIsMetadataConnect(true);
                        logger.info("Conectado a Metadata");
                    } catch (SQLException ex) {
                        logger.error("No es posible conectarse a Metadata. Error: "+ ex.getMessage());
                        gDatos.getServerStatus().setIsMetadataConnect(false);
                    }
                }
            }
            
            if (gDatos.getServerStatus().isIsMetadataConnect()) {
                /*
                    Busca en BD Tipos de Procesos asignados a los servicios
                */
                try {
                    int result = gSub.getMDprocAssigned();
                    if (result==0) {
                        logger.info("Se recuperaron exitosamente los procesos asignados...");
                    } else {
                        logger.error("No es posible recuperar los procesos asignados...");
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(srvMonitor.class.getName()).log(Level.SEVERE, null, ex);
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
                    System.out.println("Error. "+e.getMessage());
                    logger.error("no se ha podido levantar Thread Agendas "+ thAgendas.getName());
                }
            } else {
                logger.error("No es posible conectarse a BD Metadata...");
            }
        }
    }
}
