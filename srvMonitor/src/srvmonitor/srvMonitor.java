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
import java.util.logging.Logger;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class srvMonitor {
    static globalAreaData gDatos;
    static srvRutinas gSub;
    static boolean isSocketActive;
    
    public srvMonitor() throws IOException {

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
            gSub.sysOutln("Starting MainTask Schedule cada: "+ Integer.valueOf(gDatos.getTxpMain())/1000 + " segundos");
            gSub.sysOutln("Server: "+ gDatos.getSrvName());
            gSub.sysOutln("Listener Port: " + gDatos.getSrvPort());
            gSub.sysOutln("Metadata Type: " + gDatos.getDbType());
            gSub.sysOutln("Maximo Procesos: " +  gDatos.getNumProcMax());
            gSub.sysOutln("Estado Servicio: " + String.valueOf(gDatos.isSrvActive()));
        } else { 
            System.out.println("error cargando AreaData");
        }
    }
    
    static class mainTimerTask extends TimerTask {
        
        //Declare los Thread de cada proceso
        //
        Thread thSocket = new thMonitorSocket(gDatos);
        
        //Constructor de la clase
        public mainTimerTask() {
            
            System.out.println("inicio constructor");
            
        }
        
        @Override
        public void run() { 
            System.out.println("mainTimerTask vivo");
            
            //Starting Monitor Thread Server
            //
            try {
                if (!thSocket.isAlive()) {
                    if (gDatos.isSrvActive()) {
                        System.out.println("iniciando socket Monitor");
                        thSocket.start();
                    }
                }
            } catch (Exception e) {
                if (gDatos.isSrvActive()) {
                    System.out.println("iniciando socket monitor forced");
                    thSocket = new thMonitorSocket(gDatos);
                    thSocket.start();
                }
            }
            
            //Establece Conexión a Metadata
            if (!gDatos.isIsMetadataConnect()) {
                if (gDatos.getDbType().equals("ORA")) {
                    try {
                        Class.forName(gDatos.getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(srvMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.setMetadataConnection(DriverManager.getConnection(gDatos.getConnString(), gDatos.getDbORAUser(), gDatos.getDbORAPass()));
                        gDatos.setIsMetadataConnect(true);
                        System.out.println("Conectado a Metadata");
                    } catch (SQLException ex) {
                        System.out.println("No es posible conectarse a Metadata. Error: "+ ex.getMessage());
                        gDatos.setIsMetadataConnect(false);
                    }
                    
                
                }
                if (gDatos.getDbType().equals("SQL")) {
                    try {
                        Class.forName(gDatos.getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(srvMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        DriverManager.setLoginTimeout(5);
                        gDatos.setMetadataConnection(DriverManager.getConnection(gDatos.getConnString()));
                        gDatos.setIsMetadataConnect(true);
                        System.out.println("Conectado a Metadata");
                    } catch (SQLException ex) {
                        System.out.println("No es posible conectarse a Metadata. Error: "+ ex.getMessage());
                        gDatos.setIsMetadataConnect(false);
                    }
                }
            }
            
            //Accede a la metada a recuperar parametros de los servicios registrados
            if (gDatos.isIsMetadataConnect()) {
                try {
                    int result = gSub.getMDprocAssigned();
                    if (result!=0) {
                        gSub.sysOutln("no pudo recuperar metadata assigned process");
                    }
                } catch (SQLException ex) {
                    gSub.sysOutln(ex.getMessage());
                }
            
            }
            
        
        }
            
    }
}
