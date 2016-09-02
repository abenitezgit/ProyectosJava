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
import org.json.JSONException;
import org.json.JSONObject;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thKeepAlive extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    Logger logger = Logger.getLogger("thKeepAlive");
    
    //Carga constructor para inicializar los datos
    public thKeepAlive(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
    @Override
    public void run() {
        if (gDatos.getServiceStatus().isIsActivePrimaryMonHost()) {
            try {
                Socket skCliente = new Socket(gDatos.getServiceInfo().getSrvMonHost(), gDatos.getServiceInfo().getMonPort());
                
                OutputStream aux = skCliente.getOutputStream(); 
                DataOutputStream flujo= new DataOutputStream( aux ); 
                String dataSend = gSub.sendDataKeep("keep");
                
                logger.info("Generando (tx) hacia Server Monitor Primario: "+dataSend);
                
                flujo.writeUTF( dataSend ); 
                
                
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream(inpStr);
                String response = dataInput.readUTF();
                
                logger.info("Recibiendo (rx)...: "+response);
                JSONObject jHeader = new JSONObject(response);

                try {
                    if (jHeader.getString("result").equals("OK")) {
                        JSONObject jData = jHeader.getJSONObject("data");
                        //Como es una repsuesta no se espera retorno de error del SP
                        //el mismo lo resporta internamente si hay alguno.
                        //gSub.updateAssignedProcess(jData);
                        logger.info("Enviando a actualizar lstAssignedTypeProc...");
                        gSub.updateAssignedProcess(jData);
                        logger.info("Enviando a actualizar lstPoolProcess...");
                        gSub.updatePoolProcess(jData);
                    } else {
                        if (jHeader.getString("result").equals("error")) {
                            JSONObject jData = jHeader.getJSONObject("data");
                            System.out.println("Error result: "+jData.getInt("errCode")+ " " +jData.getString("errMesg"));
                        }
                    }
                    gDatos.getServiceStatus().setIsConnectMonHost(true);
                } catch (JSONException e) {
                    logger.error("Error en formato de respuesta");
                }
            } catch (NumberFormatException | IOException e) {
                gDatos.getServiceStatus().setIsActivePrimaryMonHost(false);
                gDatos.getServiceStatus().setIsConnectMonHost(false);
                logger.error(" Error conexion a server de monitoreo primary...."+ e.getMessage());
            }

        } else {
            //
            //Valida conexion a server secundario Backup
            //
            try {
                Socket skCliente = new Socket(gDatos.getServiceInfo().getSrvMonHostBack(), gDatos.getServiceInfo().getMonPortBack());
                
                OutputStream aux = skCliente.getOutputStream(); 
                DataOutputStream flujo= new DataOutputStream( aux ); 
                String dataSend = gSub.sendDataKeep("keep");
                
                logger.info("Generando (tx) hacia Server Monitor Secundario: "+dataSend);
                
                flujo.writeUTF( dataSend ); 
                
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream(inpStr);
                String response = dataInput.readUTF();
                
                logger.info("Recibiendo (rx)...: "+response);
                JSONObject jHeader = new JSONObject(response);

                try {
                    if (jHeader.getString("result").equals("OK")) {
                        JSONObject jData = jHeader.getJSONObject("data");
                        //Como es una repsuesta no se espera retorno de error del SP
                        //el mismo lo resporta internamente si hay alguno.
                        //gSub.updateAssignedProcess(jData);
                        logger.info("Enviando a actualizar lstAssignedTypeProc...");
                        gSub.updateAssignedProcess(jData);
                        logger.info("Enviando a actualizar lstPoolProcess...");
                        gSub.updatePoolProcess(jData);
                    } else {
                        if (jHeader.getString("result").equals("error")) {
                            JSONObject jData = jHeader.getJSONObject("data");
                            System.out.println("Error result: "+jData.getInt("errCode")+ " " +jData.getString("errMesg"));
                        }
                    }
                    gDatos.getServiceStatus().setIsConnectMonHost(true);
                } catch (Exception e) {
                    logger.error("Error en formato de respuesta");
                }
            } catch (NumberFormatException | IOException e) {
                gDatos.getServiceStatus().setIsActivePrimaryMonHost(true);
                gDatos.getServiceStatus().setIsConnectMonHost(false);
                logger.error(" Error conexion a server de monitoreo backup...."+ e.getMessage());
            }
        }
    }
}
