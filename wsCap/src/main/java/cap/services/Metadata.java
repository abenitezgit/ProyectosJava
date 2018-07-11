package cap.services;

import cap.utiles.GlobalParams;

public class Metadata {
	GlobalParams gParams;
	SocketService ss;
	
	public Metadata(GlobalParams m) {
		this.gParams = m;
		this.ss = new SocketService(gParams);
	}
	
	

	public String getDBRequest(String method, Object param) throws Exception {
		try {
			String response = "";
			
			switch (gParams.getConfig().getConnectTypeMon()) {
			case "SOCKET":
				response = ss.getDBRequest(method, param);
				break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public String getMonRequest(String method, Object param) throws Exception {
		try {
			String response = "";
			
			switch (gParams.getConfig().getConnectTypeMon()) {
			case "SOCKET":
				response = ss.getMonRequest(method, param);
				break;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
