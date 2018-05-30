package ecc.services;


import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.api.FtpAPI;
import com.rutinas.Rutinas;

import ecc.model.RStorage;
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
	private String hostIP;
	private String userName;
	private String userPass;
	private String workFolder;
	
	//Parametros de Ingreso
	private int zone;
	private String audioPathFile;
	private String downFileName;
	private String ID;
	private int rstorage;
	private String ftpPath;
	
	//Getter and Setter
	
	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public int getRstorage() {
		return rstorage;
	}

	public void setRstorage(int rstorage) {
		this.rstorage = rstorage;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
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
			zone = jo.getInt("zone");
			audioPathFile = jo.getString("audioPathFile");
			ID = jo.getString("ID");
			downFileName = ID + ".wav";
			rstorage = jo.getInt("rstorage");
		} catch (Exception e) {
			throw new Exception("Error parseaDataInput: "+e.getMessage());
		}
	}
	
	public void getDataConfig(int zone, int rstorage) throws Exception {
		try {
			RStorage rs = new RStorage();
			if (gDatos.getMapStorage().containsKey(String.valueOf(rstorage))) {
				rs = gDatos.getMapStorage().get(String.valueOf(rstorage));
			}
			
			hostIP = rs.getServer();
			userName = rs.getUser();
			userPass = rs.getPass();
			ftpPath = rs.getPath();
			
			if (zone==1) {
				workFolder = gDatos.getInfoZon1().getWorkFolder();
			} else {
				workFolder = gDatos.getInfoZon2().getWorkFolder();
			}
			
			if (ftpPath.equals("/")) {
				ftpPath="";
			}

		} catch (Exception e) {
			throw new Exception("Error getDataConfig: "+e.getMessage());
		}
	}
	
	public void getAudioFTP() throws Exception {
		try {
			ftp.setHostIP(hostIP);
			ftp.setUserName(userName);
			ftp.setUserPass(userPass);
			ftp.setConnectTimeout(3000);
			
			logger.info("Conectandose a Sitio FTP");
			logger.info("Resource Storage: "+rstorage);
			logger.info("FTP Server		: "+hostIP);
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
