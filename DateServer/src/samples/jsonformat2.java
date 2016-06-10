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
public class jsonformat2 {
    
    
    public static void main(String args[]) {
       
        String cadena = "{\"keepAlive\":[{\"srvPort\":\"9090\",\"numProcMax\":\"10\",\"numProcExec\":\"0\",\"srvName\":\"srv00001\",\"srvStart\":\"2016-06-08 17:11:22\",\"cmd\":\"update\",\"numTotalExec\":\"0\"}]}";
        
        String cadena2 = "{“request”:”keepAlive”,”auth”:”qwerty0987”,”params”:[{“srvName”:”srv00001”,”srvStatus”:”Waiting”},{“srvName”:”srv00002”,”srvStatus”:”Waiting”}]}";
        
        JSONObject ds = new JSONObject(cadena2);
        
        System.out.println(ds.length());
        
        
        System.out.println("request: " + ds.get("request").toString());
        System.out.println("auth: " + ds.get("auth").toString());
        System.out.println("params: " + ds.get("params").toString());
        
        
        /*
        
        JSONArray lista = ds.getJSONArray("keepAlive");
        
        JSONObject row = lista.getJSONObject(0);
        
        
        System.out.println(ds.toString());
        System.out.println(lista.toString());
        System.out.println(row.toString());
        
        System.out.println(row.get("srvName"));
*/
    }
    
}
