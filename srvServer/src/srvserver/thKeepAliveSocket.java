/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;
import utilities.globalAreaData;
import java.io.* ; 
import java.net.* ;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thKeepAliveSocket extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static boolean isSocketActive;
    static String errNum;
    static String errDesc;
    Logger logger = Logger.getLogger("thKeepAlive");
    
    //Carga constructor para inicializar los datos
    public thKeepAliveSocket(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        isSocketActive = true;
    }
    
    @Override
    public void run() {
        String inputData;
        String outputData;
        String request;
        String auth;

        Socket skCliente;
        String response;
        String dataSend;
        try {
            skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            
            dataSend = gSub.sendDataKeep("keep");
            
            logger.info("Enviando (tx)...: "+dataSend);
            flujo.writeUTF( dataSend ); 
            
            InputStream inpStr = skCliente.getInputStream();
            DataInputStream dataInput = new DataInputStream(inpStr);
            response = dataInput.readUTF();
            
            logger.info("Recibiendo (tx)...: "+response);
            JSONObject jHeader = new JSONObject(response);
            
            try {
                if (jHeader.getString("result").equals("OK")) {
                    JSONObject jData = jHeader.getJSONObject("data");
                    //Como es una repsuesta no se espera retorno de error del SP
                    //el mismo lo resporta internamente si hay alguno.
                    gSub.updateAssignedProcess(jData);
                } else {
                    if (jHeader.getString("result").equals("error")) {
                        JSONObject jData = jHeader.getJSONObject("data");
                        System.out.println("Error result: "+jData.getString("errNum")+ " " +jData.getString("errMesg"));
                    }
                }
            } catch (Exception e) {
                logger.error("Error en formato de respuesta");
            }

            //Analiza Respuesta
            dataInput.close();
            inpStr.close();
            flujo.close();
            aux.close();
            skCliente.close();
        }
        catch (NumberFormatException | IOException e) {
            logger.error("Error Conectando a Socket monitor: " + e.getMessage());
        }
    }
}
