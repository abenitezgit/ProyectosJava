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
    static String CLASS_NAME="thMonitorSocket";
    
    //Carga constructor para inicializar los datos
    public thMonitorSocket(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        isSocketActive  = true;
    }
    
    @Override
    public void run() {
        try {
            gSub.sysOutln(CLASS_NAME+": Starting Listener Thread Monitor Server port: " + gDatos.getSrvPort());
            ServerSocket skServidor = new ServerSocket(Integer.valueOf(gDatos.getSrvPort()));
            String inputData;
            String outputData;
            String dRequest;
            String dAuth;
            JSONObject jHeader;
            JSONObject jData;
            int result;
            
            while (isSocketActive) {
                Socket skCliente = skServidor.accept();
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream( inpStr );
                
                //Espera Entrada
                //
                try {
                    inputData  = dataInput.readUTF();
                    gSub.sysOutln(CLASS_NAME+": Recibiendo TX: "+inputData);
                    
                    jHeader = new JSONObject(inputData);
                    jData = jHeader.getJSONObject("data");
                    
                    dAuth = jHeader.getString("auth");
                    dRequest = jHeader.getString("request");

                    if (dAuth.equals(gDatos.getAuthKey())) {

                        switch (dRequest) {
                            case "keepAlive":
                                //gSub.sysOutln(CLASS_NAME+": ejecutando ... updateStatusServices: "+ rs.getJSONObject("params"));
                                result = gSub.updateStatusServices(jData);
                                if (result==0) {
                                    outputData = gSub.sendAssignedProc(jData.getString("srvName"));
                                } else {
                                    outputData = gSub.sendError(10);
                                }
                                break;
                            case "getDate":
                                outputData = gSub.sendDate();
                                break;
                            case "getStatus":
                                gSub.sysOutln(CLASS_NAME+": ejecutando ... getStatusServices");
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
                } catch (IOException | JSONException e) {
                    outputData = gSub.sendError(90);
                }
                     
                //Envia Respuesta
                //
                OutputStream outStr = skCliente.getOutputStream();
                DataOutputStream dataOutput = new DataOutputStream(outStr);
                gSub.sysOutln(CLASS_NAME+": Enviando respuesta: "+ outputData);
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
