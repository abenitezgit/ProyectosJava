/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import utilities.globalAreaData;
import java.io.IOException;
import java.sql.Connection;
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
    static Connection dbConnection;
    static boolean isMetadataConnect;
    
    public srvMonitor() throws IOException {
        isMetadataConnect = false;
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
            if (!isMetadataConnect) {
                if (gDatos.getDbType().equals("ORA")) {
                    try {
                        Class.forName(gDatos.getDriver());
                        //isMetadataConnect = true;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(srvMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        dbConnection = DriverManager.getConnection(gDatos.getConnString(), gDatos.getDbORAUser(), gDatos.getDbORAPass());
                        isMetadataConnect = true;
                        System.out.println("Conectado a Metadata");
                    } catch (SQLException ex) {
                        System.out.println("No es posible conectarse a Metadata. Error: "+ ex.getMessage());
                    }
                    
                
                }
                if (gDatos.getDbType().equals("SQL")) {
                    isMetadataConnect = true;
                }
            }
        
        }
            
    }
}
