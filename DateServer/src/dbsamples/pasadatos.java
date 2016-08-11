/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbsamples;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class pasadatos {
    
    
    
    public static void main(String args[]) throws IOException {
        Persona persona = new Persona();
        
        
        JSONObject jo = new JSONObject();
        jo.put("nombre", "andres");
        jo.put("edad", 10);
        
        persona = (Persona) serializeJSonString(jo.toString(), Persona.class);
        
        System.out.println("nombre: "+persona.getNombre());
        System.out.println("edad: "+persona.edad);
    
    }
    
    public static String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);
        
        return mapper.writeValueAsString(object);
    }
    
    
    public static  Object serializeJSonString (String parseJson, Class className) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.readValue(parseJson.toString(), className);
    }
    
    static class Persona {
        String nombre;
        int edad;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getEdad() {
            return edad;
        }

        public void setEdad(int edad) {
            this.edad = edad;
        }
        
    }
}
