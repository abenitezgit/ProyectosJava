/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import utilities.globalAreaData;
import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class SrvServer {
    static globalAreaData gDatos = new globalAreaData();
    static srvRutinas gSub ;
    
    //Carga Clase log4
    static Logger logger = Logger.getLogger("srvServer");
    
    public SrvServer() {
        /*
            El constructor solo se ejecuta cuando la clase es instanciada desde otra.
            Cuando la clase posee un main() principal de ejecución, el constructor  no
            es considerado.
        */
    }   

    public static void main(String[] args) throws IOException {
        try {
            gSub = new srvRutinas(gDatos);

            if (gDatos.getServiceStatus().isSrvLoadParam()) {
                Timer mainTimer = new Timer("thMain");
                mainTimer.schedule(new mainTimerTask(), 2000, gDatos.getServiceInfo().getTxpMain());
                logger.info("Agendando mainTimerTask cada "+gDatos.getServiceInfo().getTxpMain()+ " segundos...");
            } else {
                logger.error("Error leyendo archivo de parametros");
            }
        } catch (Exception e) {
            logger.error("Error General: "+e.getMessage());
        }
    }
    
    static class mainTimerTask extends TimerTask {
        
        //Declare los Thread de cada proceso
        //
        Thread thRunProc = new thRunProcess(gDatos);
        Thread thSocket = new thServerSocket(gDatos);
        Thread thKeep; // = new thKeepAliveSocket(gDatos);
        
        //Constructor de la clase
        public mainTimerTask() {
        }
        
        @Override
        public void run() {
            logger.info("Iniciando mainTimerTask.: "+gSub.getDateNow("yyyy-MM-dd HH:mm:ss"));
            
            /*
            Buscando Thread Activos
            */
                boolean thServerFound=false;
                boolean thKeepFound= false;
                boolean thSubRunFound = false;
                //Thread tr = Thread.currentThread();
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                //System.out.println("TOTAL de Threads del Servicio: "+threadSet.size());
                for ( Thread t : threadSet){
                    //System.out.println("Thread :"+t+":"+"state:"+t.getState());
                    if (t.getName().equals("thServerSocket")) {
                        thServerFound=true;
                    }
                    if (t.getName().equals("thKeepAlive")) {
                        thKeepFound=true;
                    }
                    if (t.getName().equals("thSubRunProcess")) {
                        thSubRunFound=true;
                    }
                }
                //Resulta Busqueda
                if (!thServerFound) {
                    gDatos.getServiceStatus().setIsSocketServerActive(false);
                } else {
                    gDatos.getServiceStatus().setIsSocketServerActive(true);
                }
                
                if (!thKeepFound) {
                    gDatos.getServiceStatus().setIsKeepAliveActive(false);
                } else {
                    gDatos.getServiceStatus().setIsKeepAliveActive(true);
                }

                if (!thSubRunFound) {
                    gDatos.getServiceStatus().setIsSubRunProcActive(false);
                } else {
                    gDatos.getServiceStatus().setIsSubRunProcActive(true);
                }
            
            /*
            Se aplicara validacion modular de procesos, ya que se encuentran en un bucle infinito.
            */
            
            //Levanta Socket Server
            //
            try {
                if (!gDatos.getServiceStatus().isIsSocketServerActive()) {
                    thSocket.setName("thServerSocket");
                    gDatos.getServiceStatus().setIsSocketServerActive(true);
                    thSocket.start();
                    logger.info(" Levantando thSocket Server....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServiceStatus().setIsSocketServerActive(false);
                //System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido Levantar socket server "+ thSocket.getName()+" "+e.getMessage());
            }
            
            //Levanta KeepAlive
            //
            try {
                if (!gDatos.getServiceStatus().isIsKeepAliveActive()) {
                    thKeep = new thKeepAliveSocket(gDatos);
                    thKeep.setName("thKeepAlive");
                    gDatos.getServiceStatus().setIsKeepAliveActive(true);
                    logger.info(" Levantando thread KeepAlive....normal...");
                    thKeep.start();
                } 
            } catch (Exception e) {
                gDatos.getServiceStatus().setIsKeepAliveActive(false);
                //System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido Levantar thread: "+ thKeep.getName()+ " "+e.getMessage());
            }

            /**
             * Solo ejecuta Thread de runProcess y de ETL si hay asignados procesos al servicio
             */
            if (gDatos.getServiceStatus().isIsAssignedTypeProc()) {
                //Levanta thRunProcess Monitorearo por los Subprocesos del TimerTask
                //TimerTask: thSubRunProcess
                //Al Agendar el Thread principal Muere y queda solo el Hijo.
                try {
                    if (!gDatos.getServiceStatus().isIsSubRunProcActive()) {
                        thRunProc.setName("thRunProcess");
                        gDatos.getServiceStatus().setIsSubRunProcActive(true);
                        logger.info("Agendando thread RunProcess....normal...");
                        thRunProc.start();
                    } 
                } catch (Exception e) {
                    gDatos.getServiceStatus().setIsSubRunProcActive(false);
                    //System.out.println("Error. "+e.getMessage());
                    logger.error("no se ha podido Agendar thread: "+ thRunProc.getName()+ " "+e.getMessage());
                }
            } else {
                logger.warn("Aun no hay procesos asignados al servicio.");
            }
            
            logger.info("Finalizando mainTimerTask.: "+gSub.getDateNow("yyyy-MM-dd HH:mm:ss"));
        }
    }
}