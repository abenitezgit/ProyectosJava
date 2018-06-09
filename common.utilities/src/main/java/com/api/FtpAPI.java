package com.api;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpAPI {
	Logger logger = Logger.getLogger("FtpAPI");
	FTPClient ftp = new FTPClient();
	
	private String hostIP;
	private String userName;
	private String userPass;
	private int connectTimeout = 3000;
	
	//Constructor
	public FtpAPI() {
		
	}
	
	/**
	 * @param hostIP
	 * @param userName
	 * @param userPass
	 * @param timeout (milisegundos)
	 */
	public FtpAPI(String hostIP, String userName, String userPass, int timeout) {
		this.hostIP = hostIP;
		this.userName = userName;
		this.userPass = userPass;
		this.connectTimeout = timeout;
	}
	
	//Getter and setter
	
	public FTPClient getFtpClient() {
		return ftp;
	}
	
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
			logger.info("Conecting FtpHost: "+hostIP);
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
            
            logger.info("Connected to FtpHost: "+hostIP);
            
		} catch (Exception e) {
			throw new Exception("connect: "+e.getMessage());
		}
	}
	
	public void disconnect() throws Exception {
		try {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
			logger.info("Disconnect from ftpHost: "+hostIP);
		} catch (Exception e) {
			throw new Exception("disconnect: "+e.getMessage());
		}
	}
	
	public boolean isConnect() throws Exception {
		try {
			return ftp.isConnected();
		} catch (Exception e) {
			throw new Exception("isConnect: "+e.getMessage());
		}
	}
	
	public InputStream getInputStream(String remotePathFile) throws Exception {
		try {
			return ftp.retrieveFileStream(remotePathFile);
		} catch (Exception e) {
			throw new Exception("getInputStream: "+e.getMessage());
		}
	}
	
	public boolean download(String remotePathFile, String localPathFile) throws Exception {
		try {
			logger.info("Downloading File: "+remotePathFile+ " to: "+localPathFile);
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
			throw new Exception("download: "+e.getMessage());
		}
	}
	
	public boolean mkdir(String remotePath) {
		try {
			boolean root=true;
			StringTokenizer tokens = new StringTokenizer(remotePath,"/");
			
			while (tokens.hasMoreTokens()) {
				String dirName = tokens.nextToken();
				if (root) {
					if (!cwd("/"+dirName)) {
						return false;
					}
					root = false;
				} else {
					if (!cwd(dirName)) {
						if (ftp.makeDirectory(dirName)) {
							if (!cwd(dirName)) {
								return false;
							}							
						}
					}
				}
			}
			
			return true;
		} catch (Exception e) {
			logger.error("ftp:mkdir: "+e.getMessage());
			return false;
		}
	}
	
	public boolean cwd(String remotePath)  {
		try {
			int success = ftp.cwd(remotePath);
			if (success==250) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("ftp:cwd: "+e.getMessage());
			return false;
		}
	}
	
	public boolean deleteFile(String localPathFile) throws Exception {
		try {
			
			return ftp.deleteFile(localPathFile);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	public boolean upload(InputStream is, String remotePathFile) throws Exception {
		try {
			
			
			OutputStream outputStream = ftp.storeFileStream(remotePathFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;
 
            while ((read = is.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            is.close();
            outputStream.close();
 
            boolean completed = ftp.completePendingCommand();
            if (completed) {
                System.out.println("The second file is uploaded successfully.");
            }
            
            return completed;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean upload(String remotePathFile, String localPathFile) throws Exception {
		try {
			logger.info("Subiendo archivo: "+localPathFile+ " to: "+remotePathFile);
			File upload = new File(localPathFile);
			FileInputStream fisUploadloadFile = new FileInputStream(upload);   	//Puntero para escribir el archivo
			InputStream isInputloadFile = new BufferedInputStream(fisUploadloadFile);
			
			//ftp.appendFile(remotePathFile, isInputloadFile);
			boolean success = ftp.storeFile(remotePathFile, isInputloadFile);
			
			fisUploadloadFile.close();
			isInputloadFile.close();
 
			return success;
		} catch (Exception e) {
			throw new Exception("download: "+e.getMessage());
		}
	}


}
