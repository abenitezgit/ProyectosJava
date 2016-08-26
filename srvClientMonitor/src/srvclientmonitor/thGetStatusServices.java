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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import org.json.JSONArray;
import org.json.JSONObject;
import static srvclientmonitor.frmMain.gDatos;

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
        
        Timer t1 = new Timer();
        t1.schedule(new myTimerTask(), 5000, 5000);
    }
    
    
    class myTimerTask extends TimerTask {
    
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
                gDatos.getjTextAreaLog().append(jData.toString()+"\n");
                gDatos.getjTextAreaLog().setCaretPosition(gDatos.getjTextAreaLog().getText().length());
                
                JSONArray jaServicios = jData.getJSONArray("servicios");
                gDatos.getjTextAreaPoolProcess().setText(null);
                gDatos.getjTextAreaTypeProc().setText(null);
                for (int i=0; i<jaServicios.length(); i++) {
                    String srvID = jaServicios.getJSONObject(i).getString("srvID");
                    
                    JSONArray jaPool = jaServicios.getJSONObject(i).getJSONArray("lstPoolProcess");
                    for (int j=0; j<jaPool.length(); j++) {
                        gDatos.getjTextAreaPoolProcess().append(srvID+" "+jaPool.getJSONObject(j).toString()+"\n");
                        gDatos.getjTextAreaPoolProcess().setCaretPosition(gDatos.getjTextAreaPoolProcess().getText().length());
                    }
                    
                    JSONArray jaType = jaServicios.getJSONObject(i).getJSONArray("lstAssignedTypeProc");
                    for (int j=0; j<jaType.length(); j++) {
                        gDatos.getjTextAreaTypeProc().append(srvID+" "+jaType.getJSONObject(j).toString()+"\n");
                        gDatos.getjTextAreaTypeProc().setCaretPosition(gDatos.getjTextAreaTypeProc().getText().length());
                    }
                    
                }
            }
            
            //Enable Status Monitor
            gDatos.getLblMonDesc().setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_START_01()));
                
                
            dataInput.close();
            inpStr.close();
            flujo.close();
            aux.close();
            skCliente.close();
            
            System.out.println(response);
            
        } catch (NumberFormatException | IOException e) {
            //Enable Status Monitor
            String mesg = "Error conexion a server de monitoreo primary...."+ e.getMessage();
            gDatos.getLblMonDesc().setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_SPHERE_OFF_STATUS()));
            System.out.println(mesg);
            gDatos.getjTextAreaLog().append(mesg+"\n");
            gDatos.getjTextAreaLog().setCaretPosition(gDatos.getjTextAreaLog().getRows()-1);
        }
    }
}
}
