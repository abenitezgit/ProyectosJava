//package cap.service;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class ThDBAccess extends Thread{
//	static final String MODULE="ThDBAccess";
//	static Logger logger = Logger.getLogger(MODULE);
//	static Rutinas mylib = new Rutinas();
//	static GlobalArea gDatos;
//	static Procedures myproc;
//	
//	//Control de Ejecucion del servicio
//	boolean init;
//	static int TxP;
//	
//	public ThDBAccess(GlobalArea m) {
//		try {
//			gDatos = m;
//			myproc = new Procedures(gDatos);
//			String pathFileLog4j=gDatos.getInfo().getPathProperties()+"/"+gDatos.getInfo().getLogProperties();
//			if (mylib.fileExist(pathFileLog4j)) {
//				PropertyConfigurator.configure(pathFileLog4j);
//				logger.info("Constructor iniciado");
//				logger.info("Logger Name: "+logger.getName());
//				logger.info("Logger Level: "+mylib.getLoggerLevel(logger));
//				logger.setLevel(Level.INFO);
//				logger.info("Logger SET Level: "+mylib.getLoggerLevel(logger));
//				logger.trace("Logger Trace Enable");
//				logger.debug("Logger DEBUG Enable");
//				logger.info("Logger INFO Enable");
//				init = true;
//			} else {
//				mylib.console(1,"Archivo no encontrado: "+pathFileLog4j);
//				init = false;
//			}
//		} catch (Exception e) {
//			mylib.console(1,"Error en constructor "+MODULE+" ("+e.getMessage()+")");
//			init = false;
//		}
//	}
//	
//    @Override
//    public void run() {
//    	if (init) {
//    		TxP = gDatos.getInfo().getTxpDB()/1000;
//	        Timer timerMain = new Timer("thDBAccess");
//	        timerMain.schedule(new mainTask(), 5000, TxP*1000);
//	        logger.info("Servicio "+MODULE+" agendado cada: "+TxP+ " segundos");
//    	} else {
//    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
//    	}
//    }
//    
//    static class mainTask extends TimerTask {
//    	Map<String, Boolean> mapThread = new HashMap<>();
//    	Module module = new Module();
//    	Thread thListener;
//
//        //Constructor de la clase
//        public mainTask() {
//        	module.setName(MODULE);
//        	module.setType("TIMERTASK");
//        	module.setTxp(TxP);
//        }
//        
//        public void run() {
//        	int ciclo=0;
//        	try {
//        		/**
//        		 * Inicia ciclo del Modulo
//        		 */
//        		logger.info("Iniciando ciclo "+MODULE);
//        		module.setLastFecIni(mylib.getDateNow());
//        		gDatos.getMapModule().put(MODULE, module);
//        	
//        		//Recupera Datos de Servicios
//        		loadServicesParam();
//        		
//        		//Sincroniza GroupControl y ProcControl con DB
//        		syncProcessControl();
//        		
//        		mylib.console("services: "+mylib.serializeObjectToJSon(gDatos.getMapService(), false));
//
//        		
//        		/**
//        		 * Finalizando ciclo del Modulo
//        		 */
//        		logger.info("Terminado ciclo "+MODULE);
//        		module.setLastFecFin(mylib.getDateNow());
//        		gDatos.getMapModule().put(MODULE, module);
//
//        	} catch (Exception e) {
//        		switch (ciclo) {
//        		case 1:
//        			logger.error("Error generando lista de agendas "+MODULE+" ("+e.getMessage()+")");
//        			break;
//    			default:
//    				logger.error("Error inesperado "+MODULE+" ("+e.getMessage()+")");
//    				break;
//        		}
//        		/**
//        		 * Finalizando ciclo del Modulo
//        		 */
//        		logger.info("Terminado ciclo "+MODULE+" con Error");
//        	}
//        }
//    }
//    
//    static private void syncProcessControl() throws Exception {
//    	try {
//    		MetaData dbConn = new MetaData(gDatos);
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		
//			//Setea parametro syncMetadata a true
//			gDatos.setSyncMetadata(false);
//    		
//    		//Objetos de paso
//    		Map<String, GroupControl> mgc = new HashMap<>();
//    		Map<String, ProcControl> mpc = new HashMap<>();
//    		String vSql;
//    		
//    		dbConn.open();
//    		
//    		if (dbConn.isConnected()) {
//    			/*
//    			 * Recupera todos los Grupos que tiene status<>'FINISHED'
//    			 */
//    			logger.info("Recupera todos los Grupos que tiene status<>'FINISHED'");
//    			vSql = dbQuery.getSqlFindGroupControl();
//    			if (dbConn.executeQuery(vSql)) {
//    				ResultSet rs = dbConn.getQuery();
//    				GroupControl gc = new GroupControl();
//    				while (rs.next()) {
//    					gc = new GroupControl();
//    					mylib.parseaGroupControl(gc, rs);
//    					String key = rs.getString("grpID")+":"+rs.getString("numSecExec");
//    					mgc.put(key, gc);
//    				}
//    				rs.close();
//    			} else {
//    				//No pudo ejecutar la query
//    			}
//    			
//    			
//    			/**
//    			 * Recupera todos los proceso asociados a los grupos encontrados
//    			 */
//    			logger.info("Recupera todos los proceso asociados a los grupos encontrados");
//    			for (Map.Entry<String, GroupControl> entry : mgc.entrySet()) {
//    				String[] param = entry.getKey().split(":");
//	    			vSql = dbQuery.getSqlFindProcControl(param[0], param[1]);
//	    			if (dbConn.executeQuery(vSql)) {
//	    				ResultSet rs = dbConn.getQuery();
//	    				ProcControl pc = new ProcControl();
//	    				while (rs.next()) {
//	    					pc = new ProcControl();
//	    					mylib.parseaProcControl(pc, rs);
//	    					String key = rs.getString("procID")+":"+rs.getString("numSecExec");
//	    					mpc.put(key, pc);
//	    				}
//	    				rs.close();
//	    			} else {
//	    				//No pudo ejecutar la query
//	    			}
//    			}
//    			
//    			
//    			/**
//    			 * Actualiza mapProcControl desde BD
//    			 */
//    			logger.info("Actualiza Procesos mapProcControl Huerfanos en BD");
//    			if (mpc.size()>0) {
//    				for (Map.Entry<String, ProcControl> entry : mpc.entrySet()) {
//    					if (!gDatos.isExistProcControl(entry.getKey())) {
//    						ProcControl pc = new ProcControl();
//    						pc = entry.getValue();
//    						pc.setErrCode(99);
//    						pc.setErrMesg("proceso huerfano");
//    						pc.setFecFinished(mylib.getDate());
//    						pc.setFecUpdate(mylib.getDate());
//    						pc.setStatus("FINISHED");
//    						pc.setuStatus("ERROR");
//    						updateDBProcControl(dbConn, entry.getKey(), pc);
//    					}
//    				}
//    			}
//
//    			/**
//    			 * Actualiza mapGroupControl desde BD
//    			 */
//    			logger.info("Actualiza Procesos mapGroupControl Huerfanos en BD");
//    			if (mgc.size()>0) {
//    				for (Map.Entry<String, GroupControl> entry : mgc.entrySet()) {
//    					if (!gDatos.isExistGroupControl(entry.getKey())) {
//    						GroupControl gc = new GroupControl();
//    						gc = entry.getValue();
//    						gc.setErrCode(99);
//    						gc.setErrMesg("proceso huerfano");
//    						gc.setFecFinished(mylib.getDate());
//    						gc.setFecUpdate(mylib.getDate());
//    						gc.setStatus("FINISHED");
//    						gc.setuStatus("ERROR");
//    						updateDBGroupControl(dbConn, entry.getKey(), gc);
//    					}
//    				}
//    			}
//    			
//    			/**
//    			 * Actualiza BD desde mapGroupControl
//    			 */
//    			logger.info("Actualiza BD desde mapGroupControl");
//    			if (gDatos.getMapGroupControl().size()>0) {
//    				for (Map.Entry<String, GroupControl> entry : gDatos.getMapGroupControl().entrySet()) {
//    					String[] param = entry.getKey().split(":");
//    					vSql = dbQuery.getSqlIsExistGroupControl(param[0], param[1]);
//    					if (dbConn.executeQuery(vSql)) {
//    						if (dbConn.isExistRows()) {
//    							updateDBGroupControl(dbConn, entry.getKey(), entry.getValue());
//    						} else {
//    							insertDBGroupControl(dbConn, entry.getKey(), entry.getValue());
//    						}
//    					}
//    				}
//    			}
//    			
//    			/**
//    			 * Actualiza BD desde mapProcControl
//    			 */
//    			logger.info("Actualiza BD desde mapProcControl");
//    			if (gDatos.getMapProcControl().size()>0) {
//    				for (Map.Entry<String, ProcControl> entry : gDatos.getMapProcControl().entrySet()) {
//    					String[] param = entry.getKey().split(":");
//    					vSql = dbQuery.getSqlIsExistProcControl(param[0], param[1]);
//    					if (dbConn.executeQuery(vSql)) {
//    						if (dbConn.isExistRows()) {
//    							updateDBProcControl(dbConn, entry.getKey(), entry.getValue());
//    						} else {
//    							insertDBProcControl(dbConn, entry.getKey(), entry.getValue());
//    						}
//    					}
//    				}
//    			}
//
//    			
//    			//Setea parametro syncMetadata a true
//    			gDatos.setSyncMetadata(true);
//    			
//    			dbConn.close();
//    		}
//    		
//    	} catch (Exception e) {
//    		throw new Exception(e.getMessage());
//    	}
//    }
//    
//
//    static private void insertDBProcControl(MetaData dbConn, String key, ProcControl pc) throws Exception {
//    	try {
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		PreparedStatement psInsert;
//    		
//    		String vSql = dbQuery.getSqlInsertProcControl();
//    		psInsert = dbConn.getConnection().prepareStatement(vSql);
//    		psInsert.setString(1, pc.getGrpID());
//    		psInsert.setString(2, pc.getNumSecExec());
//    		psInsert.setString(3, pc.getProcID());
//    		psInsert.setString(4, pc.getTypeProc());
//    		psInsert.setInt(5, pc.getOrder());
//    		psInsert.setTimestamp(6, mylib.getSqlTimestamp(pc.getFecIns()));
//    		psInsert.setTimestamp(7, mylib.getSqlTimestamp(pc.getFecFinished()));
//    		psInsert.setString(8, pc.getStatus());
//    		psInsert.setString(9, pc.getuStatus());
//    		psInsert.setInt(10, pc.getErrCode());
//    		psInsert.setString(11, pc.getErrMesg());
//    		psInsert.setTimestamp(12, mylib.getSqlTimestamp(pc.getFecUpdate()));
//    		
//    		int numInserted = psInsert.executeUpdate();
//    		
//    		if (numInserted<1) {
//    			logger.error("No pudo insertar ProcControl key: "+key);
//    		} else {
//    			logger.info("Se insertó ProcControl key: "+key);
//    		}
//    		
//    	} catch (Exception e) {
//    		throw new Exception(e.getMessage());
//    	}
//    }
//
//    
//    static private void insertDBGroupControl(MetaData dbConn, String key, GroupControl gc) throws Exception {
//    	try {
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		PreparedStatement psInsert;
//    		String[] param = key.split(":");
//    		
//    		String vSql = dbQuery.getSqlInsertGroupControl();
//    		psInsert = dbConn.getConnection().prepareStatement(vSql);
//    		psInsert.setString(1, param[0]);
//    		psInsert.setString(2, param[1]);
//    		psInsert.setString(3, gc.getProcID());
//    		psInsert.setInt(4, gc.getOrder());
//    		psInsert.setTimestamp(5, mylib.getSqlTimestamp(gc.getFecIns()));
//    		psInsert.setTimestamp(6, mylib.getSqlTimestamp(gc.getFecFinished()));
//    		psInsert.setString(7, gc.getStatus());
//    		psInsert.setString(8, gc.getuStatus());
//    		psInsert.setInt(9, gc.getErrCode());
//    		psInsert.setString(10, gc.getErrMesg());
//    		psInsert.setTimestamp(11, mylib.getSqlTimestamp(gc.getFecUpdate()));
//    		
//    		int numInserted = psInsert.executeUpdate();
//    		
//    		if (numInserted<1) {
//    			logger.error("No pudo insertar GroupControl key: "+key);
//    		} else {
//    			logger.info("Se insertó GroupControl key: "+key);
//    		}
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		logger.error("Error insertDBGroupControl ("+e.getMessage()+")");
//    		throw new Exception(e.getMessage());
//    	}
//    }
//
//    static private void updateDBProcControl(MetaData dbConn, String key, ProcControl pc) throws Exception {
//    	try {
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		PreparedStatement psUpdate;
//    		
//    		String vSql = dbQuery.getSqlUpdateProcControl(key);
//    		psUpdate = dbConn.getConnection().prepareStatement(vSql);
//    		psUpdate.setTimestamp(1, mylib.getSqlTimestamp(pc.getFecFinished()));
//    		psUpdate.setString(2, pc.getStatus());
//    		psUpdate.setString(3, pc.getuStatus());
//    		psUpdate.setInt(4, pc.getErrCode());
//    		psUpdate.setString(5, pc.getErrMesg());
//    		psUpdate.setTimestamp(6, mylib.getSqlTimestamp(pc.getFecUpdate()));
//    		
//    		int numUpdated = psUpdate.executeUpdate();
//    		
//    		if (numUpdated<1) {
//    			logger.error("No pudo Actualizar ProcControl Key: "+key);
//    		} else {
//    			logger.info("Se actualizo procControl key: "+key);
//    		}
//    		
//    	} catch (Exception e) {
//    		throw new Exception(e.getMessage());
//    	}
//    }
//    
//    static private void updateDBGroupControl(MetaData dbConn, String key, GroupControl gc) throws Exception {
//    	try {
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		PreparedStatement psUpdate;
//    		
//    		String vSql = dbQuery.getSqlUpdateGroupControl(key);
//    		psUpdate = dbConn.getConnection().prepareStatement(vSql);
//    		psUpdate.setTimestamp(1, mylib.getSqlTimestamp(gc.getFecFinished()));
//    		psUpdate.setString(2, gc.getStatus());
//    		psUpdate.setString(3, gc.getuStatus());
//    		psUpdate.setInt(4, gc.getErrCode());
//    		psUpdate.setString(5, gc.getErrMesg());
//    		psUpdate.setTimestamp(6, mylib.getSqlTimestamp(gc.getFecUpdate()));
//    		
//    		int numUpdated = psUpdate.executeUpdate();
//    		
//    		if (numUpdated<1) {
//    			logger.error("No pudo Actualizar GroupControl Key: "+key);
//    		} else {
//    			logger.info("Se actualizo groupControl key: "+key);
//    		}
//    		
//    	} catch (Exception e) {
//    		throw new Exception(e.getMessage());
//    	}
//    }
//    
//    static private void loadServicesParam() throws Exception {
//    	try {
//    		MetaData dbConn = new MetaData(gDatos);
//    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
//    		
//    		dbConn.open();
//    		
//    		if (dbConn.isConnected()) {
//    			String vSql = dbQuery.getSqlFindServices();
//    			if (dbConn.executeQuery(vSql)) {
//    				ResultSet rs = dbConn.getQuery();
//    				Service service;
//    				while (rs.next()) {
//    					service = new Service();
//    					service.setSrvId(rs.getString("srvID"));
//    					service.setEnable(rs.getInt("srvEnable")==1);
//    					service.setSrvDesc(rs.getString("srvDesc"));
//    					service.setOrderBalance(rs.getInt("orderBalance"));
//    					service.setPctBalance(rs.getInt("pctBalance"));
//    					
//    					asignaTypeProc(service, rs.getString("srvTypeProc"));
//    					asignaCliProc(service, rs.getString("srvTypeProc"));
//    					
//    					//logger.info("Agregando servicio 1: "+mylib.serializeObjectToJSon(service, false));
//    					gDatos.updateMapService(service.getSrvId(), service);
//    					
//    				}
//    				rs.close();
//    			}
//    			dbConn.close();
//    			logger.info("Recuperación exitosa de Servicios desde BD");
//    		}
//    		
//    	} catch (Exception e) {
//    		logger.error("Error loadServiceParam ("+e.getMessage()+")");
//    		throw new Exception(e.getMessage());
//    	}
//    }
//    
//    static private void asignaTypeProc(Service srv, String data) throws Exception {
//    	try {
//	    	JSONArray ja = new JSONObject(data).getJSONArray("lstProc");
//	    	AssignedTypeProc atp;
//	    	
//	    	for (int i=0; i<ja.length(); i++) {
//	    		atp = new AssignedTypeProc();
//	    		
//	    		atp.setTypeProc(ja.getJSONObject(i).getString("typeProc"));
//	    		atp.setMaxThread(ja.getJSONObject(i).getInt("maxThread"));
//	    		
//	    		srv.addTypeProc(atp);
//	    	}
//    	} catch (Exception e) {
//    		logger.error("Error asignaTypeProc ("+e.getMessage()+")");
//    		throw new Exception(e.getMessage());
//    	}
//    }
//    
//    static private void asignaCliProc(Service srv, String data) throws Exception {
//    	try {
//	    	JSONArray ja = new JSONObject(data).getJSONArray("lstCli");
//	    	AssignedCliProc acp;
//	
//	    	for (int i=0; i<ja.length(); i++) {
//	    		acp = new AssignedCliProc();
//	    		
//	    		acp.setCliID(ja.getString(i));
//	    		
//	    		srv.addCliProc(acp);
//	    	}
//    	} catch (Exception e) {
//    		logger.error("Error asignaCliProc ("+e.getMessage()+")");
//    		throw new Exception(e.getMessage());
//    	}
//    }
//
//}
