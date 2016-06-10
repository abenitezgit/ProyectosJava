/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iosamples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author andresbenitez
 */
public class ftpcli {
        public static void main(String args[]) throws IOException {
        String server = "hwk00";
        int port = 21;
        String user = "andresbenitez";
        String pass = "Maca3108";
        FTPClient ftp = new FTPClient();
        try {
            
            FTPClientConfig config = new FTPClientConfig();
            
            
            ftp.connect(server);
            ftp.login(user, pass);
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("/Users/andresbenitez/Documents/instaldown3.log");
 
            String firstRemoteFile = "instaldown3.log";
            InputStream inputStream = new FileInputStream(firstLocalFile);
 
            System.out.println("Start uploading first file");
            boolean done = ftp.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
 
            // APPROACH #2: uploads second file using an OutputStream
            File secondLocalFile = new File("/Users/andresbenitez/Documents/instaldown3.log");
            String secondRemoteFile = "instaldown4.log";
            inputStream = new FileInputStream(secondLocalFile);
 
            System.out.println("Start uploading second file");
            OutputStream outputStream = ftp.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;
 
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();
 
            boolean completed = ftp.completePendingCommand();
            if (completed) {
                System.out.println("The second file is uploaded successfully.");
            }
            
            
            int reply = ftp.getReplyCode();
            
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            
            
        } catch (IOException e) {
            System.out.println("IO Error");
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    
    }

}
