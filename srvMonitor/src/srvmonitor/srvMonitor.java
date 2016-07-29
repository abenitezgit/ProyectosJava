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
    static boolean isSocketActive;
    
    //Carga Clase log4
    static Logger logger = Logger.getLogger("srvMonitor");    
    
    public srvMonitor() {

    }   

    //Aplicacion Servidor
    //Levanta Puerto para informar status

    public static void main(String[] args) throws IOException {
        //Instancia las Clases
        gDatos  = new globalAreaData();
        gSub = new srvRutinas(gDatos);
        
        //Inicializa Parametros
        isSocketActive  = true;
        
        if (gDatos.isSrvLoadParam()) {
            Timer mainTimer = new Timer();
            mainTimer.schedule(new mainTimerTask(), 2000, Integer.valueOf(gDatos.getTxpMain()));
            logger.info("Starting MainTask Schedule cada: "+ Integer.valueOf(gDatos.getTxpMain())/1000 + " segundos");
            logger.info("Server: "+ gDatos.getSrvName());
            logger.info("Listener Port: " + gDatos.getSrvPort());
            logger.info("Metadata Type: " + gDatos.getDbType());
            logger.info("Maximo Procesos: " +  gDatos.getNumProcMax());
            logger.info("Estado Servicio: " + String.valueOf(gDatos.isSrvActive()));
        } else { 
            logger.error("Error cargando AreaData");
        }
    }
    
    static class mainTimerTask extends TimerTask {
        
        //Declare los Thread de cada proceso
        //
        Thread thSocket = new thMonitorSocket(gDatos);
        Thread thKeepMon = new thKeepAliveServices(gDatos);
        Thread thAgendas = new thGetAgendas(gDatos);
        
        //Constructor de la clase
        public mainTimerTask() {
        }

        private void levantaServerSocket() {
            try {
                if (!thSocket.isAlive()) {
                    thSocket.start();
                    gDatos.setIsSocketServerActive(true);
                    logger.info("iniciando thMonitorSocket...normal..."+thSocket.getName());
                } 
            } catch (Exception e) {
                    thSocket = new thMonitorSocket(gDatos);
                    thSocket.start();
                    gDatos.setIsSocketServerActive(true);
                    logger.warn("iniciando thMonitorSocket...forced..."+thSocket.getName());
            }
        }
    
        private void metaDataConnect() {
            //Establece Conexión a Metadata
            if (!gDatos.isIsMetadataConnect()) {
                if (gDatos.getDbType().equals("ORA")) {
                    try {
                        Class.forName(gDatos.getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.setMetadataConnection(DriverManager.getConnection(gDatos.getConnString(), gDatos.getDbORAUser(), gDatos.getDbORAPass()));
                        gDatos.setIsMetadataConnect(true);
                        logger.info("conectado a metadata ORA...");
                    } catch (SQLException ex) {
                        logger.error("no es posible conectarse a metadata..."+ ex.getMessage());
                        gDatos.setIsMetadataConnect(false);
                    }
                
                }
                if (gDatos.getDbType().equals("SQL")) {
                    try {
                        Class.forName(gDatos.getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.setMetadataConnection(DriverManager.getConnection(gDatos.getConnString()));
                        gDatos.setIsMetadataConnect(true);
                        logger.info("Conectado a Metadata");
                    } catch (SQLException ex) {
                        logger.error("No es posible conectarse a Metadata. Error: "+ ex.getMessage());
                        gDatos.setIsMetadataConnect(false);
                    }
                }
            }
        }

        
        @Override
        public void run() { 
            logger.info("Ejecutando MainTimerTask...");
            
            //Starting Monitor Thread Server
            //
            levantaServerSocket();
            if (gDatos.isIsSocketServerActive()) {
                metaDataConnect();
                if (gDatos.isIsMetadataConnect()) {
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
                    /*
                    Ejecuta Proceso en Thread para Buscar Agendas y Procesos Activos
                    */
                    try {
                        if (!thAgendas.isAlive()) {
                            thAgendas.start();
                            logger.info("iniciando thAgendas...normal...");
                        } 
                    } catch (Exception e) {
                        thAgendas = new thGetAgendas(gDatos);
                        thAgendas.start();
                        logger.warn("iniciando thAgendas...forced...");
                    }
                } else {
                    logger.error("No es posible conectarse a BD Metadata...");
                }
            } else {
                logger.error("No es posible levantar Socket Server...");
            }
                
        }
    }
}
