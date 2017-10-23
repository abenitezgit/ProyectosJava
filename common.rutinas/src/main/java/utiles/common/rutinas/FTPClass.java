package utiles.common.rutinas;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClass {
	FTPClient ftp = new FTPClient();
	
	private String hostIP;
	private String userName;
	private String userPass;
	private int connectTimeout = 3000;
	
	//Constructor
	public FTPClass() {
		
	}
	
	public FTPClass(String hostIP, String userName, String userPass, int timeout) {
		this.hostIP = hostIP;
		this.userName = userName;
		this.userPass = userPass;
		this.connectTimeout = timeout;
	}
	
	//Getter and setter
	
	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	//Public Methods
	
	public void connect() throws Exception {
		try {
	        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	        int reply;

	        	ftp.setConnectTimeout(connectTimeout);
	        	ftp.connect(hostIP);

            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new Exception("Problema al intentar conectar con el servidor FTP");
            }
            
            ftp.login(userName, userPass);
            ftp.setFileType(BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            
		} catch (Exception e) {
			throw new Exception("Error connect: "+e.getMessage());
		}
	}
	
	public void disconnect() throws Exception {
		try {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
		} catch (Exception e) {
			throw new Exception("Error isOpen: "+e.getMessage());
		}
	}
	
	public boolean isConnect() throws Exception {
		try {
			return ftp.isConnected();
		} catch (Exception e) {
			throw new Exception("Error isOpen: "+e.getMessage());
		}
	}
	
	public InputStream getInputStream(String remotePathFile) throws Exception {
		try {
			return ftp.retrieveFileStream(remotePathFile);
		} catch (Exception e) {
			throw new Exception("Error downStream: "+e.getMessage());
		}
	}
	
	public boolean download(String remotePathFile, String localPathFile) throws Exception {
		try {
			boolean result;
			File downloadFile = new File(localPathFile);
			FileOutputStream fosDownloadFile = new FileOutputStream(downloadFile);   	//Puntero para escribir el archivo
			OutputStream osDownloadFile = new BufferedOutputStream(fosDownloadFile);
			
			InputStream isDownloadFile = ftp.retrieveFileStream(remotePathFile);
			
            //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = isDownloadFile.read(bytesArray)) != -1) {
            		osDownloadFile.write(bytesArray, 0, bytesRead);
            }
            if (ftp.completePendingCommand()) {
            		result = true;
            } else {
            		result = false;
            }
            isDownloadFile.close();
            osDownloadFile.close();
            fosDownloadFile.close();
 
			return result;
		} catch (Exception e) {
			throw new Exception("Error bajando archivo: "+e.getMessage());
		}
	}

}
