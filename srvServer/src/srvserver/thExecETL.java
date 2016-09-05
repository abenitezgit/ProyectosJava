/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import dataClass.PoolProcess;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import utilities.dataAccess;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thExecETL extends Thread{
    PoolProcess pool = new PoolProcess();
    static srvRutinas gSub;
    static globalAreaData gDatos;
    Logger logger = Logger.getLogger("thExecETL");
    
    public thExecETL(globalAreaData m, PoolProcess poolProcess) {
        this.pool = poolProcess;
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
    @Override
    public void run() {
        logger.info("Ejecutando ETL:"+pool.getProcID()+" "+pool.getIntervalID()+"  en 20 segundos mas");
        Timer t1 = new Timer();
       
        t1.schedule(new task(), 20000);
       
    
    }
    
    class task extends TimerTask {
    
        
        @Override
        public void run() {
            logger.info("Iniciando ejecución ETL:"+pool.getProcID()+" "+pool.getIntervalID());
            
            if (isValidDataParam()) {
                dataAccess sConn = new dataAccess(gDatos);
                sConn.setDbType((String) pool.getParams().get("sDBType"));
                sConn.setDbHost((String) pool.getParams().get("sIP"));
                sConn.setDbPort((String) pool.getParams().get("sDBPort"));
                sConn.setDbName((String) pool.getParams().get("sDbName"));
                sConn.setDbUser((String) pool.getParams().get("sUserName"));
                sConn.setDbPass((String) pool.getParams().get("sUserPass"));
                
                sConn.conectar();
                
                if (sConn.isConnected()) {
                    System.out.println("Connected!!");
                } else {
                    System.out.println("NO Connected!!");
                }
                
                gDatos.setFinishedPoolProcess(pool, "Success");
            } else {
                logger.error("Error en lectura de parámetros de entrada.");
                gDatos.setFinishedPoolProcess(pool, "Error");
            }
            
            logger.info("Finalizando ejecución ETL:"+pool.getProcID()+" "+pool.getIntervalID());

        }
        
        public boolean isValidDataParam() {
            boolean isValid = true;
            
            try {
                if (pool.getGrpID()==null || pool.getProcID()==null || pool.getIntervalID()==null || pool.getNumSecExec()==null ||
                    pool.getParams()==null) {
                    isValid = false;
                }
                return isValid;
            } catch (Exception e) {
                return false;
            }
        }
        
        
    }
}
