package ecc.services;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.api.FtpAPI;
import com.rutinas.Rutinas;

import ecc.utiles.GlobalArea;

public class GetFileService {
	Logger logger = Logger.getLogger("wsGrab");
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	
	public GetFileService(GlobalArea m) {
		gDatos = m;
	}
	
	
	//Clases especificas
	FtpAPI ftp = new FtpAPI();
	
	//Parametros de Acceso
	private String HostIP;
	private String userName;
	private String userPass;
	private String workFolder;
	
	//Parametros de Ingreso
	private String zone;
	private String audioPathFile;
	private String downFileName;
	private String ID;
	private String rstorage;
	private String ftpPath;
	
	//Getter and Setter

	
	public String getHostIP() {
		return HostIP;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getRstorage() {
		return rstorage;
	}

	public void setRstorage(String rstorage) {
		this.rstorage = rstorage;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getAudioPathFile() {
		return audioPathFile;
	}

	public void setAudioPathFile(String audioPathFile) {
		this.audioPathFile = audioPathFile;
	}

	public String getDownFileName() {
		return downFileName;
	}

	public void setDownFileName(String downFileName) {
		this.downFileName = downFileName;
	}

	public void setHostIP(String hostIP) {
		HostIP = hostIP;
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

	public String getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	//Public Methods

	public void paseaDataInput(String dataInput) throws Exception {
		try {
			JSONObject jo = new JSONObject(dataInput);
			zone = jo.getString("zone");
			audioPathFile = jo.getString("audioPathFile");
			ID = jo.getString("ID");
			downFileName = ID + ".wav";
			rstorage = jo.getString("rstorage");
		} catch (Exception e) {
			throw new Exception("Error parseaDataInput: "+e.getMessage());
		}
	}
	
	public void getDataConfig(String zone) throws Exception {
		try {
			String Zone = "Zon"+zone;
			String rs = "rs1";
			switch(rstorage) {
				case "1":
					rs = "rs1";
					break;
				case "2":
					rs = "rs2";
					break;
			}
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(gDatos.getFileConfig()));
			HostIP = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+"."+rs+".server");
			userName = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+"."+rs+".user");
			userPass = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+"."+rs+".pass");
			workFolder = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+".workFolder");
			ftpPath = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+"."+rs+".path");
			if (ftpPath.equals("/")) {
				ftpPath="";
			}

		} catch (Exception e) {
			throw new Exception("Error getDataConfig: "+e.getMessage());
		}
	}
	
	public void getAudioFTP() throws Exception {
		try {
			ftp.setHostIP(HostIP);
			ftp.setUserName(userName);
			ftp.setUserPass(userPass);
			ftp.setConnectTimeout(3000);
			
			logger.info("Conectandose a Sitio FTP");
			logger.info("Resource Storage: "+rstorage);
			logger.info("FTP Server	: "+HostIP);
			logger.info("FTP User		: "+userName);
			logger.info("FTP Pass		: "+userPass);
			
			ftp.connect();
			
			if (ftp.isConnect()) {
				logger.info("Conectado a Sitio FTP");
				logger.info("Extrayendo grabación: "+ftpPath+audioPathFile);
				
				String localPathFile = workFolder+"/"+downFileName;
				if (ftp.download(ftpPath+audioPathFile, localPathFile)) {
					logger.info("Audio recuperado conerrectamente!!!");
				} else {
					throw new Exception("Audio no pudo ser recuperado");
				}
				ftp.disconnect();
			} else {
				throw new Exception("No pudo conectarse a sitio FTP");
			}
		} catch (Exception e) {
			throw new Exception("Error getAudioFTP: "+e.getMessage());
		}
	}

}
