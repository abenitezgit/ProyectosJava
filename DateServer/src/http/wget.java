/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import com.github.axet.wget.WGet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author andresbenitez
 */
public class wget {
        
        public static void main(String[] args) {
        try {
            // choise internet url (ftp, http)
            URL url = new URL("http://grabacionesclaro.e-contact.cl/2011/2016083000/20160830_000018_00011008887674_78535196_TTR42-1472526018.383122.WAV");
            // choise target folder or filename "/Users/axet/Downloads/ap61.ram"
            File target = new File("/Users/andresbenitez/Downloads/A.wav");
            // initialize wget object
            WGet w = new WGet(url, target);
            // single thread download. will return here only when file download
            // is complete (or error raised).
            w.download();
        } catch (MalformedURLException | RuntimeException e) {
            System.out.println("error: "+e.getMessage());
        }
    }
    
}
