/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author andresbenitez
 */
public class DateClient {
        public static void main(String[] args) throws IOException {
            
            
        //Envia sp a ejecutar
        //
            
        //String serverAddress = JOptionPane.showInputDialog(
        //    "Enter IP Address of a machine that is\n" +
        //    "running the date service on port 9090:");
        Socket skCliente = new Socket("localhost", 9090);

        OutputStream aux = skCliente.getOutputStream(); 
        DataOutputStream flujo= new DataOutputStream( aux ); 
        flujo.writeUTF( "getStatus" ); 
        skCliente.close();


        /*
        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        System.exit(0);*/
    }
}
