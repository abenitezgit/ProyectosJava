/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author andresbenitez
 */
public class testAudio {
    
    public static void main(String args[]) throws MalformedURLException, IOException {
    
    URL url = new URL("http://srv-gui-g.e-contact.cl/e-recorder/audio/20160414/09/01_20160414_095333_94410999430612__1460638413.4928.wav");
    Scanner s = new Scanner(url.openStream());
    
    File f=new File("/Users/andresbenitez/Documents/paso/JAJA.mp3");
    
    File f2 = new File("grabacionesclaro.e-contact.cl/2011/2016041300/20160413_000118_00011008887674_98458726_TTR42-1460516478.154581.WAV");
    
    org.apache.commons.io.FileUtils.copyURLToFile(url, f);
    
    //String bip = "/Users/andresbenitez/Documents/paso/01.mp3";
    
    Media hit = new Media(f);
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    
    mediaPlayer.play();
    }
}
