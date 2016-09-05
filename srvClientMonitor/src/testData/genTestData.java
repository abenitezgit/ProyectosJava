/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testData;

import DataClass.PoolProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import srvclientmonitor.globalAreaData;
import srvclientmonitor.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class genTestData {
    static globalAreaData gDatos = new globalAreaData();
    static srvRutinas gSub = new srvRutinas(gDatos);
    
    public static void main(String args[]) throws IOException {
        PoolProcess pool = new PoolProcess();
        Map<Object, Object> param = new HashMap<Object, Object>();
        List<PoolProcess> lstPoolProcess = new ArrayList<>();
        
        pool.setGrpID("GRP00001");
        pool.setInsTime("2016-08-29 10:10:00");
        pool.setNumSecExec("201608201010");
        pool.setProcID("OSP00001");
        pool.setStatus("initial");
        pool.setTypeProc("OSP");
        
        param.put("dbSource", "oratest");
        param.put("dbDest", "oratest");
        
        pool.setParams(param);
        
        lstPoolProcess.add(pool);
        
        System.out.println("pool: " + gSub.serializeObjectToJSon(lstPoolProcess, false));
        
        PoolProcess newPool = new PoolProcess();
        
        newPool = (PoolProcess) gSub.serializeJSonStringToObject(gSub.serializeObjectToJSon(lstPoolProcess.get(0), false), PoolProcess.class);
        
        
        System.out.println("newpool: " + gSub.serializeObjectToJSon(newPool, true));
        
        
        
        
        
    
    
    }
}
