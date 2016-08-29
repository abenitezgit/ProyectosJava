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
public class thExecFTP extends Thread{
    PoolProcess pool = new PoolProcess();
    static srvRutinas gSub;
    static globalAreaData gDatos;
    Logger logger = Logger.getLogger("thExecETL");
    
    public thExecFTP(globalAreaData m, PoolProcess pool) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
    @Override
    public void run() {
        logger.info("Iniciando Proceso FTP");
        
        
       
    
    }
        
    
    
}
