/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 *
 * @author andresbenitez
 */
public class strucData {
    
    public static void main(String args[]) throws IOException {
        dataServicio servicio = new dataServicio();
        dataServicio srv = new dataServicio();
        
        List<dataServicio> lst = new ArrayList<>();
        
        List<String> lst2 = new ArrayList<>();
        
        servicio.setSrvID("srv00001");
        servicio.setSrvDesc("Servicio Primariio");
        servicio.setSrvEnable(1);
        
        lst.add(servicio);
        
        lst2.add("srv00001");
        
        srv.setSrvID("srv00001");
        srv.setSrvDesc("Servicio Primariio");
        srv.setSrvEnable(1);
        
        int index = lst2.indexOf(srv.getSrvID());
        
        System.out.println("indice found: "+index);
        
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        
        String response;
        
        //mapper.writeValue(System.out, servicio);
        response = mapper.writeValueAsString(servicio);
        
        System.out.println(response);
        
        
        
        
    
    }
}
