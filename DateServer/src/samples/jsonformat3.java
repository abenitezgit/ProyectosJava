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
public class jsonformat3 {
    
    public static void print(Object obj) {
        System.out.println(obj);
    }
 
    public static void main(String args[]) {
    
    
            
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainjo = new JSONObject();

        
        jo.put("srvName","srv00001");
        jo.put("srvStatus","Waiting");
        
        ja.put(jo);

        jo = new JSONObject();
        jo.put("srvName","srv00002");
        jo.put("srvStatus","Pending");
        
        ja.put(jo);

        mainjo.put("params", ja);
        mainjo.put("auth", "qwerty0987");
        mainjo.put("request", "keepAlive");
        
        //mainjo.put("auth", "qwerty0987");
        
        System.out.println(mainjo.toString());
        
        JSONObject ds = new JSONObject(mainjo.toString());
        print(ds.get("request"));
        
    }
    
}
