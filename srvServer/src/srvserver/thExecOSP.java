/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;

import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thExecOSP extends Thread{
    static srvRutinas gRutinas;
    static globalAreaData gDatos;
    
    public thExecOSP(globalAreaData m) {
        gDatos = m;
        gRutinas = new srvRutinas(gDatos);
        

    }
    
        @Override
    public void run() {
        System.out.println("Ejecutando OSP");
        
       
    
    }
    
}
