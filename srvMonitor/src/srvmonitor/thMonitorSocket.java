/*
 * To change this license header, choose License Headers in Project Properties.    
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;
import utilities.globalAreaData;
import java.io.* ; 
import java.net.* ;
import org.json.JSONException;
import org.json.JSONObject;
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
            String inputData;
            String outputData;
            String request;
            String auth;
            JSONObject rs;
            
            while (isSocketActive) {
                Socket skCliente = skServidor.accept();
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream( inpStr );
                
                //Espera Entrada
                //
                try {
                    inputData  = dataInput.readUTF();
                    rs = new JSONObject(inputData);
                    //gSub.sysOutln(inputData);
                    
                    auth = rs.getString("auth");

                    if (auth.equals(gDatos.getAuthKey())) {
                        request = rs.getString("request");

                        switch (request) {
                            case "keepAlive":
                                outputData = gSub.updateStatusServices(rs.getJSONObject("params"));
                                break;
                            case "getDate":
                                outputData = gSub.sendDate();
                                break;
                            case "getStatus":
                                outputData = gSub.getStatusServices();
                                break;
                            case "putExecOSP":
                                gSub.putExecOSP(inputData);
                                outputData = gSub.sendOkTX();
                                break;
                            default:
                                outputData = gSub.sendError(99, "Error Desconocido...");
                        }
                    } else {
                        outputData = gSub.sendError(60);
                    }
                    System.out.println(inputData);
                } catch (IOException | JSONException e) {
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
