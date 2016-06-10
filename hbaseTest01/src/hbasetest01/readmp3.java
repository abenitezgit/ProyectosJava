/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;
import com.sun.media.Connector;
import javax.media.*;
import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 *
 * @author andresbenitez
 */
public class readmp3 {
    
    public static void main(String args[]) throws NoPlayerException, IOException, CannotRealizeException {

        
 String path = "file:/Users/andresbenitez/Documents/paso/01.mp3";
 

    MediaLocator src = new MediaLocator("ffile:/Users/andresbenitez/Documents/paso/01.mp3");
    
    System.out.println(src.getProtocol());
        
// Take the path of the audio file from command line
 File f=new File("/Users/andresbenitez/Documents/paso/01.mp3");
 
 
 
 f.setExecutable(true);
 
 System.out.println(f.getPath());
 System.out.println(f.toURI().toURL());
 System.out.println(f.getName());


 // Create a Player object that realizes the audio
 
 
 final Player p3 = Manager.createPlayer(src);
 
 final Player p2 = Manager.createPlayer(f.toURI().toURL());
 

 
 System.out.println(p2.getDuration());
 
 p2.start();

 
 
 final Player p = Manager.createRealizedPlayer(f.toURI().toURL());
 
 
 
 p.setRate(24000);


  // Start the music
  p.start();


  // Create a Scanner object for taking input from cmd
  Scanner s=new Scanner(System.in);


  // Read a line and store it in st
  String st=s.nextLine();


   // If user types 's', stop the audio
   if(st.equals("s"))
   {
   p.stop();
   }
    
    }
    
}
