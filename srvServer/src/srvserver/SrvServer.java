/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import utilities.globalAreaData;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class SrvServer {
    static globalAreaData gDatos = new globalAreaData();
    static srvRutinas gRutinas;
    
    public SrvServer() throws IOException {
        gRutinas = new srvRutinas(gDatos);
    }   

    //Aplicacion Servidor
    //Levanta Puerto para informar status

    public static void main(String[] args) throws IOException {
        if (gDatos.isSrvLoadParam()) {
            Timer mainTimer = new Timer();
            mainTimer.schedule(new mainTimerTask(), 2000, Integer.valueOf(gDatos.getTxpMain()));
        } else {
            System.out.println("archivo de parametros no pudo ser cargado");
        }
        
    }
    
    static class mainTimerTask extends TimerTask {
        
        //Declare los Thread de cada proceso
        //
        Thread thSocket;
        Thread thKeep;
        Thread thOSP;
        Thread thLOR;
        Thread thMOV;
        Thread thOTX;
        Thread thFTP;
        Thread thETL;
        
        
        //Constructor de la clase
        public mainTimerTask() {
            
            System.out.println("inicio constructor");

        }
        
        
        @Override
        public void run() {
            System.out.println("Inicio TimerTask Server");
            
            //Monitor KeepAlive
            try {
                if (!thKeep.isAlive()) {
                    if (gDatos.isSrvActive()) {
                        System.out.println("iniciando socket keep  Monitor");
                        thKeep.start();
                    }
                } else {
                    if (!gDatos.isSrvActive()) {
                        thKeep.interrupt();
                        System.out.println("thKeep interrupted");
                    }
                }
            }
            catch (Exception e) {
                if (gDatos.isSrvActive()) {
                    System.out.println("iniciando socket keep  Monitor forced");
                    thKeep = new thKeepAliveSocket(gDatos);
                    thKeep.start();
                }
            }
            
            //Monitor thSocket Server
            //
            try {
                if (!thSocket.isAlive()) {
                    if (gDatos.isSrvActive()) {
                        System.out.println("iniciando socket server");
                        thSocket.start();
                    }
                } else {
                    if (!gDatos.isSrvActive()) {
                        thSocket.interrupt();
                        System.out.println("thSocket interrupted");
                    }
                }
            } catch (Exception e) {
                if (gDatos.isSrvActive()) {
                    System.out.println("iniciando socket Server forced");
                    thSocket = new thServerSocket(gDatos);
                    thSocket.start();
                }
            }
            
            //Control Princial de Ejecucion de Procesos
            if (gDatos.isSrvActive()) {    //Control de Revision de Procesos
                if (gDatos.isSrvGetTypeProc()) { //Valida si ya recibio los parametros globales de ejecucion
                    for (int i=0;i<gDatos.getAssignedTypeProc().size();i++) {
                        System.out.println("procesos assignados: "+ gDatos.getAssignedTypeProc().get(i).toString());
                    }
                } else {
                    System.out.println("no se han recibido los parametros de proceso");
                }
            } else {
                System.out.println("Proceso General no esta activo");
            }
        }
    }
}
