/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class jsonformat5 {
    
    public static void main(String args[]) {
        
        String jsonSource = "{\"request\":\"keepAlive\",\"auth\":\"qwerty0987\",\"params\":[{\"srvPort\":\"9090\",\"process\":[],\"numProcMax\":\"20\",\"numProcExec\":\"0\",\"srvName\":\"srv00001\",\"srvStart\":\"2016-06-14 12:53:03\",\"numTotalExec\":\"0\",\"isgetTypeProc\":false}]}";
        String jsonMatriz = "request";
        String jsonParam = "color";
        String jsonValor = "rojo";

        String out = jsonAddObject(jsonSource, jsonMatriz, jsonParam, jsonValor, 0);
        
        System.out.println(out);
        
    }
    

    static String jsonAddObject (String jsonSource, String jsonMatriz, String jsonParam, Object jsonValor, int nivel) {
        try {
            boolean found = false;
            JSONObject jo = new JSONObject(jsonSource);
            
            for (int i=0; i<jo.names().length(); i++) {
                if (jo.names().get(i).toString().equals(jsonMatriz)) {
                    found = true;
                }
                
                
            }

            
            return "nulo";
        } catch (Exception e) {
            return jsonSource;
        }
    }
    
}
