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

public class DBService {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Metadata md;
	
	public DBService(GlobalParams m) {
		this.gParams = m;
		this.md = new Metadata(gParams);
	}
	
	
	public DataResponse getDBRequest(DataRequest dRequest) throws Exception {
		final String LISTENER_METHOD = "dbRequest";
		Object dataObject = null;
		
		try {
			DataResponse dResponse = new DataResponse();
			JSONArray ja;
			
			String sendRequest = md.genStringRequest(LISTENER_METHOD, dRequest);
			
			if (md.executeRequest(sendRequest)) {
				//Analiza el StringJSON que viene de respuesta para pasarlo a un objecto del tipo
				//correspondiente al request method

				switch (dRequest.getMethod()) {
					case "addDependence":
						dataObject = (boolean) md.getResponse();
						break;
					case "addDBase":
						dataObject = (boolean) md.getResponse();
						break;
					case "addClient":
						dataObject = (boolean) md.getResponse();
						break;
					case "addCategory":
						dataObject = (boolean) md.getResponse();
						break;
					case "addProcGroup":
						dataObject = (boolean) md.getResponse();
						break;
					case "addSchedDiary":
						dataObject = (boolean) md.getResponse();
						break;
					case "addSchedule":
						dataObject = (boolean) md.getResponse();
						break;
					case "addDiary":
						dataObject = (boolean) md.getResponse();
						break;
					case "addGroup":
						dataObject = (boolean) md.getResponse();
						break;
					case "addGroupActiveManual":
						dataObject = (boolean) md.getResponse();
						break;
					case "addGroupActiveManualActual":
						dataObject = (boolean) md.getResponse();
						break;
					case "addProcActiveManualActual":
						dataObject = (boolean) md.getResponse();
						break;
					case "getAgeGroupWeek":
						dataObject = (boolean) md.getResponse();
						break;
					case "addProcActiveManual":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteGroupControl":
						dataObject = (boolean) md.getResponse();
						break;
					case "getAgeGroupStat":
						List<Map<String,Object>> lstMaps = new ArrayList<>();
						List<Object> lstList = new ArrayList<>();
						
						ja = (JSONArray) md.getResponse();
						for (int j=0; j<ja.length(); j++) {
							lstMaps = new ArrayList<>();
							for (int i=0; i<ja.getJSONArray(j).length(); i++) {
								
								Map<String,Object> cols = new HashMap<>();
								cols = ja.getJSONArray(j).getJSONObject(i).toMap();
								
								lstMaps.add(cols);
							}
							lstList.add(lstMaps);
						}
						dataObject = lstList;
						break;
					default:
						List<Map<String,Object>> lstRows = new ArrayList<>();
						
						ja = (JSONArray) md.getResponse();
						for (int i=0; i<ja.length(); i++) {
							
							Map<String,Object> cols = new HashMap<>();
							cols = ja.getJSONObject(i).toMap();
							
							lstRows.add(cols);
						}
						dataObject = lstRows;
						break;
				}

				dResponse.setCode(md.getStatusCode());
				dResponse.setMessage(md.getMessage());
				dResponse.setData(dataObject);
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
