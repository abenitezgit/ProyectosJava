package org.dataAccess;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.Mov;
import org.model.MovMatch;
import org.model.Osp;
import org.model.OspParam;
import org.model.PGPending;
import org.utilities.GlobalParams;

import com.api.MysqlAPI;
import com.rutinas.Rutinas;

public class DataAccess {
	Logger logger = Logger.getLogger("DataAccess");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	MysqlAPI dbConn;
	
	public DataAccess(GlobalParams m) {
		gParams = m;
		dbConn = new MysqlAPI(gParams.getInfo().getDbHostName(), gParams.getInfo().getDbName(), String.valueOf(gParams.getInfo().getDbPort()),gParams.getInfo().getDbUser(),gParams.getInfo().getDbPass(),gParams.getInfo().getDbTimeOut());
	}
	
	
	/**
	 * Public Methods
	 */
	public void open() throws Exception {
		dbConn.open();
	}
	
	public void close() throws Exception {
		dbConn.close();
	}
	
	public boolean isConnected() throws Exception {
		
		return dbConn.isConnected();
	}
	
	public String getDBParams() throws Exception {
		try {
			JSONArray ja = new JSONArray();
			
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_dbParams()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						String resp = rs.getString("resp");
						JSONObject jo = new JSONObject(resp);
						ja.put(jo);
					}
				}
				dbConn.close();
			}
			
			return ja.toString();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}

	}
	
	public Map<String, PGPending> getActiveGroup() {
		final String module = "getActiveGroup()";
		String logmsg = module + " - ";
		Map<String, PGPending> mp = new HashMap<>();
		
		try {
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_groupActiveServer("+ gParams.getMapMonParams().get(gParams.getMonID()).getAgeGapMinute() +")";
				logger.info(logmsg+"Ejecutando: "+vSql);
				if (dbConn.executeQuery(vSql)) {
					logger.info(logmsg+"Ejecucion Exitosa");
					logger.info(logmsg+"Recuperando Grupos y procesos asociados...");
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						PGPending pg = new PGPending();
						pg.setFecIns(rs.getDate("FECINS"));
						pg.setGrpID(rs.getString("GRPID"));
						pg.setnOrder(rs.getInt("NORDER"));
						pg.setNumSecExec(rs.getString("NUMSECEXEC"));
						pg.setProcID(rs.getString("PROCID"));
						pg.setStatus(rs.getString("STATUS"));
						pg.setTypeProc(rs.getString("TYPEPROC"));
						
						String key = pg.getGrpID()+":"+pg.getNumSecExec()+":"+pg.getProcID();
						mp.put(key, pg);
					}
				} else {
					logger.info(logmsg+"No pudo ejecutar query");
				}
				dbConn.close();
			} else {
				logger.error(logmsg+"No pudo conectarse a Metadata");
				//setDBAccess(false);
			}
			return mp;
		} catch (Exception e) {
			//setDBAccess(false);
			logger.error(logmsg+"Error general: "+e.getMessage());
			return null;
		} finally {
			try {
				if (dbConn.isConnected()) {
					dbConn.close(); 
				}
			} catch (Exception e) {}
		}
	}

	public Object getProcessParam(String procID, String procType)  {
		final String module="getProcessParam()";
		String logmsg = module+" - ";
		
		try {
			String vSql="";
			Object params = null;
			
			logger.info(logmsg+"Recuperando parametros de proceso "+procType+" "+procID);
			
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				logger.info(logmsg+"Conexion establecida a Metadata!");
				switch(procType) {
					case "ETL":
						break;
					case "MOV":
						Mov mov = new Mov();
						List<MovMatch> lstMovMatch = new ArrayList<>();
						
						vSql = "call sp_get_mov('"+procID+"')";
						logger.info(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								try {
									String resp = rs.getString("resp");
									logger.info(logmsg+"Respuesta de la query: "+resp);
									
									logger.info(logmsg+"Serializando respuesta en objeto clase Mov()");
									mov = (Mov) mylib.serializeJSonStringToObject(resp, Mov.class);
									
									logger.info(logmsg+"Se ha serializado correctamente el objeto clase Mov()");
									
									
									//Busca Match de Campos

									String vSql2 = "call sp_get_movMatch('"+procID+"')";
									logger.info(logmsg+"Ejecutando query: "+vSql2);
									if (dbConn.executeQuery(vSql2)) {
										ResultSet rs2 = dbConn.getQuery();
										while (rs2.next()) {
											MovMatch movMatch = new MovMatch();
											resp = rs2.getString("resp");
											logger.info(logmsg+"Respuesta de la query: "+resp);
											
											logger.info(logmsg+"Serializando respuesta en objeto clase MovMatch()");
											movMatch = (MovMatch) mylib.serializeJSonStringToObject(resp, MovMatch.class);
									
											logger.info(logmsg+"Se ha serializado correctamente el objeto clase MovMatch()");
											
											lstMovMatch.add(movMatch);
										}
									}
									
									//Asignacion de Lista de Campos al Mov
									mov.setLstMovMatch(lstMovMatch);
									
									//Asigancion de objeto de respuesta
									params = mov;
								} catch (Exception e) {
									logger.error(logmsg+"No es posible recuperar parametros de proceso: "+e.getLocalizedMessage());
								}
							}
						}
						
						break;
					case "OSP":
						Osp osp = new Osp();
						Map<String, OspParam> mapOspParam = new TreeMap<>();
						
						vSql = "call sp_get_osp('"+procID+"')";
						logger.info(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								try {
									String resp = rs.getString("resp");
									logger.info(logmsg+"Respuesta de la query: "+resp);
									
									logger.info(logmsg+"Serializando respuesta en objeto clase Osp()");
									osp = (Osp) mylib.serializeJSonStringToObject(resp, Osp.class);
									
									logger.info(logmsg+"Se ha serializado correctamente el objeto clase Osp()");
									
									//Busca Parametros del OSP
									String vSql2 = "call sp_get_ospParams('"+procID+"')";
									logger.info(logmsg+"Ejecutando query: "+vSql2);
									if (dbConn.executeQuery(vSql2)) {
										ResultSet rs2 = dbConn.getQuery();
										while (rs2.next()) {
											OspParam ospParam = new OspParam();
											resp = rs2.getString("resp");
											logger.info(logmsg+"Respuesta de la query: "+resp);
											
											logger.info(logmsg+"Serializando respuesta en objeto clase OspParams()");
											ospParam = (OspParam) mylib.serializeJSonStringToObject(resp, OspParam.class);
											
											logger.info(logmsg+"Se ha serializado correctamente el objeto clase OspParams()");
											
											mapOspParam.put(String.format("%30d", ospParam.getOrder()), ospParam);
										}
									}
									
									osp.setMapOspParam(mapOspParam);
									
									params = osp;
								} catch (Exception e) {
									logger.error(logmsg+"No es posible recuperar parametros de proceso: "+e.getLocalizedMessage());
								}
							}
						}
						
						break;
					default:
						logger.error("getProcessParam: tipo de proceso no encontrado");
				}
				
				dbConn.close();
			}
			return params;
			
		} catch (Exception e) {
			logger.error(logmsg+"Error general: "+e.getMessage());
			return null;
		} finally {
			try {
				if (dbConn.isConnected()) {
					dbConn.close(); 
				}
			} catch (Exception e) {}
		}
	}

	public void updateProcControl(String key, String status, int errCode, String errMesg) throws Exception {
		try {
			dbConn.open();
			if (dbConn.isConnected()) {
				String[] tokens = key.split(":");
				String vGrpID = tokens[0];
				String vNumSecExec = tokens[1];
				String vProcID = tokens[2];
				String vSql = "call srvConf.sp_upd_procControl('"+vGrpID+"','"+vNumSecExec+"','"+vProcID+"','"+status+"',"+errCode+",'"+errMesg+"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						int result = rs.getInt("resp");
						if (result<0) {
							logger.error("updateProcControl: No es posible actualizar Metadata para ID: "+key);
						}
					}
				}
				dbConn.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
	}

}
