package cap.services;

import cap.utiles.GlobalParams;

public class MonService {
	GlobalParams gParams;
	Metadata md;
	
	public MonService (GlobalParams m) {
		this.gParams = m;
		this.md = new Metadata(gParams);
	}
	
	public String getMonRequest(String method, Object param) throws Exception {
		try {
			String response = "";
			
			response = md.getMonRequest(method, param);
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
