/*
 * To change this license header, choose License Headers in Project Properties.    
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;
import utilities.globalAreaData;
import java.io.* ; 
import java.net.* ;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thMonitorSocket extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static boolean isSocketActive;
    static String errNum;
    static String errDesc;
    
    //Carga constructor para inicializar los datos
    public thMonitorSocket(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        isSocketActive  = true;
    }
    
    @Override
    public void run() {
        try {
            gSub.sysOutln("Starting Listener Thread Monitor Server port: " + gDatos.getSrvPort());
            ServerSocket skServidor = new ServerSocket(Integer.valueOf(gDatos.getSrvPort()));
            String inputData = "";
            String outputData = "";
            String request = "";
            String auth;
            
            while (isSocketActive) {
                Socket skCliente = skServidor.accept();
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream( inpStr );
                
                //Espera Entrada
                //
                try {
                    inputData  = dataInput.readUTF();
                    //gSub.sysOutln(inputData);
                    
                    if (gDatos.isSrvActive()) {
                        //Analiza Respuesta
                        
                        auth = gSub.getAuthData (inputData);
                        gSub.sysOutln("auth: "+auth);
                        gSub.sysOutln("gauth: "+gDatos.getAuthKey());
                        
                        if (auth.equals(gDatos.getAuthKey())) {
                            request = gSub.getRequest(inputData);

                            switch (request) {
                                case "keepAlive":
                                    outputData = gSub.updateStatusServices(inputData);
                                    break;
                                case "getDate":
                                    outputData = gSub.sendDate();
                                    break;
                                case "getStatus":
                                    outputData = gSub.getStatusServices();
                                    break;
                                default:
                                    outputData = "{unknow request}";
                            }
                        } else {
                            outputData = gSub.sendError(60);
                        }
                        
                        System.out.println(inputData);
                    } else {
                        System.out.println("servico offline para realizar acciones");
                        outputData = gSub.sendError(80);
                    }
                                        

                } catch (Exception e) {
                    outputData = gSub.sendError(90);
                }
                     
                //Envia Respuesta
                //
                OutputStream outStr = skCliente.getOutputStream();
                DataOutputStream dataOutput = new DataOutputStream(outStr);
                System.out.println(outputData);
                if (outputData==null) {
                    dataOutput.writeUTF("{}");
                } else {
                    dataOutput.writeUTF(outputData);
                }
                
                //Cierra Todas las conexiones
                //
                inpStr.close();
                dataInput.close();
                skCliente.close();
            }
        
        } catch (NumberFormatException | IOException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
}
