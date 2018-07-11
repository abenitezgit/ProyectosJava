package cap.services;

import cap.utiles.GlobalParams;

public class DBService {
	GlobalParams gParams;
	Metadata md;
	
	public DBService(GlobalParams m) {
		this.gParams = m;
		this.md = new Metadata(gParams);
	}
	
	
	public String getDBRequest(String method, Object param) throws Exception {
		try {
			String response = "";
			
			response = md.getDBRequest(method, param);
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
