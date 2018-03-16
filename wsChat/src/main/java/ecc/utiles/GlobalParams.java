package ecc.utiles;

import ecc.model.DataRequest;
import ecc.model.Info;

public class GlobalParams {
	
	private Info info = new Info();
	private DataRequest dr = new DataRequest();

	
	//Getter and Setter
	
	public Info getInfo() {
		return info;
	}

	public DataRequest getDr() {
		return dr;
	}

	public void setDr(DataRequest dr) {
		this.dr = dr;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

}
