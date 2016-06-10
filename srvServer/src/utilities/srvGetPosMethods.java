/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class srvGetPosMethods {
    srvRutinas gRutinas; 
    globalAreaData gDatos;
    
    public srvGetPosMethods(globalAreaData m) {
        gDatos = m;
        gRutinas  = new srvRutinas(gDatos);
    }
    
    public boolean isAuth(String request) {
        try {
            JSONObject ds = new JSONObject(request);
            if (ds.get("auth").toString().equals("qwerty0987")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public String getTypeRequest(String request) {
        try {
            JSONObject ds = new JSONObject(request);
            JSONObject single = (JSONObject) ds.get("header");
            return single.get("request").toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getCmdRequest(String request) {
        try {
            JSONObject ds = new JSONObject(request);
            JSONObject single = (JSONObject) ds.get("header");
            return single.get("cmd").toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getTypeBody(String request) {
        return "body";
    }
    
}
