package ecc.services;

import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.api.SolrAPI;
import com.rutinas.Rutinas;

import ecc.model.SolrDataWav;
import ecc.utiles.GlobalArea;

import org.apache.log4j.Logger;

public class ExtractDataService {
	Logger logger = Logger.getLogger("wsGrab");
	static GlobalArea gDatos;
	static Rutinas mylib = new Rutinas();
	
	
	//Constructor
	public ExtractDataService(GlobalArea m) {
		gDatos = m;
	}
	
	//Variables de Retorno
	private String ftpDir;
	private String fname;
	private int zone;
	private String rstorage;
	
	//Getter and Setter
	
	public String getFtpDir() {
		return ftpDir;
	}

	public String getRstorage() {
		return rstorage;
	}

	public void setRstorage(String rstorage) {
		this.rstorage = rstorage;
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
	
	public void getDataID(String ID) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		try {
			logger.info("Iniciando conexión a solR...");
			solrConn.connect("cloudera4:2181,cloudera5:2181", "collgrabdata");
			
			if (solrConn.connected()) {
				logger.info("Conexion establecida a solR ");
				
				//Declara filtros
				Map<String,String> filters = new HashMap<>();
				filters.put("q", "*:*");
				filters.put("fl", "ftpdir,id,fname,zone,rstorage");
				filters.put("fq", "id:"+ID);
				filters.put("rows", "1");
				filters.put("key", "id");
				
				//Extrae row
				logger.info("Recuperando datos desde solR...");
				Map<String,String> mapRow = new HashMap<>();
				mapRow = solrConn.getRows(filters);
				
				//Busca columna URL
				SolrDataWav solrdata = new SolrDataWav();
				for (Entry<String, String> entry : mapRow.entrySet()) {
					logger.info("Encontrado id: "+entry.getKey()+ " value: "+entry.getValue());
					
					solrdata = new SolrDataWav();
					solrdata = (SolrDataWav) mylib.serializeJSonStringToObject(entry.getValue(), SolrDataWav.class);
					
					ftpDir = mylib.nvlString(solrdata.getFtpdir());
					fname = mylib.nvlString(solrdata.getFname());
					if (solrdata.getRstorage().size()==1) {
						rstorage = solrdata.getRstorage().get(0);
					} else {
						rstorage = "1";
					}
					zone = solrdata.getZone();
					try {
						if (zone!=1 && zone!=2) {
							logger.info("Asignacion de zone default 1 (ECC)");
							zone = 1;
						}
					} catch (Exception e) {
						logger.error("Asignacion de zone por exception en 1");
						zone = 1;
					}
					
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Error getDataID: "+e.getMessage());
		} finally {
			try {
				solrConn.close();
			} catch (Exception e) {}
		}
	}
}
