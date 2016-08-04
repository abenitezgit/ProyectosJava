/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 *
 * @author andresbenitez
 */
public class strucData {
    
    public static void main(String args[]) throws IOException {
        dataServicio servicio = new dataServicio();
        
        servicio.setSrvID("srv00001");
        servicio.setSrvDesc("Servicio Primariio");
        servicio.setSrvEnable(1);
        
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        
        String response;
        
        //mapper.writeValue(System.out, servicio);
        response = mapper.writeValueAsString(servicio);
        
        System.out.println(response);
        
        
    
    }
}
