/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;
import utilities.globalAreaData;
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
        Thread tr = Thread.currentThread();
        System.out.println("Current Thread KeepAlive: "+tr.getName()+ " ID: "+tr.getId());
        
        
        Timer timerMain = new Timer("thSubKeep");
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
            int numServices = gDatos.getLstServiceStatus().size();
            System.out.println("Inicia thKeepAlive");
            
            if (numServices>0) {
                /*
                    Para cada servicio registrado se procedera a monitoreo via socket
                    ejecutando el getStatus
                */
                for (int i=0; i<numServices; i++) {
                    jData = new JSONObject(gDatos.getLstServiceStatus().get(i));
                    //String srvHost = jData.getString("srvHost");
                    //String srvPort = jData.getString("srvPort");
                
                }
            } else {
                System.out.println("Warning: No hay servicios por monitorear...");
            }
            
            Socket skCliente;
            String response;
            String dataSend;
        }
    }
}
