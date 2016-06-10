/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class jsonformat {
    
    public static void main(String args[]) throws IOException {
        Properties p = new Properties();
        
        p.load(new FileInputStream("/Users/andresbenitez/Documents/Apps/NetBeansProjects3/DateServer/src/samples/newproperties.properties"));
        
        String jsonString = p.getProperty("json");
        
        System.out.println(jsonString);
        
        JSONObject obj = new JSONObject(jsonString);
        
        JSONArray arr = obj.getJSONArray("param");
        
        System.out.println(arr.length());
        
        String post_id = arr.getJSONObject(0).getString("post_id");

        System.out.println(post_id);
        
        obj.append("srvName", "srv0001");
        
        System.out.println(obj.toString());
        
        System.out.println("OTRO------------");
        
        JSONObject jo = new JSONObject();
            jo.put("nombre", "abt");
            jo.put("edad", true);
            
        JSONArray ja = new JSONArray();
            ja.put(jo);
            ja.put(jo);
            
        JSONObject mainjo = new JSONObject();
            mainjo.put("empleados", ja);
            mainjo.put("personas", ja);
            mainjo.put("gente", ja);
            
        System.out.println("1:"+mainjo.toString());
        // {"empleados":[{"nombre":"abt","edad":10},{"nombre":"abt","edad":10}],"personas":[{"nombre":"abt","edad":10},{"nombre":"abt","edad":10}]}
        
        //JSONObject jr = new JSONObject(mainjo.toString()).length();
        
        //JSONArray ar = new JSONArray();
        
        //Cantidad de Elementos principales (categorias)
        int i = new JSONObject(mainjo.toString()).length();
        
        //Los nombres de las categorias
        JSONArray ar = new JSONObject(mainjo.toString()).names();
        
        //Recorre un JSONArray
        
        JSONObject object = new JSONObject(mainjo.toString());
        //JSONObject getObject = object.getJSONObject("empleados");
        JSONArray getArray = object.getJSONArray("empleados");

        for(int j = 0; j < getArray.length(); j++)
        {
               System.out.println(getArray.get(j).toString());
               JSONObject oo = getArray.getJSONObject(j);
               System.out.println(oo.get("nombre"));
              //Iterate through the elements of the array i.
              //Get thier value.
              //Get the value for the first element and the value for the last element.
        }
        
        System.out.println(i);
        
        System.out.println(ar.toString());
        System.out.println(ar.get(1).toString());
        
        
        //System.out.println(ar.length());
            
    }
    
}
