package ecc.model;

public class SolrDataWav {
	String id;
	String ftpdir;
	String fname;
	int zone;
	int rstorage;
	
	public int getRstorage() {
		return rstorage;
	}
	public void setRstorage(int rstorage) {
		this.rstorage = rstorage;
	}
	public String getFtpdir() {
		return ftpdir;
	}
	public void setFtpdir(String ftpdir) {
		this.ftpdir = ftpdir;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public int getZone() {
		return zone;
	}
	public void setZone(int zone) {
		this.zone = zone;
	}
}
