package ecc.utiles;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ecc.model.DataRequest;
import ecc.model.InfoZon1;
import ecc.model.InfoZon2;
import ecc.model.RStorage;

public class GlobalArea {
	private String fileConfig;
	private String clusterName;
	private String appName;
	private String zkHost;
	private String collectionBase;
	private String collectionBack;
	private String hbTableName;

	private InfoZon1 infoZon1 = new InfoZon1();
	private InfoZon2 infoZon2 = new InfoZon2();
	private DataRequest dr = new DataRequest();
	private Map<String, String> mapOpcion = new TreeMap<>();
	private Map<String, RStorage> mapStorage = new HashMap<>();
	private String opcionMatriz;
	private String solRfqFilters;
		
	//Constructor
	public GlobalArea() {
		setFileConfig("/usr/local/hadoop/conf/hadoopnew.properties");
		setClusterName("ecchwk01");
		setAppName("wsGrab");
	}

	//Getter and Setter
	
	public String getFileConfig() {
		return fileConfig;
	}

	public Map<String, RStorage> getMapStorage() {
		return mapStorage;
	}

	public void setMapStorage(Map<String, RStorage> mapStorage) {
		this.mapStorage = mapStorage;
	}

	public String getHbTableName() {
		return hbTableName;
	}

	public void setHbTableName(String hbTableName) {
		this.hbTableName = hbTableName;
	}

	public InfoZon1 getInfoZon1() {
		return infoZon1;
	}

	public void setInfoZon1(InfoZon1 infoZon1) {
		this.infoZon1 = infoZon1;
	}

	public InfoZon2 getInfoZon2() {
		return infoZon2;
	}

	public void setInfoZon2(InfoZon2 infoZon2) {
		this.infoZon2 = infoZon2;
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

	public String getCollectionBase() {
		return collectionBase;
	}

	public void setCollectionBase(String collectionBase) {
		this.collectionBase = collectionBase;
	}

	public String getCollectionBack() {
		return collectionBack;
	}

	public void setCollectionBack(String collectionBack) {
		this.collectionBack = collectionBack;
	}

	public String getZkHost() {
		return zkHost;
	}

	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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

}
