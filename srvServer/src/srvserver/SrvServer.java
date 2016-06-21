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
    static srvRutinas gRutinas = new srvRutinas(gDatos);
    
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
            String typeProc;
            String procID;
            String status;
            JSONObject rowList;
            JSONObject params;
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
                                            gDatos.getPoolProcess().get(i).put("startDate",gRutinas.getDateNow("yyyy-MM-dd HH:MI:SS"));

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
                                            
                                            thOSP = new thExecOSP(gDatos, rowList);
                                            thOSP.start();
                                        } else {
                                            System.out.println("esperando por threads libres del proceso");
                                        }
                                    } else {
                                        System.out.println("Esperando por thread libres del servicio");
                                    }
                                } else {
                                    System.out.println("no hay procesos queued..");
                                }
                            } catch (Exception e) {
                                System.out.println("error desconocido ejecutando proceso: "+e.getMessage());
                            }
                            //gRutinas.sysOutln("in pool: "+gDatos.getPoolProcess().get(i).toString());
                        }
                    } else {
                        System.out.println("no hay procesos en pool de ejecicion..");
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
