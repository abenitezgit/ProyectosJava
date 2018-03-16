package ecc.model;

import java.util.ArrayList;
import java.util.List;

public class UriGrab {
	int numGrab;
	List<String> urlGrab = new ArrayList<>();;

	
	public List<String> getUrlGrab() {
		return urlGrab;
	}
	public void setUrlGrab(List<String> urlGrab) {
		this.urlGrab = urlGrab;
	}
	public int getNumGrab() {
		return numGrab;
	}
	public void setNumGrab(int numGrab) {
		this.numGrab = numGrab;
	}
}
