package cap.DataAccess;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.api.MysqlAPI;
import com.rutinas.Rutinas;

import cap.model.Mov;
import cap.model.MovMatch;
import cap.model.PGPending;
import cap.utiles.GlobalParams;

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
	 * Private Methods
	 */
	private void setDBAccess(boolean status) {
		gParams.setDbValid(status);
	}
	
	public Object getProcessParam(String procID, String procType) throws Exception {
		try {
			Object params = null;
			dbConn.open();
			if (dbConn.isConnected()) {
				switch(procType) {
					case "ETL":
						break;
					case "MOV":
						Mov mov = new Mov();
						List<MovMatch> lstMovMatch = new ArrayList<>();
						
						String vSql = "call sp_get_mov('"+procID+"')";
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								mov = (Mov) mylib.serializeJSonStringToObject(rs.getString("resp"), Mov.class);
								
								//Busca Match de Campos
								String vSql2 = "call sp_get_movMatch('"+procID+"')";
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										MovMatch movMatch = new MovMatch();
										movMatch = (MovMatch) mylib.serializeJSonStringToObject(rs2.getString("resp"), MovMatch.class);
										lstMovMatch.add(movMatch);
									}
								}
								mov.setLstMovMatch(lstMovMatch);
								
								params = mov;
	
							}
						}
						
						logger.info("mov: "+mylib.serializeObjectToJSon(mov, true));
						
						break;
					case "OSP":
						break;
					default:
						logger.error("getProcessParam: tipo de proceso no encontrado");
				}
				
				dbConn.close();
			}
			return params;
		} catch (Exception e) {
			logger.error("getProcessParams: "+e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
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

	public String getRol(String monID) throws Exception {
		setDBAccess(true);
		try {
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_upd_monitorRol('"+ gParams.getInfo().getMonID() +"')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						return rs.getString(1);
					}
				} else {
					setDBAccess(false);
				}
			} else {
				logger.error("open: Nu puede conectarse a la BD");
				setDBAccess(false);
			}
			return null;
		} catch (Exception e) {
			logger.error("open: "+e.getMessage());
			setDBAccess(false);
			return null;
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
	}

	public Map<String, PGPending> getActiveGroup() throws Exception {
		setDBAccess(true);
		Map<String, PGPending> mp = new HashMap<>();
		try {
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_groupActiveServer("+ gParams.getInfo().getAgeGapMinute() +")";
				if (dbConn.executeQuery(vSql)) {
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
						logger.debug("getActiveGroup key: "+key);
						mp.put(key, pg);
					}
				} else {
					setDBAccess(false);
				}
				dbConn.close();
			} else {
				logger.error("open: No puede conectarse a la BD");
				setDBAccess(false);
			}
			return mp;
		} catch (Exception e) {
			logger.error("open: "+e.getMessage());
			setDBAccess(false);
			throw new Exception(e.getMessage());
		} finally {
			if (dbConn.isConnected()) {
				dbConn.close(); 
			}
		}
	}

}
