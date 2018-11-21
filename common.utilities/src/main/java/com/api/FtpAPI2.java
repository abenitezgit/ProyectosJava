package com.api;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpAPI2 {
	FTPClient ftp = new FTPClient();
	
	private String hostIP;
	private String userName;
	private String userPass;
	private int connectTimeout = 3000;
	private int replyCode;
	private String replyString;
	
	//Constructor
	public FtpAPI2() {
	}
	
	/**
	 * @param hostIP
	 * @param userName
	 * @param userPass
	 * @param timeout (milisegundos)
	 */
	public FtpAPI2(String hostIP, String userName, String userPass, int timeout) {
		this.hostIP = hostIP;
		this.userName = userName;
		this.userPass = userPass;
		this.connectTimeout = timeout;
	}
	
	//Getter and setter

	public int getReplyCode() {
		return replyCode;
	}

	public void setReplyCode(int replyCode) {
		this.replyCode = replyCode;
	}

	public String getReplyString() {
		return replyString;
	}

	public void setReplyString(String replyString) {
		this.replyString = replyString;
	}

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
		final int FTP_LOGIN_SUCCESS = 230;
		final int FTP_SWITCHING_SUCCESS = 200;
		
		try {
	        //ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	        //int reply;

        	ftp.setConnectTimeout(connectTimeout);
        	ftp.connect(hostIP);

        	replyCode = ftp.getReplyCode();
        	replyString = ftp.getReplyString();
        	
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftp.disconnect();
                throw new Exception("Problema al intentar conectar con el servidor FTP");
            }
            
            ftp.login(userName, userPass);
        	replyCode = ftp.getReplyCode();
        	replyString = ftp.getReplyString();
        	
        	if (replyCode==FTP_LOGIN_SUCCESS) {
	        	ftp.setFileType(BINARY_FILE_TYPE);
	        	replyCode = ftp.getReplyCode();
	        	replyString = ftp.getReplyString();
	        	
	        	if (replyCode==FTP_SWITCHING_SUCCESS) {
	        		ftp.enterLocalPassiveMode();
		        	replyCode = ftp.getReplyCode();
		        	replyString = ftp.getReplyString();
	        	}
        	} 
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void disconnect() throws Exception {
		try {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
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
	
	public void rename(String remotePath, String source, String dest) throws Exception {
		try {
			
			ftp.changeWorkingDirectory(remotePath);
			if (!ftp.rename(source, dest)) {
				throw new Exception("unable to rename file: "+dest);
			}
			
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	public List<String> getFiles(String remotePath, String pattern) throws Exception {
		try {
			List<String> files = new ArrayList<>();
			
			FTPFile[] myfiles = ftp.listFiles(remotePath+"/"+pattern);
			
			for (int i=0; i<myfiles.length; i++) {
				files.add(myfiles[0].getName());
			}
			
			return files;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
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
            
            replyCode = ftp.getReplyCode();
            replyString = ftp.getReplyString();
            
            isDownloadFile.close();
            osDownloadFile.close();
            fosDownloadFile.close();
 
			return result;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean mkdir(String remotePath) throws Exception {
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
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean cwd(String remotePath) throws Exception {
		try {
			int success = ftp.cwd(remotePath);
			if (success==250) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
            
            return completed;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void upload(String remotePathFile, String localPathFile) throws Exception {
		try {
			File upload = new File(localPathFile);
			FileInputStream fisUploadloadFile = new FileInputStream(upload);   	//Puntero para escribir el archivo
			InputStream isInputloadFile = new BufferedInputStream(fisUploadloadFile);
			
			//ftp.appendFile(remotePathFile, isInputloadFile);
			ftp.storeFile(remotePathFile, isInputloadFile);
			
			replyCode = ftp.getReplyCode();
			replyString = ftp.getReplyString();
			
			fisUploadloadFile.close();
			isInputloadFile.close();
 
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}



}
