package ecc.model;

public class Info {
	
	private String configPath;
	private String configFile;
	private String cloudName;
	private String zkSolr;

	//Getter and Setter
	
	public String getConfigPath() {
		return configPath;
	}
	public String getZkSolr() {
		return zkSolr;
	}
	public void setZkSolr(String zkSolr) {
		this.zkSolr = zkSolr;
	}
	public String getCloudName() {
		return cloudName;
	}
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
}
