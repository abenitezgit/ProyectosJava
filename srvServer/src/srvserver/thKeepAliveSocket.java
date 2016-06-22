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
    static String CLASS_NAME = "thKeepAliveSocket";
    
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
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            
            dataSend = gSub.sendDataKeep("keep");
            gSub.sysOutln(CLASS_NAME+": Enviado TX: " + dataSend);
            
            flujo.writeUTF( dataSend ); 
            
            InputStream inpStr = skCliente.getInputStream();
            DataInputStream dataInput = new DataInputStream(inpStr);
            response = dataInput.readUTF();

            //Analiza Respuesta
            gSub.sysOutln(CLASS_NAME+": Recibiendo respuesta: " + response);
            
            
            gSub.sysOutln(CLASS_NAME+": ejecutando...: updateAssignedProcess: " + response);
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
            gSub.sysOutln(CLASS_NAME+": Error Conectando a Socket monitor: " + e.getMessage());
        }
    }
}
