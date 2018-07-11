package ecc.model;

public class Info {
	private String restServer;
	private String restPort;
	private String restUrlBase;
	private String restUrlExtract;
	private String restContentType;
	private String restMediaWork;
	
	
	public String getRestMediaWork() {
		return restMediaWork;
	}
	public void setRestMediaWork(String restMediaWork) {
		this.restMediaWork = restMediaWork;
	}
	public String getRestUrlExtract() {
		return restUrlExtract;
	}
	public void setRestUrlExtract(String restUrlExtract) {
		this.restUrlExtract = restUrlExtract;
	}
	public String getRestServer() {
		return restServer;
	}
	public void setRestServer(String restServer) {
		this.restServer = restServer;
	}
	public String getRestPort() {
		return restPort;
	}
	public void setRestPort(String restPort) {
		this.restPort = restPort;
	}
	public String getRestUrlBase() {
		return restUrlBase;
	}
	public void setRestUrlBase(String restUrlBase) {
		this.restUrlBase = restUrlBase;
	}
	public String getRestContentType() {
		return restContentType;
	}
	public void setRestContentType(String restContentType) {
		this.restContentType = restContentType;
	}
}
