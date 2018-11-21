package org.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.model.Ftp;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;
import org.utilities.MyUtils;

import com.api.FtpAPI2;
import com.api.SFtpAPI;
import com.rutinas.Rutinas;

public class ServiceFTP {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	MyUtils utils;
	Logger logger;
	MyLogger mylog;
	Ftp ftpProc;

	public ServiceFTP(GlobalParams m, Ftp ftpProc, MyLogger mylog) {
		this.gParams = m;
		this.ftpProc = ftpProc;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
		this.utils = new MyUtils(gParams);
	}

	private Date fecTask;
	
	public void setFecTask(Date fecTask) {
		this.fecTask = fecTask;
	}
	
	public boolean sendFtpFiles() throws Exception {
		final int FTP_STATUS_OK = 200;
		final int FTP_TRANSFER_COMPLETE = 226;
		
		try {
			boolean exitStatus = false;
			
			FtpAPI2 ftp = new FtpAPI2();
			
			ftp.setHostIP(ftpProc.getServerIP());
			ftp.setUserName(ftpProc.getUserName());
			ftp.setUserPass(ftpProc.getUserPass());
			ftp.setConnectTimeout(10000);
			
			mylog.info("Conectando a Sitio FTP: "+ftpProc.getServerIP()+ " ("+ftpProc.getUserName()+")");
			ftp.connect();
			
			mylog.info(ftp.getReplyCode()+" "+ftp.getReplyString());
			
			if (ftp.getReplyCode()==FTP_STATUS_OK) {
				mylog.info("Conectado exitosamente a sitio ftp");
				
				mylog.info("Enviando archivos a sitio ftp...");
				
				if (ftpProc.getLocalPath().equals("DEFAULT")) {
					ftpProc.setLocalPath(gParams.getAppConfig().getWorkPath());
				}
				
				String localFile = mylib.parseFnParam(ftpProc.getLocalFile(), fecTask);
				String remoteFile = mylib.parseFnParam(ftpProc.getRemoteFile(), fecTask);

				String localPath = utils.getLocalPath(ftpProc.getLocalPath());
				String localPathFileName = localPath+"/"+localFile;
				String remotePathFileName = utils.getFormatedPath(ftpProc.getRemotePath())+"/"+remoteFile;
					
				mylog.info("Uploading file: "+localPathFileName);
				ftp.upload(remotePathFileName, localPathFileName);
					
				if (ftp.getReplyCode()==FTP_TRANSFER_COMPLETE) {
					mylog.info("Upload Success: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					exitStatus = true;
				} else {
					mylog.error("Upload ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					throw new Exception("Upload ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				}
				
				ftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				throw new Exception("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("sendFtpFiles(): "+e.getMessage());
		}
	}
	
	public boolean downFtpFiles() throws Exception {
		final int FTP_STATUS_OK = 200;
		final int FTP_TRANSFER_COMPLETE = 226;
		
		try {
			boolean exitStatus = false;
			
			FtpAPI2 ftp = new FtpAPI2();
			
			ftp.setHostIP(ftpProc.getServerIP());
			ftp.setUserName(ftpProc.getUserName());
			ftp.setUserPass(ftpProc.getUserPass());
			ftp.setConnectTimeout(10000);
			
			mylog.info("Conectando a Sitio FTP: "+ftpProc.getServerIP()+ " ("+ftpProc.getUserName()+")");
			ftp.connect();
			
			mylog.info(ftp.getReplyCode()+" "+ftp.getReplyString());
			
			if (ftp.getReplyCode()==FTP_STATUS_OK) {
				mylog.info("Conectado exitosamente a sitio ftp");
				
				mylog.info("Bajando archivos desde sitio ftp...");
				
				if (ftpProc.getLocalPath().equals("DEFAULT")) {
					ftpProc.setLocalPath(gParams.getAppConfig().getWorkPath());
				}
				
				String localFile = mylib.parseFnParam(ftpProc.getLocalFile(), fecTask);
				String remoteFile = mylib.parseFnParam(ftpProc.getRemoteFile(), fecTask);

				String localPath = utils.getLocalPath(ftpProc.getLocalPath());
				String localPathFileName = localPath+"/"+localFile;
				String remotePathFileName = utils.getFormatedPath(ftpProc.getRemotePath())+"/"+remoteFile;
					
				mylog.info("Download file: "+localPathFileName);
				ftp.download(remotePathFileName, localPathFileName);
					
				if (ftp.getReplyCode()==FTP_TRANSFER_COMPLETE) {
					mylog.info("Download Success: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					exitStatus = true;
				} else {
					mylog.error("Download ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					throw new Exception("Download ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				}
				
				ftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				throw new Exception("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("downFtpFiles(): "+e.getMessage());
		}
	}

	public boolean sendSFtpFiles() throws Exception {
		try {
			boolean exitStatus = false;
			
			SFtpAPI sftp = new SFtpAPI();
			
			sftp.setServerIP(ftpProc.getServerIP());
			sftp.setUserName(ftpProc.getUserName());
			sftp.setUserPass(ftpProc.getUserPass());
			
			mylog.info("Conectando a Sitio SFTP: "+ftpProc.getServerIP()+ " ("+ftpProc.getUserName()+")");
			sftp.connect();
			
			if (sftp.isConnect()) {
				mylog.info("Conectado exitosamente a sitio sftp");
				
				mylog.info("Enviando archivos a sitio sftp...");
				
				if (ftpProc.getLocalPath().equals("DEFAULT")) {
					ftpProc.setLocalPath(gParams.getAppConfig().getWorkPath());
				}
				
				String localFile = mylib.parseFnParam(ftpProc.getLocalFile(), fecTask);
				String remoteFile = mylib.parseFnParam(ftpProc.getRemoteFile(), fecTask);

				String localPath = utils.getLocalPath(ftpProc.getLocalPath());
				String localPathFileName = localPath+"/"+localFile;
				String remotePathFileName = utils.getFormatedPath(ftpProc.getRemotePath())+"/"+remoteFile;
				
				mylog.info("Uploading Local pathFile: "+localPathFileName);
				mylog.info("Uploading Remote pathFile: "+remotePathFileName);
				if (sftp.upload(remotePathFileName, localPathFileName)) {
					mylog.info("Upload Success");
					exitStatus = true;
				} else {
					mylog.error("Upload ERROR");
					throw new Exception("Upload ERROR");
				}
				sftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP");
				throw new Exception("Error conectandose al sitio FTP");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("sendFtpFiles(): "+e.getMessage());
		}
	}

	public boolean downSFtpFiles() throws Exception {
		try {
			boolean exitStatus = false;
			
			SFtpAPI sftp = new SFtpAPI();
			
			sftp.setServerIP(ftpProc.getServerIP());
			sftp.setUserName(ftpProc.getUserName());
			sftp.setUserPass(ftpProc.getUserPass());
			
			mylog.info("Conectando a Sitio SFTP: "+ftpProc.getServerIP()+ " ("+ftpProc.getUserName()+")");
			sftp.connect();
			
			if (sftp.isConnect()) {
				mylog.info("Conectado exitosamente a sitio sftp");
				
				mylog.info("Bajando archivos desde sitio sftp...");
				
				if (ftpProc.getLocalPath().equals("DEFAULT")) {
					ftpProc.setLocalPath(gParams.getAppConfig().getWorkPath());
				}
				
				String localFile = mylib.parseFnParam(ftpProc.getLocalFile(), fecTask);
				String remoteFile = mylib.parseFnParam(ftpProc.getRemoteFile(), fecTask);

				String localPath = utils.getLocalPath(ftpProc.getLocalPath());
				String localPathFileName = localPath+"/"+localFile;
				String remotePathFileName = utils.getFormatedPath(ftpProc.getRemotePath())+"/"+remoteFile;
				
				mylog.info("Download file: "+localPathFileName);
				if (sftp.download(remotePathFileName, localPathFileName)) {
					mylog.info("Download Success");
					exitStatus = true;
				} else {
					mylog.error("Download ERROR");
					throw new Exception("Download ERROR");
				}
				sftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP");
				throw new Exception("Error conectandose al sitio FTP");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("downSFtpFiles(): "+e.getMessage());
		}
	}

}
