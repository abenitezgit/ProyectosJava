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
public class jsonformat6 {
    
    public static void main (String args[]) {
        JSONObject jo = new JSONObject();
        
        jo.append("request", "method");
        
        System.out.println(jo.toString());
    
    }
    
}
