/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;
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
public class thServerSocket extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static boolean isSocketActive = true;
    static String errNum;
    static String errDesc;
    static String CLASS_NAME = "thServerSocket";
    
    //Carga constructor para inicializar los datos
    public thServerSocket(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
            
    @Override
    public void run() {
        try {
            gSub.sysOutln("Starting Listener Thread Service Server port: " + gDatos.getSrvPort());
            String inputData;
            String outputData;
            String dRequest;
            String dAuth;
            JSONObject jHeader;
            JSONObject jData;
            
            ServerSocket skServidor = new ServerSocket(Integer.valueOf(gDatos.getSrvPort()));
            
            while (isSocketActive) {
                Socket skCliente = skServidor.accept();
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream( inpStr );
                
                //Recibe Data Input (request)
                //
                //Espera Entrada
                //
                try {
                    inputData  = dataInput.readUTF();
                    System.out.println(CLASS_NAME+": Recibiendo TX: "+ inputData);
                    //gSub.sysOutln(inputData);
                    
                    jHeader = new JSONObject(inputData);
                    jData = jHeader.getJSONObject("data");
                    
                    dAuth = jHeader.getString("auth");
                    dRequest = jHeader.getString("request");
                    
                    if (dAuth.equals(gDatos.getAuthKey())) {

                        switch (dRequest) {
                            case "getStatus":
                                outputData = gSub.sendDataKeep("request");
                                break;
                            case "getDate":
                                outputData = gSub.sendDate();
                                break;
                            case "updateAssignedProc":
                                gSub.updateAssignedProcess(jData);
                                outputData = gSub.sendOkTX();
                                break;
                            case "executeProcess":
                                //json format:
                                // {
                                //   "request":"executeProcess",
                                //   "auth":"qwerty0987",
                                //   "typeProc":"OSP",
                                //   "procID":"OSP00001",
                                //   "sendDate":"2016-10-02 10:09:10",
                                //   "params":
                                //      {
                                //        <dependera del tipo de proceso>
                                //      }
                                // }
                                System.out.println(CLASS_NAME+": ejecutando ...enqueProcess..: "+ inputData);
                                int result = gSub.enqueProcess(jData);
                                if (result==0) {
                                    outputData = gSub.sendOkTX();
                                } else {
                                    if (result==2) {
                                        outputData = gSub.sendError(99, "Proceso ya se encuntra encolado...");
                                    } else {
                                        outputData = gSub.sendError(99, "Error encolando proceso...");
                                    }
                                }
                                break;
                            case "getPoolProcess":
                                outputData = gSub.sendPoolProcess();
                                break;
                            case "getList":
                                outputData = gSub.sendList(jData);
                                break;
                            default:
                                outputData = gSub.sendError(99,"Error desconocido..");
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
                
                System.out.println(CLASS_NAME+": Enviando respuesta: "+ outputData);
                
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
            System.out.println("thSocket error: "+e.getMessage());
        }
    }
}
