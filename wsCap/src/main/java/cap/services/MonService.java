package cap.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.rutinas.Rutinas;

import cap.model.DataRequest;
import cap.model.DataResponse;
import cap.utiles.GlobalParams;

public class MonService {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Metadata md;
	
	public MonService (GlobalParams m) {
		this.gParams = m;
		this.md = new Metadata(gParams);
	}
	
	public DataResponse getMonRequest(DataRequest dRequest) throws Exception {
		final String LISTENER_METHOD = "monRequest";
		Object dataObject = null;
		
		try {
			DataResponse dResponse = new DataResponse();
			JSONArray ja;
			
			String sendRequest = md.genStringRequest(LISTENER_METHOD, dRequest);
			
			if (md.executeRequest(sendRequest)) {
				
				//Analiza el StringJSON que viene de respuesta para pasarlo a un objecto del tipo
				//correspondiente al request method
				
				switch (dRequest.getMethod()) {
					case "forceCancelProcess":
						dataObject = md.getResponse();
						break;
					default:
						List<Map<String,Object>> lstrows = new ArrayList<>();

						ja = (JSONArray) md.getResponse();
						for (int i=0; i<ja.length(); i++) {
							
							Map<String,Object> cols = new HashMap<>();
							cols = ja.getJSONObject(i).toMap();
							lstrows.add(cols);
						}
						dataObject = lstrows;
						break;
				}
				
				dResponse.setCode(md.getStatusCode());
				dResponse.setMessage(md.getMessage());
				dResponse.setData(dataObject);
				//dResponse.setData(md.getResponse());
				dResponse.setStatus("SUCCESS");
			} else {
				dResponse.setCode(md.getStatusCode());
				dResponse.setMessage(md.getMessage());
				dResponse.setData(dataObject);
				dResponse.setStatus("ERROR");
			}
			
			return dResponse;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
