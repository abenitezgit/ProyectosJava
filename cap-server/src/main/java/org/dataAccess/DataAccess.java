package org.dataAccess;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.model.AgeGroupStat;
import org.model.Category;
import org.model.Client;
import org.model.DBase;
import org.model.Dependence;
import org.model.ExpTable;
import org.model.ExpTableParam;
import org.model.Ftp;
import org.model.Group;
import org.model.LoadTable;
import org.model.LoadTableParam;
import org.model.LogMessage;
import org.model.Mov;
import org.model.MovMatch;
import org.model.Osp;
import org.model.OspParam;
import org.model.PGPending;
import org.model.ProcControl;
import org.model.ProcGroup;
import org.utilities.GlobalParams;

import com.api.MysqlAPI;
import com.model.SPparam;
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
	
	public boolean addDBresources(String method, Object param) throws Exception {
		try {
			List<SPparam> spParams = new ArrayList<>();
			String spName = "";
			boolean resultStat = false;
			
			switch(method) {
				case "addGroup":
					JSONObject joGroup = (JSONObject) param;
					String strGroup = joGroup.toString();
					Group group = (Group) mylib.serializeJSonStringToObject(strGroup, Group.class);
					
					spParams.add(new SPparam(group.getGrpID()));
					spParams.add(new SPparam(group.getGrpDesc()));
					spParams.add(new SPparam(group.getEnable()));
					spParams.add(new SPparam(group.getHorID()));
					spParams.add(new SPparam(group.getGrpID()));
					spParams.add(new SPparam(group.getCliID()));
					spParams.add(new SPparam(group.getMaxTimeExec()));
					spParams.add(new SPparam(group.getTypeBalance()));
					spParams.add(new SPparam(group.getTypeRequest()));
					spParams.add(new SPparam(group.getCatID()));
					
					spName = "sp_add_group";
					
					break;
				case "addProcGroup":
					JSONObject joProcGroup = (JSONObject) param;
					String strProcGroup = joProcGroup.toString();
					ProcGroup procGroup = (ProcGroup) mylib.serializeJSonStringToObject(strProcGroup, ProcGroup.class);
					
					spParams.add(new SPparam(procGroup.getGrpID()));
					spParams.add(new SPparam(procGroup.getProcID()));
					spParams.add(new SPparam(procGroup.getnOrder()));
					spParams.add(new SPparam(procGroup.getEnable()));
					spParams.add(new SPparam(procGroup.getType()));
					
					spName = "sp_add_procGroup";
					
					break;
				case "addClient":
					JSONObject joClient = (JSONObject) param;
					String strClient = joClient.toString();
					Client client = (Client) mylib.serializeJSonStringToObject(strClient, Client.class);
					
					spParams.add(new SPparam(client.getCliID()));
					spParams.add(new SPparam(client.getCliDesc()));
					
					spName = "sp_add_client";
					
					break;
				case "addCategory":
					JSONObject joCat = (JSONObject) param;
					String strCat = joCat.toString();
					Category cat = (Category) mylib.serializeJSonStringToObject(strCat, Category.class);
					
					spParams.add(new SPparam(cat.getCatID()));
					spParams.add(new SPparam(cat.getCatDesc()));
					spParams.add(new SPparam(cat.getEnable()));
					
					spName = "sp_add_category";
					
					break;
				case "addDBase":
					JSONObject jodb = (JSONObject) param;
					String strDb = jodb.toString();
					DBase db = (DBase) mylib.serializeJSonStringToObject(strDb, DBase.class);
					
					spParams.add(new SPparam(db.getDbID()));
					spParams.add(new SPparam(db.getDbDesc()));
					spParams.add(new SPparam(db.getDbPort()));
					spParams.add(new SPparam(db.getDbInstance()));
					spParams.add(new SPparam(db.getDbName()));
					spParams.add(new SPparam(db.getDbType()));
					spParams.add(new SPparam(db.getDbJDBCString()));
					spParams.add(new SPparam(db.getDbName()));
					spParams.add(new SPparam(db.getDbFileConf()));
					spParams.add(new SPparam(db.getDbEnable()));
					
					spName = "sp_add_dbase";
					
					break;
				case "addDependence":
					JSONObject jodep = (JSONObject) param;
					String strDep = jodep.toString();
					Dependence dep = (Dependence) mylib.serializeJSonStringToObject(strDep, Dependence.class);
					
					spParams.add(new SPparam(dep.getGrpID()));
					spParams.add(new SPparam(dep.getCritical()));
					spParams.add(new SPparam(dep.getProcHijo()));
					spParams.add(new SPparam(dep.getProcPadre()));
					
					spName = "sp_add_depProc";
					
					break;
			}
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				if (dbConn.executeProcedure(spName, spParams)) {
					resultStat = true;
				}
				
				dbConn.close();
			}
			
			return resultStat;
		} catch (Exception e) {
			throw new Exception("addDBresource(): "+e.getMessage());
		}
	}
	
	public boolean deleteDBresources(String method, Object params) throws Exception {
		try {
			List<SPparam> spParams = new ArrayList<>();
			String spName = "";

			boolean exitStatus = false;
			
			switch(method) {
				case "deleteGroupControl":
					JSONObject joGroup = (JSONObject) params;
					
					spParams.add(new SPparam(joGroup.getString("grpID")));
					spParams.add(new SPparam(joGroup.getString("numSecExec")));
					
					spName = "sp_del_groupControl";

					break;
			}
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				if (dbConn.executeProcedure(spName, spParams)) {
					exitStatus = true;
				}
				
				dbConn.close();
			}

			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	
	public List<Map<String,Object>> getDBresources(String method, Object params) throws Exception {
		List<Map<String,Object>> rows = new ArrayList<>();
		try {
			
			List<SPparam> spParams = new ArrayList<>();
			String param="";
			
			JSONArray ja = new JSONArray();
			JSONObject joParams = new JSONObject();
			String spName="";
			
			switch (method) {
				case "getDBGroup":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbGroup";
					break;
				case "getDBprocGroup":
					joParams = (JSONObject) params;
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbprocGroup";
					break;
				case "getDBschedule":
					joParams = (JSONObject) params;
					param = joParams.getString("horID");
					spParams.add(new SPparam(param));
					spName = "sp_get_schedule";
					break;
				case "getDBschedDiary":
					joParams = (JSONObject) params;
					param = joParams.getString("horID");
					spParams.add(new SPparam(param));
					spName = "sp_get_schedDiary";
					break;
				case "getDBdiary":
					joParams = (JSONObject) params;
					param = joParams.getString("ageID");
					spParams.add(new SPparam(param));
					spName = "sp_get_diary";
					break;
				case "getDBbase":
					joParams = (JSONObject) params;
					param = joParams.getString("dbID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbbase";
					break;
				case "getDBgroupControl":
					joParams = (JSONObject) params;
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbGroupControl";
					break;
				case "getDBprocControl":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("grpID")));
					spParams.add(new SPparam(joParams.getString("numSecExec")));
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_dbProcControl";
					break;
				case "getDBclient":
					joParams = (JSONObject) params;
					param = joParams.getString("cliID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbClient";
					break;
				case "getDBcategory":
					joParams = (JSONObject) params;
					param = joParams.getString("catID");
					spParams.add(new SPparam(param));
					spName = "sp_get_category";
					break;
				case "getDBprocDep":
					joParams = (JSONObject) params;
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dependences";
					break;
			}
			
			dbConn.open();
			if (dbConn.isConnected()) {
				if (dbConn.executeProcedure(spName, spParams)) {
					ResultSet rs = dbConn.getSpResult();
					ResultSetMetaData rsm = rs.getMetaData();
					
					while (rs.next()) {
						JSONObject jo = new JSONObject();
						Map<String,Object> cols = new HashMap<>();

						for (int i=1; i<=rsm.getColumnCount(); i++) {
							switch(rsm.getColumnType(i)) {
							case java.sql.Types.VARCHAR:
								jo.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								cols.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								break;
							case java.sql.Types.INTEGER:
								jo.put(rsm.getColumnLabel(i), rs.getInt(rsm.getColumnLabel(i)));
								cols.put(rsm.getColumnLabel(i), rs.getInt(rsm.getColumnLabel(i)));
								break;
							default:
								jo.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								cols.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								break;
							}
						}
						
						rows.add(cols);
						ja.put(jo);
					}
					rs.close();
				}
				dbConn.close();
			} 			
			
			
			//return ja.toString();
			//return mylib.serializeObjectToJSon(rows, false);
			return rows;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
					rs.close();
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
	
	public List<Map<String,Object>> getAgeGroupDayHourAlert(int vDay, int vHour) throws Exception {
		try {
			Map<String,Object> cols = new HashMap<>();
			List<Map<String,Object>> lstRows = new ArrayList<>();
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_ageGroupDayHourStat("+vDay+","+vHour+")";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					
					Calendar cal = Calendar.getInstance();
					int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
					int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
					int minuteOfHour = cal.get(Calendar.MINUTE);
					
					while (rs.next()) {
						
						cols = new HashMap<>();
						
						String grpID = rs.getString("grpID");
						String numSecExec = rs.getString("numSecExec");
						int day = rs.getInt("nDay");
						int hour = rs.getInt("nHour");
						int minute = rs.getInt("nMinute");
						String uStatus = Objects.isNull(rs.getString("uStatus")) ? "NULL":rs.getString("uStatus") ;
						String typeExec = Objects.isNull(rs.getString("typeExec")) ? "NULL":rs.getString("typeExec") ;
						
						if (!uStatus.equals("SUCESS")) {
							if (day<dayOfMonth) {
								cols.put("grpID", grpID);
								cols.put("numSecExec", numSecExec);
								cols.put("uStatus", uStatus);
								cols.put("typeExec", typeExec);
								lstRows.add(cols);
							} else if (day==dayOfMonth) {
								if (hour<hourOfDay) {
									cols.put("grpID", grpID);
									cols.put("numSecExec", numSecExec);
									cols.put("uStatus", uStatus);
									cols.put("typeExec", typeExec);
									lstRows.add(cols);
								} else if (hour==hourOfDay) {
									if (minute<minuteOfHour) {
										cols.put("grpID", grpID);
										cols.put("numSecExec", numSecExec);
										cols.put("uStatus", uStatus);
										cols.put("typeExec", typeExec);
										lstRows.add(cols);
									}
								}
							}
						} else if (typeExec.equals("MANUAL")) {
							cols.put("grpID", grpID);
							cols.put("numSecExec", numSecExec);
							cols.put("uStatus", uStatus);
							cols.put("typeExec", typeExec);
							lstRows.add(cols);
						}
					}
				}
			}
			
			return lstRows;
		} catch (Exception e) {
			throw new Exception("getAgeGroupDayHourAlert(): "+e.getMessage());
		}
	}
	
	public List<Map<String,Object>> getAgeGroupStat() throws Exception {

		Map<String, AgeGroupStat> mapAgeGroupStat = new TreeMap<>();
		
		try {
			
			int initDay;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				
				String vSql0 = "call sp_get_ageGroupDay(0)";
				
				if (dbConn.executeQuery(vSql0)) {
					
					String vSql = "call srvConf.sp_get_ageGroupStat()";
					if (dbConn.executeQuery(vSql)) {
						ResultSet rs = dbConn.getQuery();
						
						boolean firstRow = true;
						
						Calendar cal = Calendar.getInstance();
						int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
						int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
						int minuteOfHour = cal.get(Calendar.MINUTE);
						
						while (rs.next()) {
							
							int day = rs.getInt("nDay");
							int hour = rs.getInt("nHour");
							int minute = rs.getInt("nMinute");
							int numGroups = rs.getInt("nCount");
							String uStatus = Objects.isNull(rs.getString("uStatus")) ? "NULL":rs.getString("uStatus") ;
							String typeExec = Objects.isNull(rs.getString("typeExec")) ? "NULL":rs.getString("typeExec") ;
							
							if (firstRow) {
								initDay = day;
								firstRow = false;
							}
		
							String key = String.format("%02d", day)+":"+String.format("%02d", hour);
							
							AgeGroupStat ags = new AgeGroupStat();
							
							if (mapAgeGroupStat.containsKey(key)) {
								ags = mapAgeGroupStat.get(key);
							}
							
							ags.setnGroups(ags.getnGroups()+numGroups);
							
							if (!uStatus.equals("SUCESS")) {
								if (day<dayOfMonth) {
									ags.setAlerts(ags.getAlerts()+numGroups);
								} else if (day==dayOfMonth) {
									if (hour<hourOfDay) {
										ags.setAlerts(ags.getAlerts()+numGroups);
									} else if (hour==hourOfDay) {
										if (minute<minuteOfHour) {
											ags.setAlerts(ags.getAlerts()+numGroups);
										}
									}
								}
							} else if (typeExec.equals("MANUAL")) {
								ags.setAlerts(ags.getAlerts()+numGroups);
							}
							
							ags.setnDay(day);
							ags.setnHour(hour);
							
							mapAgeGroupStat.put(key, ags);
							
							//System.out.println(mylib.serializeObjectToJSon(mapAgeGroupStat.get(key), false));
							
						}
						//System.out.println(dayOfMonth);
						
					}
				} else {
					throw new Exception("getAgeGroupWeek(): No es posible ejecutar SP");
					//System.out.println("Error");
				}
				dbConn.close();
			}

			int itDay = 0;
			Map<String, AgeGroupStat> mapAgeGrStatFinal = new TreeMap<>();
			
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGroupStat.entrySet()) {
				int Day = Integer.valueOf(entry.getKey().split(":")[0]);
				
				if (itDay!=Day) {
					for (int i=0; i<=23; i++) {
						String getKey = entry.getKey().split(":")[0]+":"+String.format("%02d", i);
						System.out.println(getKey);
						if (!mapAgeGroupStat.containsKey(getKey)) {
							AgeGroupStat ags = new AgeGroupStat();
							ags.setAlerts(0);
							ags.setnDay(Day);
							ags.setnGroups(0);
							ags.setnHour(i);
							mapAgeGrStatFinal.put(getKey, ags);
						} else {
							mapAgeGrStatFinal.put(getKey, mapAgeGroupStat.get(getKey));
						}
					}
					itDay = Day;
				}
			}
			
			List<Map<String,Object>> lstObjects = new ArrayList<>();
			
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			
			return lstObjects;
		} catch (Exception e) {
			throw new Exception("getAgeGroupWeek(): "+e.getMessage());
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
					rs.close();
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
					rs.close();
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
					rs.close();
				} else {
					throw new Exception("getGroupParam(): No es posible ejecutar query");
				}
				dbConn.close();
			} else {
				throw new Exception("getGroupParam(): No es posible conectarse a MD");
			}

			return group;
		} catch (Exception e) {
			throw new Exception("getGroupParam(): "+e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close();
			}
		}
	}
	
	public void addMyLog(LogMessage lgm) throws Exception {
		try {
			dbConn.open();
			
			if (dbConn.isConnected()) {
				
				List<SPparam> spParams = new ArrayList<>();
				spParams.add(new SPparam(lgm.getLogID()));
				spParams.add(new SPparam(lgm.getMessageID()));
				spParams.add(new SPparam(lgm.getTimesTamp()));
				spParams.add(new SPparam(lgm.getAppName()));
				spParams.add(new SPparam(lgm.getLoggerName()));
				spParams.add(new SPparam(lgm.getLogType()));
				spParams.add(new SPparam(lgm.getMessageText()));
				spParams.add(new SPparam(lgm.getModuleName()));

				if (!dbConn.executeProcedure("sp_add_myLog", spParams)) {
					logger.error("No es posible guardar log en BD");
				}
				
				dbConn.close();
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, PGPending> addGroupActiveManual(String grpID) {
		final String module = "addGroupActiveManual()";
		String logmsg = module + " - ";
		Map<String, PGPending> mp = new HashMap<>();
		
		try {
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_add_groupActiveManual('"+ grpID +"')";
				logger.info(logmsg+"Ejecutando: "+vSql);
				if (dbConn.executeQuery(vSql)) {
					logger.info(logmsg+"Ejecucion Exitosa");
					logger.info(logmsg+"Recuperando Grupos y procesos asociados...");
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						PGPending pg = new PGPending();
						pg.setFecIns(mylib.getDate());
						pg.setGrpID(rs.getString("GRPID"));
						pg.setnOrder(rs.getInt("NORDER"));
						pg.setNumSecExec(rs.getString("NUMSECEXEC"));
						pg.setProcID(rs.getString("PROCID"));
						pg.setStatus(rs.getString("STATUS"));
						pg.setTypeProc(rs.getString("TYPEPROC"));
						pg.setCliID(rs.getString("CLIID"));
						pg.setCliDesc(rs.getString("CLIDESC"));
						pg.setTypeExec(rs.getString("TYPEEXEC"));
						
						String key = pg.getGrpID()+":"+pg.getNumSecExec()+":"+pg.getProcID();
						mp.put(key, pg);
					}
					rs.close();
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
	
	public Map<String, PGPending> getActiveGroup() throws Exception {
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
						pg.setFecIns(mylib.getDate());
						pg.setGrpID(rs.getString("GRPID"));
						pg.setnOrder(rs.getInt("NORDER"));
						pg.setNumSecExec(rs.getString("NUMSECEXEC"));
						pg.setProcID(rs.getString("PROCID"));
						pg.setStatus(rs.getString("STATUS"));
						pg.setTypeProc(rs.getString("TYPEPROC"));
						pg.setCliID(rs.getString("CLIID"));
						pg.setCliDesc(rs.getString("CLIDESC"));
						pg.setTypeExec(rs.getString("TYPEEXEC"));
						
						String key = pg.getGrpID()+":"+pg.getNumSecExec()+":"+pg.getProcID();
						mp.put(key, pg);
					}
					rs.close();
				} else {
					throw new Exception("getActiveGroup(): No es posible ejecutar Query");
				}
				dbConn.close();
			} else {
				throw new Exception("getActiveGroup(): No es posible conectarse de MD");
			}
			return mp;
		} catch (Exception e) {
			throw new Exception("getActiveGroup(): "+e.getMessage());
		} finally {
			try {
				if (dbConn.isConnected()) {
					dbConn.close(); 
				}
			} catch (Exception e) {
				throw new Exception("getActiveGroup(): No es posible cerra conexión a MD");
			}
		}
	}

	public Object getProcessParam(String procID, String procType) throws Exception {
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
								String resp = rs.getString("resp");
								logger.info(logmsg+"Respuesta de la query: "+resp);
								
								logger.info(logmsg+"Serializando respuesta en objeto clase Ftp()");
								ftp = (Ftp) mylib.serializeJSonStringToObject(resp, Ftp.class);
								
								logger.info(logmsg+"Se ha serializado correctamente el objeto clase Ftp()");
																	
								params = ftp;
							}
							rs.close();
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
									rs2.close();
								}
								
								//Asignacion de Lista de Campos al Mov
								mov.setLstMovMatch(lstMovMatch);
								
								//Asigancion de objeto de respuesta
								params = mov;
							}
							rs.close();
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
									rs2.close();
								}
								
								osp.setMapOspParam(mapOspParam);
								
								params = osp;
							}
							rs.close();
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
									rs2.close();
								}
								
								etb.setMapEtbParam(mapEtbParam);
								
								params = etb;
							}
							rs.close();
						}
						
						break;

					case "LTB":
						LoadTable ltb = new LoadTable();
						Map<Integer, LoadTableParam> mapLtbParam = new TreeMap<>();
						
						vSql = "call sp_get_ltb('"+procID+"')";
						logger.info(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.info(logmsg+"Respuesta de la query: "+resp);
								
								logger.info(logmsg+"Serializando respuesta en objeto clase LoadTable()");
								ltb = (LoadTable) mylib.serializeJSonStringToObject(resp, LoadTable.class);
								
								logger.info(logmsg+"Se ha serializado correctamente el objeto clase LoadTable()");
								
								//Busca Parametros del LoadTable
								String vSql2 = "call sp_get_ltbMatch('"+procID+"')";
								logger.info(logmsg+"Ejecutando query: "+vSql2);
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										LoadTableParam ltbParam = new LoadTableParam();
										resp = rs2.getString("resp");
										logger.info(logmsg+"Respuesta de la query: "+resp);
										
										logger.info(logmsg+"Serializando respuesta en objeto clase LoadTableParam()");
										ltbParam = (LoadTableParam) mylib.serializeJSonStringToObject(resp, LoadTableParam.class);
										
										logger.info(logmsg+"Se ha serializado correctamente el objeto clase LoadTableParam()");
										
										mapLtbParam.put(ltbParam.getFileLoadOrder(), ltbParam);
									}
									rs2.close();
								}
								
								ltb.setMapLtbParam(mapLtbParam);
								
								params = ltb;
							}
							rs.close();
						}
						
						break;
					default:
						throw new Exception("getProcessParam(): Tipoe de Proceso no encontrado");
				}
				
				dbConn.close();
			}
			return params;
			
		} catch (Exception e) {
			throw new Exception("getProcessParam(): "+e.getMessage());
		} finally {
			try {
				if (dbConn.isConnected()) {
					dbConn.close(); 
				}
			} catch (Exception e) {
				throw new Exception("getProcessParam(): "+e.getMessage());
			}
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
				List<SPparam> spParams = new ArrayList<>();
				spParams.add(new SPparam(vGrpID));
				spParams.add(new SPparam(vNumSecExec));
				spParams.add(new SPparam(vProcID));
				spParams.add(new SPparam(uStatus));
				spParams.add(new SPparam(errCode));
				spParams.add(new SPparam(errMesg));
				
				for (SPparam param : spParams) {
					logger.debug("List Param: "+param.getValue());
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
			List<SPparam> spParams = new ArrayList<>();
			spParams.add(new SPparam(items[0]));
			spParams.add(new SPparam(items[1]));
			spParams.add(new SPparam(status));
			spParams.add(new SPparam(uStatus));
			spParams.add(new SPparam(errCode));
			spParams.add(new SPparam(errMesg));
			
			logger.info("SP Group Update Name: "+spName);
			for (SPparam param : spParams) {
				logger.info("SP Group Update Param: "+param.getValue());
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
			List<SPparam> spParams = new ArrayList<>();
			spParams.add(new SPparam(pc.getGrpID()));
			spParams.add(new SPparam(pc.getNumSecExec()));
			spParams.add(new SPparam(pc.getProcID()));
			spParams.add(new SPparam(pc.getuStatus()));
			spParams.add(new SPparam(pc.getErrCode()));
			spParams.add(new SPparam(pc.getErrMesg()));
			
			if(pc.getTxResult()!=null) {
				String strTxResult = "";
				switch(pc.getTypeProc()) {
					case "MOV":
						String strMovResult = mylib.serializeObjectToJSon(pc.getTxResult(), false);
						strTxResult = strMovResult;
						break;
					case "LTB":
						String strLtbResult = mylib.serializeObjectToJSon(pc.getTxResult(), false);
						strTxResult = strLtbResult;
						break;
					case "ETB":
						String strEtbResult = mylib.serializeObjectToJSon(pc.getTxResult(), false);
						strTxResult = strEtbResult;
						break;
					default:
						break;
				}
				
				spParams.add(new SPparam(strTxResult));
			} else {
				spParams.add(new SPparam(""));
			}
			
			logger.info("SP Proc Update Name: "+spName);
			for (SPparam param : spParams) {
				logger.info("SP Proc Update Param: "+param.getValue());
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
