/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;
import java.io.*;

import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;
/**
 *
 * @author andresbenitez
 */
public class PlayFile {
        // private String srcFile = "src.au";
    // private String srcFile = "tull.mpg";
    private String srcFile = "the_privilege.mpg";

    public static void main(String [] args) {
	new PlayFile();
    }

    public PlayFile() {
	Player p;

	MediaLocator src = new MediaLocator("file:/Users/andresbenitez/Documents/paso/01.mp3");

	try {
	    p = Manager.createPlayer(src);
	    //p.addControllerListener(this);
	    p.start();
	} catch(Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public synchronized void controllerUpdate(ControllerEvent evt) {
	if (evt instanceof EndOfMediaEvent) {
	    System.out.println("Play finished");
	    System.exit(0);
	} else {
	    System.out.println(evt.toString());
	}
    }
}
