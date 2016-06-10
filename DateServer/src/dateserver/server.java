/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;
import java.io.* ; import java.net.* ;

/**
 *
 * @author andresbenitez
 */
public class server {
    static final int PUERTO=5000; 
    
    public server() {
        try {
        ServerSocket skServidor = new ServerSocket(PUERTO); 
        System.out.println("Escucho el puerto " + PUERTO ); 
        for (int numCli = 0; numCli < 3; numCli++ ) {
            Socket skCliente = skServidor.accept(); // Crea objeto 
            System.out.println("Sirvo al cliente " + numCli); 
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            flujo.writeUTF( "Hola cliente " + numCli ); 
            skCliente.close();
        }
        System.out.println("Demasiados clientes por hoy"); } catch( Exception e ) {
        System.out.println( e.getMessage() ); }
    }
    public static void main( String[] arg ) {
        new server(); }
}
