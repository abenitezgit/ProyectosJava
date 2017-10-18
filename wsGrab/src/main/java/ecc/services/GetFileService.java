package ecc.services;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.JSONObject;

import ecc.utiles.GlobalArea;
import utiles.common.rutinas.FTPClass;
import utiles.common.rutinas.Rutinas;

public class GetFileService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	
	//Clases especificas
	FTPClass ftp = new FTPClass();
	
	//Parametros de Acceso
	private String HostIP;
	private String userName;
	private String userPass;
	private String workFolder;
	
	//Parametros de Ingreso
	private String zone;
	private String audioPathFile;
	private String downFileName;
	private String connid;
	
	//Getter and Setter

	public String getHostIP() {
		return HostIP;
	}

	public String getConnid() {
		return connid;
	}

	public void setConnid(String connid) {
		this.connid = connid;
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
			connid = jo.getString("connid");
			downFileName = connid + ".wav";
		} catch (Exception e) {
			throw new Exception("Error parseaDataInput: "+e.getMessage());
		}
	}
	
	public void getDataConfig(String zone) throws Exception {
		try {
			String Zone = "Zon"+zone;
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(gDatos.getFileConfig()));
			HostIP = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+".server");
			userName = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+".user");
			userPass = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+".pass");
			workFolder = fileProperties.getProperty(gDatos.getHbProperties()+".ftp"+Zone+".workFolder");

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
			
			mylib.console("Conectandose a Sitio FTP");
			mylib.console("FTP Server	: "+HostIP);
			mylib.console("FTP User		: "+userName);
			mylib.console("FTP Pass		: "+userPass);
			
			ftp.connect();
			
			if (ftp.isConnect()) {
				mylib.console("Conectado a Sitio FTP");
				mylib.console("Extrayendo grabación");
				
				String localPathFile = workFolder+"/"+downFileName;
				if (ftp.download(audioPathFile, localPathFile)) {
					mylib.console("Audio recuperado conerrectamente!!!");
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
