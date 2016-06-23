/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class thMonitor extends Thread {
    static globalAreaData gDatos;
    
    public thMonitor(globalAreaData m) {
        gDatos = m;
    }
    
    @Override
    public void run() {
        Timer t1 = new Timer();
        t1.schedule(new mainTimerTask(), 1000, 3000);
    }
    
    class mainTimerTask extends TimerTask {
        Socket skCliente;
        String dataSend;
        String response;
        String logMon;
        String srvName;
        String typeProc;
        String srvStart;
        String srvPort;
        int numTotalExec;
        int numProcMax;
        int numProcExec;    
        
        public mainTimerTask() {
        }
    
        @Override
        public void run() {
            try {
                //Establece conexion a srvMonitor
                skCliente = new Socket("localhost", 9080);
                OutputStream aux = skCliente.getOutputStream();
                DataOutputStream flujo= new DataOutputStream( aux );
                
                JSONObject jo = new JSONObject();
                jo.put("auth", "qwerty0987");
                jo.put("request", "getStatus");
                
                dataSend = jo.toString();
                
                flujo.writeUTF( dataSend );
                
                InputStream inpStr = skCliente.getInputStream();
                DataInputStream dataInput = new DataInputStream(inpStr);
                response = dataInput.readUTF();
                
                //Parsea la respuesta
                JSONObject rs = new JSONObject(response);
                JSONArray srvList = rs.getJSONArray("params");
                JSONObject srvRow = srvList.getJSONObject(0);
                JSONArray jaProcActives = srvRow.getJSONArray("procActive");
                JSONArray jaProcAssigned = srvRow.getJSONArray("procAssigned");
                
                srvName = srvRow.getString("srvName");
                srvPort = srvRow.getString("srvPort");
                srvStart = srvRow.getString("srvStart");
                numProcExec = srvRow.getInt("numProcExec");
                numTotalExec = srvRow.getInt("numTotalExec");
                numProcMax = srvRow.getInt("numProcMax");
                
                //Agrega Respuesta en textAreaMonLog
                gDatos.getTxtAreaLog().append(response+"\n");
                
                /*
                //Agrega Respuesta en TableMonServices
                DefaultTableModel dtm = new DefaultTableModel();
                
                    //Agrega las columnas
                    dtm.addColumn("srvName");
                    dtm.addColumn("srvPort");
                    dtm.addColumn("srvStart");
                    dtm.addColumn("numProcExec");
                    dtm.addColumn("numTotalExec");
                    dtm.addColumn("numProcMax");
                    
                    gDatos.getTblMonServices().setModel(dtm);
                    
                    dtm.addRow(new Object[]{srvName,srvPort,srvStart,numProcExec,numTotalExec,numProcMax});
                */
                //Agrega Respuesta en Lista de Servicios
                DefaultListModel dlm = new DefaultListModel();
                
                    gDatos.getLstMonService().setModel(dlm);
                
                    //Agrega las columnas
                    dlm.addElement(srvName+" - [FecIni]: "+srvStart+" - [ProcExec]: "+String.valueOf(numProcExec)+" -[TotalExec]: "+String.valueOf(numTotalExec)+" - [MaxProc]: "+String.valueOf(numProcMax));
                    
                //Analiza Respuesta
                
                dataInput.close();
                inpStr.close();
                flujo.close();
                aux.close();
                skCliente.close();
            } catch (IOException ex) {
                Logger.getLogger(thMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
    }
}
