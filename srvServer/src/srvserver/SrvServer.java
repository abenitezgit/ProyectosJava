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
import org.json.JSONObject;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class SrvServer {
    static globalAreaData gDatos = new globalAreaData();
    static srvRutinas gSub = new srvRutinas(gDatos);
    static String CLASS_NAME = "srvServer";
    
    public SrvServer() throws IOException {
        //gRutinas = new srvRutinas(gDatos);
    }   

    //Aplicacion Servidor
    //Levanta Puerto para informar status

    public static void main(String[] args) throws IOException {
        if (gDatos.isSrvLoadParam()) {
            Timer mainTimer = new Timer();
            mainTimer.schedule(new mainTimerTask(), 2000, Integer.valueOf(gDatos.getTxpMain()));
        } else {
            System.out.println(CLASS_NAME+" Error leyendo archivo de parametros");
            
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
        }
        
        
        @Override
        public void run() {
            String typeProc;
            String procID;
            String status;
            JSONObject rowList;
            JSONObject params;
            System.out.println(CLASS_NAME+" Iniciando mainTimerTask....");
            
            //Monitor KeepAlive
            try {
                if (!thKeep.isAlive()) {
                    if (gDatos.isSrvActive()) {
                        thKeep.start();
                        System.out.println(CLASS_NAME+" Iniciando ThreadKeep....normal...");
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
                    thKeep = new thKeepAliveSocket(gDatos);
                    thKeep.start();
                    System.out.println(CLASS_NAME+" Iniciando ThreadKeep....forced...");
                }
            }
            
            //Monitor thSocket Server
            //
            try {
                if (!thSocket.isAlive()) {
                    if (gDatos.isSrvActive()) {
                        thSocket.start();
                        System.out.println(CLASS_NAME+" Iniciando thSocket Server....normal...");
                    }
                } else {
                    if (!gDatos.isSrvActive()) {
                        thSocket.interrupt();
                        System.out.println("thSocket interrupted");
                    }
                }
            } catch (Exception e) {
                if (gDatos.isSrvActive()) {
                    thSocket = new thServerSocket(gDatos);
                    thSocket.start();
                    System.out.println(CLASS_NAME+" Iniciando thSocket Server....forced...");
                }
            }
            
            //Control Princial de Ejecucion de Procesos
            if (gDatos.isSrvActive()) {    //Control de Revision de Procesos
                System.out.println(CLASS_NAME+" servicio activo...");
                if (gDatos.isSrvGetTypeProc()) { //Valida si ya recibio los parametros globales de ejecucion
                    System.out.println(CLASS_NAME+" lista tyeProc recibida...");
                    int numProc = gDatos.getPoolProcess().size();
                    //gRutinas.sysOutln("numproc: "+numProc);
                    if (numProc>0) {
                    
                        for (int i=0;i<numProc;i++) {
                            //Lista de Procesos en pool de ejecucion
                            //
                            //Por cada proceso en pool validar si corresponde una ejecucion
                            //
                            try {
                                rowList = gDatos.getPoolProcess().get(i);   //Lee registro del pool de procesos ingresados

                                typeProc = rowList.getString("typeProc");
                                procID = rowList.getString("procID");
                                status = rowList.getString("status");
                                params = rowList.getJSONObject("params");

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
                                            gDatos.getPoolProcess().get(i).put("startDate",gSub.getDateNow("yyyy-MM-dd HH:MI:SS"));

                                            //Actualiza Lista assignedProcess
                                            for (int j=0; j < gDatos.getAssignedTypeProc().size(); j++) {
                                                if (gDatos.getAssignedTypeProc().get(i).getString("typeProc").equals(typeProc)) {
                                                    int usedThread = gDatos.getAssignedTypeProc().get(i).getInt("usedThread");
                                                    usedThread++;
                                                    gDatos.getAssignedTypeProc().get(i).put("usedThread", usedThread);
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
                                            System.out.println(CLASS_NAME+" esperando Thread libres de Procesos...");
                                        }
                                    } else {
                                        System.out.println(CLASS_NAME+" esperando Thread libres de Srvicio...");
                                    }
                                } else {
                                    System.out.println(CLASS_NAME+" no hay procesos status queued...");
                                }
                            } catch (Exception e) {
                                System.out.println(CLASS_NAME+" Error desconocido..."+e.getMessage());
                            }
                            //gRutinas.sysOutln("in pool: "+gDatos.getPoolProcess().get(i).toString());
                        }
                    } else {
                        System.out.println(CLASS_NAME+" no hay procesos en pool de ejecucion...");
                    }
                } else {
                    System.out.println(CLASS_NAME+" no se han recibido parametros del proceso...");
                }
            } else {
                System.out.println(CLASS_NAME+" Servicio no esta activo...");
            }
        }
    }
}
