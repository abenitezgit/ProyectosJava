/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;
import utilities.globalAreaData;
import java.io.* ; 
import java.net.* ;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thKeepAliveServices extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static String CLASS_NAME = "thKeepAliveSocket";
    
    //Carga constructor para inicializar los datos
    public thKeepAliveServices(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
        
    @Override
    public void run() {
        Timer timerMain = new Timer();
        timerMain.schedule(new mainKeepTask(), 1000, 10000);
    }
    
    
    static class mainKeepTask extends TimerTask {
    
        public mainKeepTask() {
        }

        @Override
        public void run() {
            /*
                Recupera los servicios registrados en lista "serviceStatus"
            */
            
            JSONObject jData;
            int numServices = gDatos.getServiceStatus().size();
            
            if (numServices>0) {
                /*
                    Para cada servicio registrado se procedera a monitoreo via socket
                    ejecutando el getStatus
                */
                for (int i=0; i<numServices; i++) {
                    jData = new JSONObject(gDatos.getServiceStatus().get(i));
                    String srvHost = jData.getString("srvHost");
                    String srvPort = jData.getString("srvPort");
                
                }
 
                
            
            } else {
                System.out.println("Warning: No hay servicios por monitorear...");
            }
            
  
            
            
            Socket skCliente;
            String response;
            String dataSend;
            try {
                skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
                OutputStream aux = skCliente.getOutputStream(); 
                DataOutputStream flujo= new DataOutputStream( aux ); 

                dataSend = gSub.sendDataKeep("keep");

                flujo.writeUTF( dataSend ); 

                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream(inpStr);
                response = dataInput.readUTF();

                JSONObject jHeader = new JSONObject(response);

                try {
                    if (jHeader.getString("result").equals("keepAlive")) {
                         qData = jHeader.getJSONObject("data");
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
                    System.out.println("Error en formato de respuesta...");
                }

                //Analiza Respuesta
                dataInput.close();
                inpStr.close();
                flujo.close();
                aux.close();
                skCliente.close();
            }
            catch (NumberFormatException | IOException e) {
                gSub.sysOutln(CLASS_NAME+": Error Conectando a Socket monitor: " + e.getMessage());
                gSub.wait(CLASS_NAME, e.getMessage());
            }
        }
    }
}
