/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvclientmonitor;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class srvRutinas {
    globalAreaData gDatos;
    
    public srvRutinas(globalAreaData m) {
        gDatos = m;
    }
    
    public String getStatusServices() {
        try {
            JSONObject jData = new JSONObject();
            JSONObject jHeader = new JSONObject();

            jHeader.put("data", jData);
            jHeader.put("auth",gDatos.getAuthKey());
            jHeader.put("request", "getStatus");
        
            return jHeader.toString();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return null;
        }
    }
    
    
    public String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);
        
        return mapper.writeValueAsString(object);
    }
    
    public Object serializeJSonStringToObject (String parseJson, Class className) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.readValue(parseJson, className);
    }    
}
