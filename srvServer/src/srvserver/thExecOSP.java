/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thExecOSP extends Thread{
    static srvRutinas gSub;
    static globalAreaData gDatos;
    private JSONObject params = new JSONObject();
    private String procID = null;
    
    public thExecOSP(globalAreaData m, JSONObject rs) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        this.params = rs;
    }
    
    @Override
    public void run() {
        System.out.println("Ejecutando OSP");
        
        //Recuperando los parametros de entrada
        String hostName;
        String ospName;

        Timer t1 = new Timer();
        t1.schedule(new task(), 20000);
       
    
    }
    
    class task extends TimerTask {
    
        
        @Override
        public void run() {
            System.out.println("task executed...");
            
            //Actualiza estado de termino del proceso
            //
            
            

        }
    }
    
    //Rutinas internas de Control
    //
    
    
    
}
