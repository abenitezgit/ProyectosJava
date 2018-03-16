package ecc.model;

import java.util.ArrayList;
import java.util.List;

public class SolrDataWav {
	String id;
	String ftpdir;
	String fname;
	int zone;
	List<String> rstorage = new ArrayList<>();
	
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
	public List<String> getRstorage() {
		return rstorage;
	}
	public void setRstorage(List<String> rstorage) {
		this.rstorage = rstorage;
	}
}
