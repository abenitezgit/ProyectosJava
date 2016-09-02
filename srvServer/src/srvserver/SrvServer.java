/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import utilities.globalAreaData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    static globalAreaData gDatos;
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
            //Instancia las Clases
            logger.info("Iniciando srvServer...");
            gDatos = new globalAreaData();
            gSub = new srvRutinas(gDatos);

            if (gDatos.getServiceStatus().isIsLoadParam()) {
                if (gDatos.getServiceStatus().isIsLoadRutinas()) {
                    Timer mainTimer = new Timer("thMain");
                    mainTimer.schedule(new mainTimerTask(), 2000, gDatos.getServiceInfo().getTxpMain());
                    logger.info("Agendando mainTimerTask cada "+gDatos.getServiceInfo().getTxpMain()+ " segundos...");
                } else {
                    logger.error("Error cargando Rutinas...abortando modulo.");
                }
            } else {
                logger.error("Error cargando globalAreaData...abortando modulo.");
            }
        } catch (Exception e) {
            logger.error("Error en modulo principal: "+e.getMessage());
        }
    }
    
    static class mainTimerTask extends TimerTask {
        
        //Declare los Thread de cada proceso
        //
        Thread thRunProc = new thRunProcess(gDatos);
        Thread thSocket = new thServerSocket(gDatos);
        Thread thKeep; // = new thKeepAlive(gDatos);
        
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
            List<String> lstThreadActivos = new ArrayList<>();
            lstThreadActivos.clear();

            logger.info("Revisando Threads de Modulos Activos...");
            //Thread tr = Thread.currentThread();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            //System.out.println("TOTAL de Threads del Servicio: "+threadSet.size());
            for ( Thread t : threadSet){
                //System.out.println("Thread :"+t+":"+"state:"+t.getState());
                if (t.getName().equals("thServerSocket")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thServerFound=true;
                }
                if (t.getName().equals("thKeepAlive")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
                    thKeepFound=true;
                }
                if (t.getName().equals("thSubRunProcess")) {
                    lstThreadActivos.add(t.getName()+" "+t.getId()+ " "+t.getPriority()+" "+t.getState().toString());
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
                    thKeep = new thKeepAlive(gDatos);
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
                logger.warn("Aun no hay tipos de procesos asignados al servicio.");
            }
            
            logger.info("Finalizando mainTimerTask.: "+gSub.getDateNow("yyyy-MM-dd HH:mm:ss"));
        }
    }
}