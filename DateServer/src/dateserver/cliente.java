/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;
import java.io.*; 
import java.net.*;

/**
 *
 * @author andresbenitez
 */
public class cliente {
    static final String HOST = "localhost"; 
    static final int PUERTO=9090;
public cliente( ) {
try{
Socket skCliente = new Socket( HOST , PUERTO ); 
InputStream aux = skCliente.getInputStream(); 
DataInputStream flujo = new DataInputStream( aux ); 
System.out.println( flujo.readUTF() ); 
skCliente.close();
} catch( Exception e ) {
System.out.println( e.getMessage() );
} }
public static void main( String[] arg ) { 
    new cliente();
}
}
