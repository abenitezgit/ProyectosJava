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
					case "addUser":
						dataObject = (boolean) md.getResponse();
						break;
					case "addServer":
						dataObject = (boolean) md.getResponse();
						break;
					case "addBack":
						dataObject = (boolean) md.getResponse();
						break;
					case "addOsp":
						dataObject = (boolean) md.getResponse();
						break;
					case "addOspParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "addFtp":
						dataObject = (boolean) md.getResponse();
						break;
					case "addMov":
						dataObject = (boolean) md.getResponse();
						break;
					case "addMovParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "addEtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "addEtbParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "addLtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "addLtbParam":
						dataObject = (boolean) md.getResponse();
						break;
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
					case "deleteGroup":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteOsp":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteOspParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteMov":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteMovParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteEtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteEtbParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteLtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteLtbParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteFtp":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteBack":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteProcGroup":
						dataObject = (boolean) md.getResponse();
						break;
					case "deleteClient":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateBack":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateEtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateEtbParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateFtp":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateLtb":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateLtbParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateOsp":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateOspParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateMov":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateMovParam":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateUser":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateClient":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateCategory":
						dataObject = (boolean) md.getResponse();
						break;
					case "updateServer":
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
						Object objResponse = md.getResponse();
						
						String classType = objResponse.getClass().getSimpleName();
						
						if (classType.equals("Boolean")) {
							dataObject = (boolean) md.getResponse();
						} else {
							List<Map<String,Object>> lstRows = new ArrayList<>();
							
							ja = (JSONArray) md.getResponse();
							for (int i=0; i<ja.length(); i++) {
								
								Map<String,Object> cols = new HashMap<>();
								cols = ja.getJSONObject(i).toMap();
								
								lstRows.add(cols);
							}
							dataObject = lstRows;
						}
						
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
			throw new Exception("getDBRequest(): "+e.getMessage());
		}
	}
}
