package cap.utiles;

public class GlobalArea {
	String pathConfig;
	String fileConfig;
	
	//Constructor
	public GlobalArea() {
		fileConfig = "capMonitor.properties";
	}

	//Getter and Setter
	
	public String getFileConfig() {
		return fileConfig;
	}
	
	public String getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}

	//Metodos públicos
	
}
