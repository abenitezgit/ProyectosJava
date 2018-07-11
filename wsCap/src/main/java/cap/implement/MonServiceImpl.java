package cap.implement;

import cap.services.IMonService;
import cap.services.SocketService;
import cap.utiles.GlobalParams;

public class MonServiceImpl implements IMonService{
	GlobalParams gParams;
	
	public MonServiceImpl(GlobalParams m) {
		gParams = m;
	}

	public String getMapProc() {
		SocketService ss = new SocketService(gParams);
		// TODO Auto-generated method stub
		
		String response = null;
		
		switch(gParams.getConfig().getConnectTypeMon()) {
		case "SOCKET":
				response = ss.getMapProc();
			
			break;
		case "URL":
			break;
		}
		
		return response;
	}
	

}
