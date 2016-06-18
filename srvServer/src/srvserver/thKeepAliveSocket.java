/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvserver;
import utilities.globalAreaData;
import java.io.* ; 
import java.net.* ;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thKeepAliveSocket extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static boolean isSocketActive;
    static String errNum;
    static String errDesc;
    
    //Carga constructor para inicializar los datos
    public thKeepAliveSocket(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        isSocketActive = true;
    }
    
    @Override
    public void run() {
        String inputData;
        String outputData;
        String request;
        String auth;

        Socket skCliente;
        String response;
        String dataSend;
        try {
            skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
            gSub.sysOutln("Sending to Host Monitor: " + gDatos.getSrvMonHost() + "port: " + gDatos.getMonPort());
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            
            dataSend = gSub.sendDataKeep("keep");
            
            flujo.writeUTF( dataSend ); 
            
            InputStream inpStr = skCliente.getInputStream();
            DataInputStream dataInput = new DataInputStream(inpStr);
            response = dataInput.readUTF();

            //Analiza Respuesta
            gSub.sysOutln(response);
            
            int result = gSub.updateAssignedProcess(response);
            
            if (result!=0) {
                System.out.println(gSub.sendError(99,"Error en updateAssignedProc"));
            }
            
            
            dataInput.close();
            inpStr.close();
            flujo.close();
            aux.close();
            skCliente.close();
        }
        catch (NumberFormatException | IOException e) {
            System.out.println("KE error: "+e.getMessage()); 
        }
    }
}
