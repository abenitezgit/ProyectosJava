//package cap.utiles;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//import utiles.common.rutinas.Rutinas;
//
//
//public class DataExchange {
//	GlobalArea gDatos;
//	Procedures myproc;
//	Rutinas mylib = new Rutinas();
//	
//	public DataExchange(GlobalArea m) {
//		gDatos = m;
//		myproc = new Procedures(gDatos);
//	}
//	
//	public String sendStatus() {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("mapModule", gDatos.getMapModule());
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch (IOException e) {
//			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
//		}
//	}
//	
//    public String sendProcControl() {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("mapProcControl", gDatos.getMapProcControl());
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch (IOException e) {
//			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
//		}
//    	
//    }
//    
//    public String sendMapTask() {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("mapTask", gDatos.getMapTask());
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch (IOException e) {
//			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
//		}
//    	
//    }
//    
//
//    public String sendMapInterval() {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("mapInterval", gDatos.getMapInterval());
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch (IOException e) {
//			return mylib.msgResponse(99, "Error sendMapInterval ("+e.getMessage()+")","");
//		}
//    	
//    }
//    
//    public String sendGroupControl() {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("mapGroupControl", gDatos.getMapGroupControl());
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch (IOException e) {
//			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
//		}
//    	
//    }
//
//    public String sendService(String srvID) {
//		/**
//		 * Debe retornar la respuesta formateada en json con jHeader y jData
//		 */
//		
//		Map<String, Object> mapResponse = new HashMap<>();
//		String data="";
//		
//		try {
//			mapResponse.put("service", gDatos.getMapService().get(srvID));
//			mapResponse.put("task", myproc.getMapTaskServicePending(srvID));
//			data = mylib.serializeObjectToJSon(mapResponse, false);
//			
//			return mylib.msgResponse(0,"", data);
//		} catch ( Exception e) {
//			mylib.console(1,"Error sendService ("+e.getMessage()+")");
//			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
//		}
//    	
//    }
//    
//    
//	public String syncService(JSONObject jData) {
//		try {
//			if (gDatos.isSyncMetadata) {
//			
//				JSONObject jService = new JSONObject(jData.getString("service"));
//				JSONObject jTask = new JSONObject(jData.getString("task"));
//				
//				/*
//				 * Extrae los datos del servicio en un map de respuesta
//				 * y con este map envia a actualizar los valores retornados por el servicio 
//				 */
//				
//				Map<String, Object> mapSrv = jService.toMap();
//				String srvId = (String) mapSrv.get("srvId");
//				
//				gDatos.updateService(mapSrv);
//	
//				@SuppressWarnings("unchecked")
//				Map<String, Task> mapTask = (Map<String, Task>) mylib.serializeJSonStringToObject(jTask.toString(), Map.class);
//	
//				myproc.updateReceiveTask(mapTask);
//				
//				/**
//				 * Extrae los datos de las Task en un map local para ser enviados a actualizar al map global
//				 * 
//				 */
//				
//				return sendService(srvId);
//			} else {
//				return mylib.sendError(99,"Aun no se realiza sync de metadata");
//			}
//			
//		} catch (Exception e) {
//			return mylib.sendError(99, "Error proc: syncService ("+e.getMessage()+")");
//		}
//	}
//
//
//}
