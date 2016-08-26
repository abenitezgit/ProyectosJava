/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import utilities.globalAreaData;

/**
 *
 * @author andresbenitez
 */
public class thControlThread extends Thread{
    globalAreaData gDatos;
    static Logger logger = Logger.getLogger("thControlThread");

    public thControlThread(globalAreaData m) {
        gDatos = m;
    }
    
    @Override
    public void run() {
        Timer timerMain = new Timer("thSubControlThread");
        timerMain.schedule(new mainTask(), 1000, 1000);
        
    }
    
    class mainTask extends TimerTask{

        @Override
        public void run() {
            logger.info("revisando threads...");
        }
    }
}
