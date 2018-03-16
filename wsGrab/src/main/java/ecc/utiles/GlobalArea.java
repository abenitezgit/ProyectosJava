package ecc.utiles;

import java.util.Map;
import java.util.TreeMap;

import ecc.model.DataRequest;

public class GlobalArea {
	private String fileConfig;
	private String hbProperties;
	private String solrCollection;
	private DataRequest dr = new DataRequest();
	private Map<String, String> mapOpcion = new TreeMap<>();
	private String opcionMatriz;
	private String solRfqFilters;
	
	//Variables final
	
	final String service_name="wsGrab";
	
	//Getter and Setter
	
	public String getFileConfig() {
		return fileConfig;
	}
	public String getSolRfqFilters() {
		return solRfqFilters;
	}
	public void setSolRfqFilters(String solRfqFilters) {
		this.solRfqFilters = solRfqFilters;
	}
	public String getOpcionMatriz() {
		return opcionMatriz;
	}
	public void setOpcionMatriz(String opcionMatriz) {
		this.opcionMatriz = opcionMatriz;
	}
	public DataRequest getDr() {
		return dr;
	}
	public void setDr(DataRequest dr) {
		this.dr = dr;
	}
	public Map<String, String> getMapOpcion() {
		return mapOpcion;
	}
	public void setMapOpcion(Map<String, String> mapOpcion) {
		this.mapOpcion = mapOpcion;
	}
	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}
	public String getHbProperties() {
		return hbProperties;
	}
	public void setHbProperties(String hbProperties) {
		this.hbProperties = hbProperties;
	}
	public String getSolrCollection() {
		return solrCollection;
	}
	public void setSolrCollection(String solrCollection) {
		this.solrCollection = solrCollection;
	}
	
	//Constructor
	public GlobalArea() {
		setFileConfig("/usr/local/hadoop/conf/hadoop.properties");
		setHbProperties("ecchdp1");
	}

}
