package ecc.services;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.api.SolrAPI;
import com.rutinas.Rutinas;

import ecc.utiles.GlobalArea;

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
	
	public String getUploadFolder(int zone) {
		if (zone==1) {
			return gDatos.getInfoZon1().getUploadFolder();
		} else {
			return gDatos.getInfoZon2().getUploadFolder();
		}
	}
	
	public String getUrlAudio() {
		if (zone==1) {
			return gDatos.getInfoZon1().getUrlDecode();
		} else {
			return gDatos.getInfoZon2().getUrlDecode();
		}
	}
	
	public void getDataConnID(String connid) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		try {
			mylib.console("Iniciando conexión a solR...");
			solrConn.connect(gDatos.getZkHost(), gDatos.getCollectionBase());
			
			if (solrConn.connected()) {
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
					ftpDir = joValue.getString("ftpdir");
					fname = joValue.getString("fname");
					zone = joValue.getInt("zone");
					break; //solo importa recuperar el primer registro
				}
			}
		} catch (Exception e) {
			throw new Exception("Error getDataConnID: "+e.getMessage());
		} finally {
			try {
				solrConn.close();
			} catch (Exception e) {}
		}
	}
}
