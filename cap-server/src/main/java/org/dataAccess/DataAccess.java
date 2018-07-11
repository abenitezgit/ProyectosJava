package org.dataAccess;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.Dependence;
import org.model.ExpTable;
import org.model.ExpTableParam;
import org.model.Ftp;
import org.model.Group;
import org.model.Mov;
import org.model.MovMatch;
import org.model.Osp;
import org.model.OspParam;
import org.model.PGPending;
import org.model.ProcControl;
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
		dbConn = new MysqlAPI(	gParams.getAppConfig().getDbHostName(), 
								gParams.getAppConfig().getDbName(), 
								String.valueOf(gParams.getAppConfig().getDbPort()),
								gParams.getAppConfig().getDbUser(),
								gParams.getAppConfig().getDbPass(),
								gParams.getAppConfig().getDbTimeOut());
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
	
	public String getDBprocGroup(String grpID) throws Exception {
		try {
			JSONArray ja = new JSONArray();
			
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_dbprocGroup('"+grpID+"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					ResultSetMetaData rsm = rs.getMetaData();
					
					while (rs.next()) {
						JSONObject jo = new JSONObject();

						for (int i=1; i<=rsm.getColumnCount(); i++) {
							switch(rsm.getColumnType(i)) {
							case java.sql.Types.VARCHAR:
								jo.put(rsm.getColumnName(i), rs.getString(rsm.getColumnName(i)));
								break;
							case java.sql.Types.INTEGER:
								jo.put(rsm.getColumnName(i), rs.getInt(rsm.getColumnName(i)));
								break;
							default:
								jo.put(rsm.getColumnName(i), rs.getString(rsm.getColumnName(i)));
								break;
							}
						}
						
						ja.put(jo);
					}
				}
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
	
	public String getDBGroup(String grpID) throws Exception {
		try {
			JSONArray ja = new JSONArray();
			
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_dbGroup('"+grpID+"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					ResultSetMetaData rsm = rs.getMetaData();
					
					while (rs.next()) {
						JSONObject jo = new JSONObject();

						for (int i=1; i<=rsm.getColumnCount(); i++) {
							switch(rsm.getColumnType(i)) {
							case java.sql.Types.VARCHAR:
								jo.put(rsm.getColumnName(i), rs.getString(rsm.getColumnName(i)));
								break;
							case java.sql.Types.INTEGER:
								jo.put(rsm.getColumnName(i), rs.getInt(rsm.getColumnName(i)));
								break;
							default:
								jo.put(rsm.getColumnName(i), rs.getString(rsm.getColumnName(i)));
								break;
							}
						}
						
						ja.put(jo);
					}
				}
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
	
	public List<Dependence> getProcDependences(String grpID, String procID) throws Exception {
		try {
			List<Dependence> lstDep = new ArrayList<>();
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_procDependences('"+grpID+"','"+procID+"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						Dependence d = new Dependence();
						d.setGrpID(rs.getString("grpID"));
						d.setCritical(rs.getInt("critical"));
						d.setProcHijo(rs.getString("procHijo"));
						d.setProcPadre(rs.getString("procPadre"));
						
						lstDep.add(d);
					}
				}
			} 			
			
			return lstDep;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}

	}
	
	public String getServiceParam(String srvID) throws Exception {
		try {
			String response="";
			
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_service('"+srvID+"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						response = rs.getString("resp");
					}
				}
				dbConn.close();
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
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
	
	public Group getGroupParam(String grpID) throws Exception {
		final String module = "getGroupParam()";
		String logmsg = module + " - ";
		
		try {
			Group group = new Group();
			
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_groupParam('"+grpID+"')"; 
				logger.info(logmsg+"Ejecutando query: "+vSql);
				if (dbConn.executeQuery(vSql)) {
					logger.info(logmsg+"Ejecucion Exitosa");
					logger.info(logmsg+"Recuperando parametros del grupo...");
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						String strGroup = rs.getString("resp");
						group = (Group) mylib.serializeJSonStringToObject(strGroup, Group.class);
					}
				}
			}

			return group;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
				String vSql = "call srvConf.sp_get_groupActiveServer("+ gParams.getMapMonParams().get(gParams.getAppConfig().getMonID()).getAgeGapMinute() +")";
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
						pg.setCliID(rs.getString("CLIID"));
						pg.setCliDesc(rs.getString("CLIDESC"));
						
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
			
			logger.info(logmsg+"Recuperando parámetros de proceso "+procType+" "+procID);
			
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				logger.info(logmsg+"Conexión establecida a Metadata!");
				switch(procType) {
					case "FTP":
						Ftp ftp = new Ftp();
						
						vSql = "call sp_get_ftp('"+procID+"')";
						logger.info(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								try {
									String resp = rs.getString("resp");
									logger.info(logmsg+"Respuesta de la query: "+resp);
									
									logger.info(logmsg+"Serializando respuesta en objeto clase Ftp()");
									ftp = (Ftp) mylib.serializeJSonStringToObject(resp, Ftp.class);
									
									logger.info(logmsg+"Se ha serializado correctamente el objeto clase Ftp()");
																		
									params = ftp;
								} catch (Exception e) {
									logger.error(logmsg+"No es posible recuperar parametros de proceso: "+e.getLocalizedMessage());
								}
							}
						}
						
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
									logger.error(logmsg+"No es posible recuperar parámetros de proceso: "+e.getLocalizedMessage());
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
											
											mapOspParam.put(String.format("%03d", ospParam.getOrder()), ospParam);
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
					case "ETB":
						ExpTable etb = new ExpTable();
						Map<String, ExpTableParam> mapEtbParam = new TreeMap<>();
						
						vSql = "call sp_get_exp('"+procID+"')";
						logger.info(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								try {
									String resp = rs.getString("resp");
									logger.info(logmsg+"Respuesta de la query: "+resp);
									
									logger.info(logmsg+"Serializando respuesta en objeto clase ExpTable()");
									etb = (ExpTable) mylib.serializeJSonStringToObject(resp, ExpTable.class);
									
									logger.info(logmsg+"Se ha serializado correctamente el objeto clase ExpTable()");
									
									//Busca Parametros del ExpTable
									String vSql2 = "call sp_get_expMatch('"+procID+"')";
									logger.info(logmsg+"Ejecutando query: "+vSql2);
									if (dbConn.executeQuery(vSql2)) {
										ResultSet rs2 = dbConn.getQuery();
										while (rs2.next()) {
											ExpTableParam etbParam = new ExpTableParam();
											resp = rs2.getString("resp");
											logger.info(logmsg+"Respuesta de la query: "+resp);
											
											logger.info(logmsg+"Serializando respuesta en objeto clase ExpTableParam()");
											etbParam = (ExpTableParam) mylib.serializeJSonStringToObject(resp, ExpTableParam.class);
											
											logger.info(logmsg+"Se ha serializado correctamente el objeto clase ExpTableParam()");
											
											mapEtbParam.put(String.format("%03d", etbParam.getEtbOrder()), etbParam);
										}
									}
									
									etb.setMapEtbParam(mapEtbParam);
									
									params = etb;
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

	public void updateProcControl(String key, String uStatus, int errCode, String errMesg) throws Exception {
		try {
			dbConn.open();
			if (dbConn.isConnected()) {
				String[] tokens = key.split(":");
				String vGrpID = tokens[0];
				String vNumSecExec = tokens[1];
				String vProcID = tokens[2];
				
				String spName = "srvConf.sp_upd_procControl";
				List<String> spParams = new ArrayList<>();
				spParams.add("IN&VARCHAR&"+vGrpID);
				spParams.add("IN&VARCHAR&"+vNumSecExec);
				spParams.add("IN&VARCHAR&"+vProcID);
				spParams.add("IN&VARCHAR&"+uStatus);
				spParams.add("IN&INTEGER&"+errCode);
				spParams.add("IN&VARCHAR&"+errMesg);
				
				for (String param : spParams) {
					logger.debug("List Param: "+param);
				}
				
				
				//String vSql = "call srvConf.sp_upd_procControl('"+vGrpID+"','"+vNumSecExec+"','"+vProcID+"','"+status+"',"+errCode+",'"+errMesg+"')";
				dbConn.executeProcedure(spName, spParams);
				
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
	
	public boolean updateGroupControl(String key, String status, String uStatus, int errCode, String errMesg) throws Exception {
		try {
			boolean response = false;
			
			String[] items = key.split(":");  //Sepera grpID y numSecExcec
			
			String spName = "srvConf.sp_upd_groupControl";
			List<String> spParams = new ArrayList<>();
			spParams.add("IN&VARCHAR&"+items[0]);
			spParams.add("IN&VARCHAR&"+items[1]);
			spParams.add("IN&VARCHAR&"+status);
			spParams.add("IN&VARCHAR&"+uStatus);
			spParams.add("IN&INTEGER&"+errCode);
			spParams.add("IN&VARCHAR&"+errMesg);
			
			logger.info("SP Group Update Name: "+spName);
			for (String param : spParams) {
				logger.info("SP Group Update Param: "+param);
			}
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				
				dbConn.executeProcedure(spName, spParams);
				dbConn.close();
				
				response = true;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("updateGroupControl(): "+e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
	}
	
	public boolean updateProcControl(ProcControl pc) throws Exception {
		try {
			boolean response = false;
			
			String spName = "srvConf.sp_upd_procControl";
			List<String> spParams = new ArrayList<>();
			spParams.add("IN&VARCHAR&"+pc.getGrpID());
			spParams.add("IN&VARCHAR&"+pc.getNumSecExec());
			spParams.add("IN&VARCHAR&"+pc.getProcID());
			spParams.add("IN&VARCHAR&"+pc.getuStatus());
			spParams.add("IN&VARCHAR&"+pc.getErrCode());
			spParams.add("IN&VARCHAR&"+pc.getErrMesg());
			
			logger.info("SP Proc Update Name: "+spName);
			for (String param : spParams) {
				logger.info("SP Proc Update Param: "+param);
			}
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				
				dbConn.executeProcedure(spName, spParams);
				dbConn.close();
				
				response = true;
			}
			
			return response;
		} catch (Exception e) {
			throw new Exception("updateProcControl(): "+e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
	}

}
