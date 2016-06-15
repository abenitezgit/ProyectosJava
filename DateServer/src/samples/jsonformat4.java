/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class jsonformat4 {
    
    
    public static void main(String args[]) {
            JSONObject subjo = new JSONObject();
            JSONArray subja = new JSONArray();
            JSONObject submainjo = new JSONObject();
        
        
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();
            
            subjo.put("procType", "ETL");
            subjo.put("threadActive", 3);
            subjo.put("threadMax", 20);
            
            subja.put(subjo);
            jo.put("process",subja);
                

            jo.put("srvName", "srv00001");
            jo.put("srvPort", "1010");
            jo.put("numTotalExec", "10");
            jo.put("numProcMax", "10");
            jo.put("numProcExec", "5");
            jo.put("srvStart", "2015");

            ja.put(jo);

            mainjo.put("params", ja);
            mainjo.put("auth", "123");
            mainjo.put("request", "keepAlive");
            
            System.out.println(mainjo.toString());
    
    }
}
