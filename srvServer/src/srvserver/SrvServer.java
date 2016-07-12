/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import utilities.globalAreaData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import static srvserver.thKeepAliveSocket.gDatos;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class SrvServer {
    static globalAreaData gDatos = new globalAreaData();
    static srvRutinas gSub ;
    static String CLASS_NAME = "srvServer";
    
    public SrvServer() {
        /*
            El constructor solo se ejecuta cuando la clase es instanciada desde otra.
            Cuando la clase posee un main() principal de ejecución, el constructor  no
            es considerado.
        */
    }   

    //Aplicacion Servidor
    //Levanta Puerto para informar status

    public static void main(String[] args) throws IOException {
        gSub = new srvRutinas(gDatos);
        gDatos.setIsRegisterService(false);

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
            
            /*
            Valida Conexion a servidor de monitoreo
            */
            gSub.sysOutln(CLASS_NAME+" Validando conexion a server de monitoreo....");
            try {
                /*
                Valida conexion a server primario
                */
                if (gDatos.isIsActivePrimaryMonHost()) {
                    try {
                        Socket skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
                        OutputStream aux = skCliente.getOutputStream(); 
                        DataOutputStream flujo= new DataOutputStream( aux ); 
                        String dataSend = gSub.sendPing();
                        flujo.writeUTF( dataSend ); 
                        InputStream inpStr = skCliente.getInputStream();
                        DataInputStream dataInput = new DataInputStream(inpStr);
                        String response = dataInput.readUTF();
                        if (response.equals("OK")) {
                            gDatos.setIsConnectMonHost(true);
                            gSub.sysOutln(CLASS_NAME+" Conectado a server monitor primary");
                        } else {
                            gDatos.setIsActivePrimaryMonHost(false);
                            gDatos.setIsConnectMonHost(false);
                            gDatos.setIsRegisterService(false);
                        }
                    } catch (NumberFormatException | IOException e) {
                        gDatos.setIsActivePrimaryMonHost(false);
                        gDatos.setIsConnectMonHost(false);
                        gDatos.setIsRegisterService(false);
                        gSub.sysOutln(CLASS_NAME+" Error conexion a server de monitoreo primary...."+ e.getMessage());
                    }
                
                } else {
                    /*
                    Valida conexion a server secundario Backup
                    */
                    try {
                        Socket skCliente = new Socket(gDatos.getSrvMonHostBack(), Integer.valueOf(gDatos.getMonPortBack()));
                        OutputStream aux = skCliente.getOutputStream(); 
                        DataOutputStream flujo= new DataOutputStream( aux ); 
                        String dataSend = gSub.sendPing();
                        flujo.writeUTF( dataSend ); 
                        InputStream inpStr = skCliente.getInputStream();
                        DataInputStream dataInput = new DataInputStream(inpStr);
                        String response = dataInput.readUTF();
                        if (response.equals("OK")) {
                            gDatos.setIsConnectMonHost(true);
                            gSub.sysOutln(CLASS_NAME+" Conectado a server monitor secundary");
                        } else {
                            gDatos.setIsActivePrimaryMonHost(true);
                            gDatos.setIsConnectMonHost(false);
                            gDatos.setIsRegisterService(false);
                        }
                    } catch (NumberFormatException | IOException e) {
                        gDatos.setIsActivePrimaryMonHost(true);
                        gDatos.setIsConnectMonHost(false);
                        gDatos.setIsRegisterService(false);
                        gSub.sysOutln(CLASS_NAME+" Error conexion a server de monitoreo backup...."+ e.getMessage());
                    }
                
                }
            } catch (Exception e) {
                gDatos.setIsConnectMonHost(false);
                gDatos.setIsRegisterService(false);
                gSub.sysOutln(CLASS_NAME+" Error general conexion a server de monitoreo...."+ e.getMessage());
            }
            
            //Registra Startup del Servicio en serverMonitor
            if (gDatos.isIsConnectMonHost()) {
                if (!gDatos.isIsRegisterService()) {
                    int result = gSub.sendRegisterService();
                    if (result==0) {
                        System.out.println("servicio registrado");
                        gDatos.setIsRegisterService(true);
                    } else {
                        System.out.println("Error: servicio no ha podido registrarse");
                    }
                } else {
                    System.out.println("Servicio ya está registrado...");
                }
            }
            
            
            //Control Principal de Ejecucion de Procesos
            if (gDatos.isSrvActive() && gDatos.isIsRegisterService()) {    //Control de Revision de Procesos
                System.out.println(CLASS_NAME+" servicio activo...");
                /*
                    Revisa la Lista poolProcess para ver si existen registros con estado enqued
                */
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

                            
                            /*
                                Solo se tomaran acciones si el estado es queued (encolado)
                            */
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
                System.out.println(CLASS_NAME+" Servicio no esta activo...");
            }
        }
    }
}
