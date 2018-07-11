package ecc.utiles;

import ecc.model.Info;

public class GlobalParams {
	private String fileConfig;
	private String clusterName;
	private String appName;

	private Info info = new Info();
		
	//Constructor
	public GlobalParams() {
		setFileConfig("/usr/local/hadoop/conf/hadoopnew.properties");
		setClusterName("ecchwk01");
		setAppName("wsProxyGrab");
	}
	
	//Getter and Setter

	public String getFileConfig() {
		return fileConfig;
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

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

}
