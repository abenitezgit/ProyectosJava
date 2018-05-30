package ecc.services;

import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

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
	private int rstorage;
	private int dfstorage;
	
	//Getter and Setter

	public String getFtpDir() {
		return ftpDir;
	}

	public int getRstorage() {
		return rstorage;
	}

	public void setRstorage(int rstorage) {
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
			if (zone==1) {
				return gDatos.getInfoZon1().getUploadFolder();
			} else {
				return gDatos.getInfoZon2().getUploadFolder();
			}
		} catch (Exception e) {
			throw new Exception("Error getUploadFolder: "+e.getMessage());
		}
	}
	
	public String getUrlAudio() throws Exception {
		try {
			if (zone==1) {
				return gDatos.getInfoZon1().getUrlDecode();
			} else {
				return gDatos.getInfoZon2().getUrlDecode();
			}
		} catch (Exception e) {
			throw new Exception("Error getZoneLocal: "+e.getMessage());
		}
	}
	
	public void getDataID(String ID) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		try {
			logger.info("Iniciando conexión a solR...");
			logger.info("zkHost: "+gDatos.getZkHost());
			logger.info("Collection: "+gDatos.getCollectionBase());
			solrConn.connect(gDatos.getZkHost(), gDatos.getCollectionBase());
			
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
					rstorage = solrdata.getRstorage();
					zone = solrdata.getZone();
					
					if (zone==0) {
						zone = 1;
					}
					
					if (zone==1) {
						dfstorage = gDatos.getInfoZon1().getDfStorage();
					} else {
						dfstorage = gDatos.getInfoZon2().getDfStorage();
					}
					
					if (rstorage==0) {
						 rstorage = dfstorage;
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
