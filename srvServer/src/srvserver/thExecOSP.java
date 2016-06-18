/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import java.util.Timer;
import java.util.TimerTask;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thExecOSP extends Thread{
    static srvRutinas gSub;
    static globalAreaData gDatos;
    
    public thExecOSP(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        

    }
    
    @Override
    public void run() {
        System.out.println("Ejecutando OSP");
        Timer t1 = new Timer();
       
        t1.schedule(new task(), 30000);
       
    
    }
    
    class task extends TimerTask {
    
        
        @Override
        public void run() {
            System.out.println("task executed...");
        }
    }
    
    //Rutinas internas de Control
    //
    
    
    
}
