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
        Thread thOSP;
        Thread thLOR;
        Thread thMOV;
        Thread thOTX;
        Thread thFTP;
        Thread thETL;
        
        
        //Constructor de la clase
        public mainTimerTask() {
        }
        
        @Override
        public void run() {
            logger.info(" Iniciando mainTimerTask....");
            
            /*
            Buscando Thread Activos
            */
                boolean thServerFound=false;
                boolean thKeepFound= false;
                boolean thSubRunFound = false;
                Thread tr = Thread.currentThread();
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for ( Thread t : threadSet){
                    System.out.println("Thread :"+t+":"+"state:"+t.getState());
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
                //if (!thSocket.isAlive()) {
                    
                    thSocket.setName("thServerSocket");
                    gDatos.getServiceStatus().setIsSocketServerActive(true);
                    thSocket.start();
                    logger.info(" Iniciando thSocket Server....normal...");
                } 
            } catch (Exception e) {
                gDatos.getServiceStatus().setIsSocketServerActive(false);
                System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido levantar socket server "+ thSocket.getName());
            }
            
            //Levanta KeepAlive
            //
            try {
                if (!gDatos.getServiceStatus().isIsKeepAliveActive()) {
                //if (!thSocket.isAlive()) {
                    thKeep = new thKeepAliveSocket(gDatos);
                    thKeep.setName("thKeepAlive");
                    gDatos.getServiceStatus().setIsKeepAliveActive(true);
                    logger.info(" Iniciando thread KeepAlive....normal...");
                    thKeep.start();
                } 
            } catch (Exception e) {
                gDatos.getServiceStatus().setIsKeepAliveActive(false);
                System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido levantar thread: "+ thKeep.getName());
            }

            //Levanta thRunProcess Monitorearo por los Subprocesos del TimerTask
            //
            try {
                if (!gDatos.getServiceStatus().isIsSubRunProcActive()) {
                    thRunProc.setName("thRunProcess");
                    gDatos.getServiceStatus().setIsSubRunProcActive(true);
                    logger.info(" Iniciando thread RunProcess....normal...");
                    thRunProc.start();
                } 
            } catch (Exception e) {
                gDatos.getServiceStatus().setIsSubRunProcActive(false);
                System.out.println("Error. "+e.getMessage());
                logger.error("no se ha podido levantar thread: "+ thRunProc.getName());
            }

            
            /*
        private void ejecutaProcesos() {
            String typeProc;
            String procID;
            String status;
            JSONObject rowList;
            JSONObject params;

            //
            //    Revisa la Lista poolProcess para ver si existen registros con estado enqued
            //
            int numProc = gDatos.getPoolProcess().size();
            if (numProc>0) {
                for (int i=0;i<numProc;i++) {
                    //Lista de Procesos en pool de ejecucion
                    //
                    //Por cada proceso en pool validar si corresponde una ejecucion
                    //
                    try {
                        rowList     = gDatos.getPoolProcess().get(i);   //Lee registro del pool de procesos ingresados como un JSONObject
                        typeProc    = rowList.getString("typeProc");
                        procID      = rowList.getString("procID");
                        status      = rowList.getString("status");
                        params      = rowList.getJSONObject("params");


                        //
                        //    Solo se tomaran acciones si el estado es queued (encolado)
                        //
                        if (status.equals("queued")) {
                            //Valida si existen thread libres nivel de servicio
                            //
                            if (gDatos.isExistFreeThreadServices()) {
                                //Valida si existen thread libres del type de proceso a ejecutar
                                //
                                if (gDatos.isExistFreeThreadProcess(typeProc)) {
                                    //Actualiza Estadisticas del Proceso a ejecutar
                                    //
                                    //Actualiza status a estado Running
                                    //
                                    gDatos.getPoolProcess().get(i).put("status","Running");

                                    //Atualiza Fecha de Inicio de Ejecución 
                                    gDatos.getPoolProcess().get(i).put("startDate",gSub.getDateNow("yyyy-MM-dd HH:mm:ss"));

                                    //Actualiza Lista assignedProcess
                                    for (int j=0; j < gDatos.getAssignedTypeProc().size(); j++) {
                                        if (gDatos.getAssignedTypeProc().get(j).getString("typeProc").equals(typeProc)) {
                                            int usedThread = gDatos.getAssignedTypeProc().get(j).getInt("usedThread");
                                            usedThread++;
                                            gDatos.getAssignedTypeProc().get(j).put("usedThread", usedThread);
                                        }
                                    }

                                    //Actualiza Datos Globales
                                    gDatos.setNumProcExec(gDatos.getNumProcExec()+1);
                                    gDatos.setNumTotalExec(gDatos.getNumTotalExec()+1);

                                    switch (typeProc) {
                                        case "OSP":
                                            thOSP = new thExecOSP(gDatos, rowList);
                                            thOSP.start();
                                            break;
                                        case "OTX":
                                            thOTX = new thExecOTX(gDatos, rowList);
                                            thOTX.start();
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    logger.info(" esperando Thread libres de Procesos...");
                                }
                            } else {
                                logger.info(" esperando Thread libres de Srvicio...");
                            }
                        } else {
                            logger.info(" no hay procesos status queued...");
                        }
                    } catch (Exception e) {
                        logger.error(" Error desconocido..."+e.getMessage());
                    }
                    //gRutinas.sysOutln("in pool: "+gDatos.getPoolProcess().get(i).toString());
                }
            } else {
                logger.info(" no hay procesos en pool de ejecucion...");
            }
*/
        }
    }
}