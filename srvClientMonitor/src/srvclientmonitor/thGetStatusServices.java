/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvclientmonitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class thGetStatusServices extends Thread{
    globalAreaData gDatos;
    srvRutinas gSub;
    
    public thGetStatusServices(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
    
    @Override
    public void run() {
        try {
            Socket skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            String dataSend = gSub.getStatusServices();
            System.out.println("send: "+dataSend);
            
            flujo.writeUTF( dataSend ); 
            InputStream inpStr = skCliente.getInputStream();
            DataInputStream dataInput = new DataInputStream(inpStr);
            String response = dataInput.readUTF();
            
            //Analiza Respuesta
            JSONObject ds = new JSONObject(response);
            
            if (ds.getString("result").equals("OK")) {
                JSONObject jData = ds.getJSONObject("data");
                
            
            }
            
            
            
            
            System.out.println(response);
            
        } catch (NumberFormatException | IOException e) {
            System.out.println(" Error conexion a server de monitoreo primary...."+ e.getMessage());
        }
    }
    
}
