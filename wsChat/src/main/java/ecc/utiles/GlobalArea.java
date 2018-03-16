package ecc.utiles;

import ecc.model.DataRequest;

public class GlobalArea {
	String fileConfig;
	String log4Config;
	String hbProperties;
	String solrCollection;
	DataRequest dr = new DataRequest();
	boolean flagInitComponents;
	
	//Getter and Setter
	
	public String getFileConfig() {
		return fileConfig;
	}
	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}
	public String getLog4Config() {
		return log4Config;
	}
	public void setLog4Config(String log4Config) {
		this.log4Config = log4Config;
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
