package ecc.services;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.JSONObject;

import dataAccess.SolRDB;
import ecc.utiles.GlobalArea;
import utiles.common.rutinas.Rutinas;

public class ExtractService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	
	//Variables de Retorno
	private String ftpDir;
	private String fname;
	private int zone;
	
	//Getter and Setter
	
	public String getFtpDir() {
		return ftpDir;
	}

	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public void setFtpDir(String ftpDir) {
		this.ftpDir = ftpDir;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	//Public methods
	
	public String getUploadFolder(int zone) throws Exception {
		try {
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(gDatos.getFileConfig()));
			String workFolder = fileProperties.getProperty(gDatos.getHbProperties()+".ftpZon"+zone+".uploadFolder"); 
			
			return workFolder;
			
		} catch (Exception e) {
			throw new Exception("Error getUploadFolder: "+e.getMessage());
		}
	}
	
	public String getUrlAudio() throws Exception {
		try {
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(gDatos.getFileConfig()));
			String urlAudio = fileProperties.getProperty(gDatos.getHbProperties()+".urlAudioZon"+zone); 
			
			return urlAudio;
			
		} catch (Exception e) {
			throw new Exception("Error getZoneLocal: "+e.getMessage());
		}
	}
	
	public void getDataConnID(String connid) throws Exception {
		SolRDB solrConn = new SolRDB();
		try {
			mylib.console("Iniciando conexión a solR...");
			solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
			solrConn.open();
			
			if (solrConn.isConnected()) {
				mylib.console("Conexión establecido a solR...");
				
				//Declara filtros
				Map<String,String> filters = new HashMap<>();
				filters.put("q", "*:*");
				filters.put("fl", "ftpdir,id,fname,zone");
				filters.put("fq", "connid:"+connid);
				filters.put("rows", "1");
				filters.put("key", "id");
				
				//Extrae row
				mylib.console("Recuperando datos desde solR...");
				Map<String,String> mapRow = new HashMap<>();
				mapRow = solrConn.getRows(filters);
				
				//Busca columna URL
				for (Entry<String, String> entry : mapRow.entrySet()) {
					mylib.console("Encontrado id: "+entry.getKey()+ " value: "+entry.getValue());
					JSONObject joValue = new JSONObject(entry.getValue());
					ftpDir = (String) joValue.getJSONArray("ftpdir").get(0);
					fname = (String) joValue.getJSONArray("fname").get(0);
					try {
						zone = (int) joValue.getJSONArray("zone").get(0);
						if (zone!=1 && zone!=2) {
							mylib.console("Asignacion de zone default 1 (ECC)");
							zone = 1;
						}
					} catch (Exception e) {
						mylib.console("Asignacion de zone por exception en 1");
						zone = 1;
					}
					break;
				}
				
				solrConn.close();
			}
		} catch (Exception e) {
			if (solrConn.isConnected()) {
				solrConn.close();
			}
			throw new Exception("Error getDataConnID: "+e.getMessage());
		}
	}
}
