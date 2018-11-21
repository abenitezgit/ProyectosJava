package cap.services;

import cap.utiles.GlobalParams;

public class ClientService {
	GlobalParams gParams;
	
	public ClientService(GlobalParams m) {
		this.gParams = m;
	}
	
	public void syncServiceParams() throws Exception {
		try {
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}
