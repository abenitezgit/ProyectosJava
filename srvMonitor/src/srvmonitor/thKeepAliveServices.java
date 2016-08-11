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
import org.apache.log4j.Logger;
import org.json.JSONObject;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thKeepAliveServices extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static Logger logger = Logger.getLogger("thKeepAliveServices");
    
    //Carga constructor para inicializar los datos
    public thKeepAliveServices(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
        
    @Override
    public void run() {
        Timer timerMain = new Timer("thSubKeep");
        timerMain.schedule(new mainKeepTask(), 1000, 10000);
        logger.info("Se ha agendado thKeepAlive cada 10 segundos");
    }
    
    
    static class mainKeepTask extends TimerTask {
    
        public mainKeepTask() {
        }

        @Override
        public void run() {
            /**
             * Valida Conexion a MetaData
             */
            try {
                MetaData metadata = new MetaData(gDatos);
                if (gDatos.getServerStatus().isIsValMetadataConnect()) {
                    metadata.closeConnection();
                }
            } catch (Exception e) {
                logger.error("No se ha podido validar conexion a MetaData.."+e.getMessage());
            }
            
            
            /*
                Recupera los servicios registrados en lista "serviceStatus"
            */
            
            JSONObject jData;
            int numServices = gDatos.getLstServiceStatus().size();
            logger.info("Inicia thKeepAlive...");
            
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
                logger.warn("Aun no hay servicios registrados para monitorear...");
            }
            
            Socket skCliente;
            String response;
            String dataSend;
            logger.info("Finaliza thKeepAlive...");
        }
    }
}
