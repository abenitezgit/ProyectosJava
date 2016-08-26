/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import dataClass.PoolProcess;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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
            logger.info("ETL:"+pool.getProcID()+" "+pool.getIntervalID()+" executed...");
            
            gDatos.updateStatusPoolProcess("ETL", pool.getProcID(), "Finished", pool.getIntervalID());
            
            //Actualiza estado de termino del proceso
            //

        }
    }
}
