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
public class srvPosMethods {
    srvRutinas gRutinas; 
    globalAreaData gDatos;
    
    public srvPosMethods(globalAreaData m) {
        gDatos = m;
        gRutinas = new srvRutinas(gDatos);
    }
    
    public String posError(int errNum) {
        String cadena = "";
        String errDesc = "";
        switch (errNum) {
            case 10:
                errDesc = "TX no autorizada";
                break;
            default:
                break;
        }
        try {
            JSONObject jo = new JSONObject();
            
            return "";
        } catch (Exception e) {
            return "{error}";
        }
         
    }

    
}
