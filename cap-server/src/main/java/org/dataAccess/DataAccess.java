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
import org.model.BackTable;
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
import org.model.Server;
import org.model.User;
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
	
	public void close()  {
		try {
			dbConn.close();
		} catch (Exception e) {}
	}
	
	public boolean isConnected()  {
		try {
			return dbConn.isConnected();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean executeQuery(String vSql) throws Exception {
		try {
			dbConn.open();
			
			if (dbConn.isConnected()) {
				dbConn.executeQuery(vSql);
				
				dbConn.close();
			}

			return true;
			
			
		} catch (Exception e) {
			throw new Exception("executeQuery(): "+e.getMessage());
		}
	}
	
	public boolean getAgeGroupWeek() throws Exception {
		try {
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_ageGroupWeek()";
				dbConn.executeQuery(vSql);
				
				dbConn.close();
			}

			return true;
		} catch (Exception e) {
			throw new Exception("getAgeGroupWeek(): "+e.getMessage());
		}
	}
	
	public boolean updateDBresources(String method, Object param) throws Exception {
		try {
			List<SPparam> spParams = new ArrayList<>();
			String spName = "";
			boolean resultStat = false;
			JSONObject joParam = new JSONObject();
			
			switch(method) {
				case "updateBack":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_upd_back";
					break;
				case "updateEtb":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("etbID")));
					spParams.add(new SPparam(joParam.getString("etbDesc")));
					spParams.add(new SPparam(joParam.getString("etbTableName")));
					spParams.add(new SPparam(joParam.getString("etbFileName")));
					spParams.add(new SPparam("CSV"));
					spParams.add(new SPparam(joParam.getString("etbFileSep")));
					spParams.add(new SPparam("LOCAL"));
					spParams.add(new SPparam(joParam.getInt("etbAppend")));
					spParams.add(new SPparam(joParam.getInt("etbHeader")));
					spParams.add(new SPparam(joParam.getInt("etbWhere_active")));
					spParams.add(new SPparam(joParam.getString("etbWhere_body")));
					spParams.add(new SPparam(joParam.getInt("etbOrder_active")));
					spParams.add(new SPparam(joParam.getString("etbOrder_body")));
					spParams.add(new SPparam(-1));
					spParams.add(new SPparam(joParam.getInt("etbMultiFiles")));
					spParams.add(new SPparam(joParam.getInt("etbMaxRows_multiFiles")));
					spParams.add(new SPparam(joParam.getInt("etbGetEmptyFile")));
					spParams.add(new SPparam(joParam.getString("cliID")));
					spParams.add(new SPparam(joParam.getString("serverID")));
					spParams.add(new SPparam(joParam.getString("dbID")));
					spParams.add(new SPparam(joParam.getString("loginID")));
					spParams.add(new SPparam(joParam.getString("ownerID")));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(""));
					spParams.add(new SPparam(""));
					spParams.add(new SPparam(""));
					spName = "sp_upd_etb";
					break;
				case "updateEtbParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("etbID")));
					spParams.add(new SPparam(joParam.getString("etbFieldName")));  //CAMPO ORIGEN
					spParams.add(new SPparam(joParam.getString("etbFieldLabel"))); //NOMBRE DEL CAMPO A SER EXPORTADO
					spParams.add(new SPparam(joParam.getString("etbFieldType")));  //FIELD - VARCHAR - INTEGER
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(joParam.getInt("etbOrder")));
					spParams.add(new SPparam(joParam.getString("etbDataType")));  // VARCHAR - INTEGER
					spName = "sp_upd_etbParam";
					break;
				case "updateFtp":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("ftpID")));
					spParams.add(new SPparam(joParam.getString("ftpDesc")));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(joParam.getString("ftpAction")));
					spParams.add(new SPparam(joParam.getString("remoteFile")));
					spParams.add(new SPparam(joParam.getString("localFile")));
					spParams.add(new SPparam(joParam.getString("remotePath")));
					spParams.add(new SPparam("LOCAL"));
					spParams.add(new SPparam(joParam.getString("serverID")));
					spParams.add(new SPparam(joParam.getString("userID")));
					spName = "sp_upd_ftp";
					break;
				case "updateLtb":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("ltbID")));
					spParams.add(new SPparam(joParam.getString("ltbDesc")));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(joParam.getString("ltbFileName")));
					spParams.add(new SPparam(joParam.getString("ltbTableName")));
					spParams.add(new SPparam("CSV"));
					spParams.add(new SPparam("LOCAL"));
					spParams.add(new SPparam(joParam.getString("ltbLoadFixed")));
					spParams.add(new SPparam(joParam.getString("ltbFileSep")));
					spParams.add(new SPparam(joParam.getInt("ltbAppend")));
					spParams.add(new SPparam(joParam.getInt("ltbHeader")));
					spParams.add(new SPparam(-1));
					spParams.add(new SPparam(joParam.getString("cliID")));
					spParams.add(new SPparam(joParam.getString("serverID")));
					spParams.add(new SPparam(joParam.getString("dbID")));
					spParams.add(new SPparam(joParam.getString("loginID")));
					spParams.add(new SPparam(joParam.getString("ownerID")));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(""));
					spParams.add(new SPparam(""));
					spParams.add(new SPparam(""));
					spName = "sp_upd_ltb";
					break;
				case "updateLtbParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("ltbID")));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(joParam.getInt("filePosIni")));
					spParams.add(new SPparam(joParam.getInt("filePosFin")));
					spParams.add(new SPparam(joParam.getInt("fileLoadOrder")));
					spParams.add(new SPparam(joParam.getString("tbFieldName")));
					spParams.add(new SPparam(joParam.getString("tbFieldValue")));
					spParams.add(new SPparam(joParam.getString("tbFieldDataType"))); //VARCHAR - INTEGER - DATE
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(joParam.getInt("tbLoadFromFile")));
					spName = "sp_upd_ltbParam";
					break;
				case "updateOsp":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("ospID")));
					spParams.add(new SPparam(joParam.getString("ospDesc")));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(joParam.getString("ospName")));
					spParams.add(new SPparam(joParam.getString("cliID")));
					spParams.add(new SPparam(joParam.getString("serverID")));
					spParams.add(new SPparam(joParam.getString("dbID")));
					spParams.add(new SPparam(joParam.getString("loginID")));
					spParams.add(new SPparam(joParam.getString("ownerID")));
					spName = "sp_upd_osp";
					break;
				case "updateOspParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("ospID")));
					spParams.add(new SPparam(joParam.getInt("order")));
					spParams.add(new SPparam(joParam.getString("desc")));
					spParams.add(new SPparam(joParam.getString("type"))); //FN (formula) o IN (no es formula)
					spParams.add(new SPparam(joParam.getString("value")));
					spParams.add(new SPparam("VARCHAR"));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spName = "sp_upd_ospParam";
					break;
				case "updateMov":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("movID")));
					spParams.add(new SPparam(joParam.getString("movDesc")));
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(joParam.getString("cliID")));
					spParams.add(new SPparam(joParam.getString("sServerID")));
					spParams.add(new SPparam(joParam.getString("sDbID")));
					spParams.add(new SPparam(joParam.getString("sLoginID")));
					spParams.add(new SPparam(joParam.getString("sOwnerID")));
					spParams.add(new SPparam(joParam.getString("sTbName")));
					spParams.add(new SPparam(joParam.getString("dServerID")));
					spParams.add(new SPparam(joParam.getString("dDbID")));
					spParams.add(new SPparam(joParam.getString("dLoginID")));
					spParams.add(new SPparam(joParam.getString("dOwnerID")));
					spParams.add(new SPparam(joParam.getString("dTbName")));
					spParams.add(new SPparam(0));	//vSFieldUpdateActive
					spParams.add(new SPparam(""));	//vSFieldUpdateName
					spParams.add(new SPparam(""));	//vSFieldValueType
					spParams.add(new SPparam(""));	//vSFieldValueRead
					spParams.add(new SPparam(""));	//vSFieldValueUpdate
					spParams.add(new SPparam(joParam.getInt("whereActive")));
					spParams.add(new SPparam(joParam.getString("whereBody")));
					spParams.add(new SPparam(joParam.getInt("appendable")));
					spParams.add(new SPparam(0));  	//createDest
					spParams.add(new SPparam(-1)); 	//maxRows
					spParams.add(new SPparam(5000)); //maxRowsRange
					spParams.add(new SPparam(-1)); 	//maxRowsError
					spParams.add(new SPparam(0)); 	//maxPctError
					spParams.add(new SPparam(0));	//rollBackError
					spParams.add(new SPparam(0));	//FecExtActive
					spParams.add(new SPparam(0));	//fecExtEpoch
					spParams.add(new SPparam(""));	//fecExtField
					spParams.add(new SPparam("")); 	//fecExtIni
					spParams.add(new SPparam(""));	//fecExtFin
					spParams.add(new SPparam(""));	//fecExtFinIn
					spParams.add(new SPparam(""));	//fecExtIniIn
					spParams.add(new SPparam(""));	//sFieldUpdateKey
					
					spName = "sp_upd_mov";
					break;
				case "updateMovParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("movID")));
					spParams.add(new SPparam(joParam.getInt("movOrder")));
					spParams.add(new SPparam(joParam.getInt("enable")));
					spParams.add(new SPparam(joParam.getString("sourceField")));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam("VARCHAR"));	//VARCHAR - INTEGER - DATE
					spParams.add(new SPparam(joParam.getString("destField")));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(joParam.getString("fieldType"))); // FIELD - VARCHAR - INTEGER - NULL
					spName = "sp_upd_movParam";
					break;
				case "updateProcGroup":
					joParam = (JSONObject) param;
					ProcGroup procGroup = (ProcGroup) mylib.serializeJSonStringToObject(joParam.toString(), ProcGroup.class);
					
					spParams.add(new SPparam(procGroup.getGrpID()));
					spParams.add(new SPparam(procGroup.getProcID()));
					spParams.add(new SPparam(procGroup.getnOrder()));
					spParams.add(new SPparam(procGroup.getEnable()));
					spParams.add(new SPparam(procGroup.getType()));
					spName = "sp_upd_procGroup";
					break;
				case "updateDependence":
					JSONObject jodep = (JSONObject) param;
					String strDep = jodep.toString();
					Dependence dep = (Dependence) mylib.serializeJSonStringToObject(strDep, Dependence.class);
					
					spParams.add(new SPparam(dep.getGrpID()));
					spParams.add(new SPparam(dep.getCritical()));
					spParams.add(new SPparam(dep.getProcHijo()));
					spParams.add(new SPparam(dep.getProcPadre()));
					
					spName = "sp_upd_depProc";
					
					break;
				default:
					logger.error("method: "+method+" no es reconocido");
					throw new Exception("updateDBresources(): method: "+method+" no es reconocido");
			}
			dbConn.open();
			
			if (dbConn.isConnected()) {
				if (dbConn.executeProcedure(spName, spParams)) {
					ResultSet rs = dbConn.getSpResult();
					int result = 1;
					if (rs.next()) {
						try {
							result = rs.getInt(1);
						} catch (Exception e) {
							throw new Exception("updateDBresources(): No es posible leer resultado de SP. " + e.getMessage());
						}
					}
					resultStat = (result==0);
				}
				
				dbConn.close();
			}
			
			return resultStat;
		} catch (Exception e) {
			throw new Exception("updateDBresources(): "+e.getMessage());
		}
	}
	
	public boolean delDBresources(String method, Object param) throws Exception {
		try {
			List<SPparam> spParams = new ArrayList<>();
			String strParam = "";
			String spName = "";
			boolean resultStat = false;
			JSONObject joParam = new JSONObject();
			
			logger.info("Petición de Eliminación de Data...");
			logger.info("Metodo recibido: "+method);
			
			switch(method) {
				case "deleteGroup":
					JSONObject joGroup = (JSONObject) param;
					
					spParams.add(new SPparam(joGroup.getString("grpID")));
					spName = "sp_del_group";
					break;
				case "deleteProcGroup":
					JSONObject joProcGroup = (JSONObject) param;
					
					spParams.add(new SPparam(joProcGroup.getString("grpID")));
					spParams.add(new SPparam(joProcGroup.getString("procID")));
					spName = "sp_del_procGroup";
					break;
				case "deleteOsp":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_osp";
					break;
				case "deleteOspParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spParams.add(new SPparam(joParam.getInt("order")));
					spName = "sp_del_ospParam";
					break;
				case "deleteMov":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_mov";
					break;
				case "deleteMovParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spParams.add(new SPparam(joParam.getInt("order")));
					spName = "sp_del_movParam";
					break;
				case "deleteEtb":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_etb";
					break;
				case "deleteEtbParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spParams.add(new SPparam(joParam.getInt("order")));
					spName = "sp_del_etbParam";
					break;
				case "deleteLtb":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_ltb";
					break;
				case "deleteLtbParam":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spParams.add(new SPparam(joParam.getInt("order")));
					spName = "sp_del_ltbParam";
					break;
				case "deleteFtp":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_ftp";
					break;
				case "deleteBack":
					joParam = (JSONObject) param;
					
					spParams.add(new SPparam(joParam.getString("procID")));
					spName = "sp_del_back";
					break;
				case "deleteClient":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					Client client = (Client) mylib.serializeJSonStringToObject(strParam, Client.class);

					spParams.add(new SPparam(client.getCliID()));
					spName = "sp_del_client";
					break;
				case "deleteCategory":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					Category cat = (Category) mylib.serializeJSonStringToObject(strParam, Category.class);

					spParams.add(new SPparam(cat.getCatID()));
					spName = "sp_del_category";
					break;
				case "deleteUser":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					User user = (User) mylib.serializeJSonStringToObject(strParam, User.class);

					spParams.add(new SPparam(user.getUserID()));
					spName = "sp_del_user";
					break;
				case "deleteServer":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					Server srv = (Server) mylib.serializeJSonStringToObject(strParam, Server.class);

					spParams.add(new SPparam(srv.getServerID()));
					spName = "sp_del_server";
					break;
				case "deleteDBase":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					DBase db = (DBase) mylib.serializeJSonStringToObject(strParam, DBase.class);

					spParams.add(new SPparam(db.getDbID()));
					spName = "sp_del_dbase";
					break;
				case "deleteDependence":
					JSONObject jodep = (JSONObject) param;
					String strDep = jodep.toString();
					Dependence dep = (Dependence) mylib.serializeJSonStringToObject(strDep, Dependence.class);
					
					spParams.add(new SPparam(dep.getGrpID()));
					spParams.add(new SPparam(dep.getProcHijo()));
					spName = "sp_del_depProc";
					break;
				default:
					logger.error("method: "+method+" no es reconocido");
					throw new Exception("delDBresources(): method: "+method+" no es reconocido");
			}
			
			logger.info("Abriendo BD...");
			dbConn.open();
			
			if (dbConn.isConnected()) {
				logger.info("BD Connectada. Ejecutando SP: "+spName);
				if (dbConn.executeProcedure(spName, spParams)) {
					ResultSet rs = dbConn.getSpResult();
					int result = 1;
					if (rs.next()) {
						try {
							result = rs.getInt(1);
						} catch (Exception e) {
							throw new Exception("delDBresources(): No es posible leer resultado de SP. " + e.getMessage());
						}
					}
					resultStat = (result==0);
				}
				
				logger.info("Respuesta de ejecución del SP: "+resultStat);
				dbConn.close();
			}
			
			return resultStat;
		} catch (Exception e) {
			throw new Exception("delDBresources(): "+e.getMessage());
		}
	}
	
	public boolean addDBresources(String method, Object param) throws Exception {
		try {
			List<SPparam> spParams = new ArrayList<>();
			JSONObject joParam = new JSONObject();
			String strParam = "";
			String spName = "";
			boolean resultStat = false;
			
			switch(method) {
				case "addUser":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					User user = (User) mylib.serializeJSonStringToObject(strParam, User.class);
					
					if (mylib.isNullOrEmpty(user.getUserID())) {
						String userID = getNewUserID();
						spParams.add(new SPparam(userID));
						spParams.add(new SPparam(user.getUserName()));
						spParams.add(new SPparam(userID+"-"+user.getUserName())); //Mas adelante devolver a userDesc
						spParams.add(new SPparam(user.getUserType()));  // DB - SERVER
						spParams.add(new SPparam(user.getUserPass()));
						spParams.add(new SPparam(user.getUserDomain()));
						spParams.add(new SPparam("CLI00000"));
						spParams.add(new SPparam(1));
						spName = "sp_add_user";
					} else {
						spParams.add(new SPparam(user.getUserID()));
						spParams.add(new SPparam(user.getUserName()));
						spParams.add(new SPparam(user.getUserID()+"-"+user.getUserName())); //Mas adelante devolver a userDesc
						spParams.add(new SPparam(user.getUserType()));  // DB - SERVER
						spParams.add(new SPparam(user.getUserPass()));
						spParams.add(new SPparam(user.getUserDomain()));
						spParams.add(new SPparam("CLI00000"));
						spParams.add(new SPparam(user.getUserEnable()));
						spName = "sp_upd_user";
					}
					
					break;
				case "addBack":
					JSONObject joBack = (JSONObject) param;
					String strBack = joBack.toString();
					BackTable btb = (BackTable) mylib.serializeJSonStringToObject(strBack, BackTable.class);
					
					if (mylib.isNullOrEmpty(btb.getBtbID())) {
						spParams.add(new SPparam(getNewBackID()));
						spParams.add(new SPparam(btb.getBtbDesc()));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(btb.getBtbTableName()));
						spParams.add(new SPparam(btb.getBtbFileName()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(btb.getBtbWhere_active()));
						spParams.add(new SPparam(btb.getBtbWhere_body()));
						spParams.add(new SPparam(btb.getCliID()));
						spParams.add(new SPparam(btb.getServerID()));
						spParams.add(new SPparam(btb.getDbID()));
						spParams.add(new SPparam(btb.getLoginID()));
						spParams.add(new SPparam(btb.getOwnerID()));
						spName = "sp_add_back";
					} else {
						spParams.add(new SPparam(btb.getBtbID()));
						spParams.add(new SPparam(btb.getBtbDesc()));
						spParams.add(new SPparam(btb.getEnable()));
						spParams.add(new SPparam(btb.getBtbTableName()));
						spParams.add(new SPparam(btb.getBtbFileName()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(btb.getBtbWhere_active()));
						spParams.add(new SPparam(btb.getBtbWhere_body()));
						spParams.add(new SPparam(btb.getCliID()));
						spParams.add(new SPparam(btb.getServerID()));
						spParams.add(new SPparam(btb.getDbID()));
						spParams.add(new SPparam(btb.getLoginID()));
						spParams.add(new SPparam(btb.getOwnerID()));
						spName = "sp_upd_back";
					}
					
					break;
				case "addFtp":
					JSONObject joFtp = (JSONObject) param;
					String strFtp = joFtp.toString();
					Ftp ftp = (Ftp) mylib.serializeJSonStringToObject(strFtp, Ftp.class);
					
					if (mylib.isNullOrEmpty(ftp.getFtpID())) {
						spParams.add(new SPparam(getNewFtpID()));
						spParams.add(new SPparam(ftp.getFtpDesc()));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(ftp.getFtpAction()));
						spParams.add(new SPparam(ftp.getRemoteFile()));
						spParams.add(new SPparam(ftp.getLocalFile()));
						spParams.add(new SPparam(ftp.getRemotePath()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(ftp.getServerID()));
						spParams.add(new SPparam(ftp.getUserID()));
						spName = "sp_add_ftp";
					} else {
						spParams.add(new SPparam(ftp.getFtpID()));
						spParams.add(new SPparam(ftp.getFtpDesc()));
						spParams.add(new SPparam(ftp.getEnable()));
						spParams.add(new SPparam(0));	//ftpSecure
						spParams.add(new SPparam(ftp.getFtpAction()));
						spParams.add(new SPparam(ftp.getRemoteFile()));
						spParams.add(new SPparam(ftp.getLocalFile()));
						spParams.add(new SPparam(ftp.getRemotePath()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(ftp.getServerID()));
						spParams.add(new SPparam(ftp.getUserID()));
						spName = "sp_upd_ftp";
					}
					
					break;
				case "addOsp":
					JSONObject joOsp = (JSONObject) param;
					String strOsp = joOsp.toString();
					Osp osp = (Osp) mylib.serializeJSonStringToObject(strOsp, Osp.class);
					
					if (mylib.isNullOrEmpty(osp.getOspID())) {
						spParams.add(new SPparam(getNewOspID()));
						spParams.add(new SPparam(osp.getOspDesc()));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(osp.getOspName()));
						spParams.add(new SPparam(osp.getCliID()));
						spParams.add(new SPparam(osp.getServerID()));
						spParams.add(new SPparam(osp.getDbID()));
						spParams.add(new SPparam(osp.getLoginID()));
						spParams.add(new SPparam(osp.getOwnerID()));
						spName = "sp_add_osp";
					} else {
						spParams.add(new SPparam(osp.getOspID()));
						spParams.add(new SPparam(osp.getOspDesc()));
						spParams.add(new SPparam(osp.getEnable()));
						spParams.add(new SPparam(osp.getOspName()));
						spParams.add(new SPparam(osp.getCliID()));
						spParams.add(new SPparam(osp.getServerID()));
						spParams.add(new SPparam(osp.getDbID()));
						spParams.add(new SPparam(osp.getLoginID()));
						spParams.add(new SPparam(osp.getOwnerID()));
						spName = "sp_upd_osp";
					}
					
					break;
				case "addOspParam":
					JSONObject joOspParam = (JSONObject) param;
					String strOspParam = joOspParam.toString();
					OspParam op = (OspParam) mylib.serializeJSonStringToObject(strOspParam, OspParam.class);
					
					spParams.add(new SPparam(op.getOspID()));
					spParams.add(new SPparam(op.getOrder()));
					spParams.add(new SPparam(op.getDesc()));
					spParams.add(new SPparam(op.getType())); //FN (formula) o IN (no es formula)
					spParams.add(new SPparam(op.getValue()));
					spParams.add(new SPparam("VARCHAR")); //Por el momento siempre VARCHAR
					spParams.add(new SPparam(1));
					
					spName = "sp_add_ospParam";
					
					break;
				case "addLtb":
					JSONObject joLtb = (JSONObject) param;
					String strLtb = joLtb.toString();
					LoadTable ltb = (LoadTable) mylib.serializeJSonStringToObject(strLtb, LoadTable.class);
					
					if (mylib.isNullOrEmpty(ltb.getLtbID())) {
						spParams.add(new SPparam(getNewLtbID()));
						spParams.add(new SPparam(ltb.getLtbDesc()));
						spParams.add(new SPparam(1)); //enable
						spParams.add(new SPparam(ltb.getLtbFileName()));
						spParams.add(new SPparam("CSV"));  //fileType
						spParams.add(new SPparam("LOCAL"));	//filePath
						spParams.add(new SPparam(ltb.getLtbTableName()));
						spParams.add(new SPparam(ltb.getLtbLoadFixed()));
						spParams.add(new SPparam(ltb.getLtbFileSep()));
						spParams.add(new SPparam(ltb.getLtbAppend()));
						spParams.add(new SPparam(ltb.getLtbHeader()));
						spParams.add(new SPparam(-1)); //maxRows
						spParams.add(new SPparam(ltb.getCliID()));
						spParams.add(new SPparam(ltb.getServerID()));
						spParams.add(new SPparam(ltb.getDbID()));
						spParams.add(new SPparam(ltb.getLoginID()));
						spParams.add(new SPparam(ltb.getOwnerID()));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spName = "sp_add_ltb";
					} else {
						spParams.add(new SPparam(ltb.getLtbID()));
						spParams.add(new SPparam(ltb.getLtbDesc()));
						spParams.add(new SPparam(ltb.getEnable()));
						spParams.add(new SPparam(ltb.getLtbFileName()));
						spParams.add(new SPparam("CSV"));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(ltb.getLtbTableName()));
						spParams.add(new SPparam(ltb.getLtbLoadFixed()));
						spParams.add(new SPparam(ltb.getLtbFileSep()));
						spParams.add(new SPparam(ltb.getLtbAppend()));
						spParams.add(new SPparam(ltb.getLtbHeader()));
						spParams.add(new SPparam(-1));
						spParams.add(new SPparam(ltb.getCliID()));
						spParams.add(new SPparam(ltb.getServerID()));
						spParams.add(new SPparam(ltb.getDbID()));
						spParams.add(new SPparam(ltb.getLoginID()));
						spParams.add(new SPparam(ltb.getOwnerID()));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spName = "sp_upd_ltb";
					}
					
					break;
				case "addLtbParam":
					JSONObject joLtbParam = (JSONObject) param;
					String strLtbParam = joLtbParam.toString();
					LoadTableParam lp = (LoadTableParam) mylib.serializeJSonStringToObject(strLtbParam, LoadTableParam.class);
					
					spParams.add(new SPparam(lp.getLtbID()));
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(lp.getFilePosIni()));
					spParams.add(new SPparam(lp.getFilePosFin()));
					spParams.add(new SPparam(lp.getFileLoadOrder()));
					spParams.add(new SPparam(lp.getTbFieldName()));
					spParams.add(new SPparam(lp.getTbFieldValue()));
					spParams.add(new SPparam(lp.getTbFieldDataType()));  //VARCHAR - INTEGER - DATE
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(lp.getTbLoadFromFile()));
					
					spName = "sp_add_ltbParam";
					
					break;
				case "addEtb":
					JSONObject joEtb = (JSONObject) param;
					String strEtb = joEtb.toString();
					ExpTable etb = (ExpTable) mylib.serializeJSonStringToObject(strEtb, ExpTable.class);
					
					if (mylib.isNullOrEmpty(etb.getEtbID())) {
						spParams.add(new SPparam(getNewEtbID()));
						spParams.add(new SPparam(etb.getEtbDesc()));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(etb.getEtbTableName()));
						spParams.add(new SPparam(etb.getEtbFileName()));
						spParams.add(new SPparam("CSV"));
						spParams.add(new SPparam(etb.getEtbFileSep()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(etb.getEtbAppend()));
						spParams.add(new SPparam(etb.getEtbHeader()));
						spParams.add(new SPparam(etb.getEtbWhere_active()));
						spParams.add(new SPparam(etb.getEtbWhere_body()));
						spParams.add(new SPparam(etb.getEtbOrder_active()));
						spParams.add(new SPparam(etb.getEtbOrder_body()));
						spParams.add(new SPparam(-1));
						spParams.add(new SPparam(etb.getEtbMultiFiles()));
						spParams.add(new SPparam(etb.getEtbMaxRows_multiFiles()));
						spParams.add(new SPparam(etb.getEtbGetEmptyFile()));
						spParams.add(new SPparam(etb.getCliID()));
						spParams.add(new SPparam(etb.getServerID()));
						spParams.add(new SPparam(etb.getDbID()));
						spParams.add(new SPparam(etb.getLoginID()));
						spParams.add(new SPparam(etb.getOwnerID()));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spName = "sp_add_etb";
					} else {
						spParams.add(new SPparam(etb.getEtbID()));
						spParams.add(new SPparam(etb.getEtbDesc()));
						spParams.add(new SPparam(etb.getEtbEnable()));
						spParams.add(new SPparam(etb.getEtbTableName()));
						spParams.add(new SPparam(etb.getEtbFileName()));
						spParams.add(new SPparam("CSV"));
						spParams.add(new SPparam(etb.getEtbFileSep()));
						spParams.add(new SPparam("LOCAL"));
						spParams.add(new SPparam(etb.getEtbAppend()));
						spParams.add(new SPparam(etb.getEtbHeader()));
						spParams.add(new SPparam(etb.getEtbWhere_active()));
						spParams.add(new SPparam(etb.getEtbWhere_body()));
						spParams.add(new SPparam(etb.getEtbOrder_active()));
						spParams.add(new SPparam(etb.getEtbOrder_body()));
						spParams.add(new SPparam(-1));
						spParams.add(new SPparam(etb.getEtbMultiFiles()));
						spParams.add(new SPparam(etb.getEtbMaxRows_multiFiles()));
						spParams.add(new SPparam(etb.getEtbGetEmptyFile()));
						spParams.add(new SPparam(etb.getCliID()));
						spParams.add(new SPparam(etb.getServerID()));
						spParams.add(new SPparam(etb.getDbID()));
						spParams.add(new SPparam(etb.getLoginID()));
						spParams.add(new SPparam(etb.getOwnerID()));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(0));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spParams.add(new SPparam(""));
						spName = "sp_upd_etb";
					}
					
					break;
				case "addEtbParam":
					JSONObject joEtbParam = (JSONObject) param;
					String strEtbParam = joEtbParam.toString();
					ExpTableParam etbp = (ExpTableParam) mylib.serializeJSonStringToObject(strEtbParam, ExpTableParam.class);
					
					spParams.add(new SPparam(etbp.getEtbID()));
					spParams.add(new SPparam(etbp.getEtbFieldName()));  //CAMPO ORIGEN
					spParams.add(new SPparam(etbp.getEtbFieldLabel())); //NOMBRE DEL CAMPO A SER EXPORTADO
					spParams.add(new SPparam(etbp.getEtbFieldType()));  //FIELD - VARCHAR - INTEGER
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(etbp.getEtbOrder()));
					spParams.add(new SPparam(etbp.getEtbDataType()));  // VARCHAR - INTEGER
					
					spName = "sp_add_etbParam";
					
					break;
				case "addMovParam":
					joParam = (JSONObject) param;
					strParam = joParam.toString();
					MovMatch mm = (MovMatch) mylib.serializeJSonStringToObject(strParam, MovMatch.class);
					
					spParams.add(new SPparam(mm.getMovID()));
					spParams.add(new SPparam(mm.getMovOrder()));
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(mm.getSourceField()));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam("VARCHAR"));	 //sourceType : VARCHAR - INTEGER - DATE
					spParams.add(new SPparam(mm.getDestField()));
					spParams.add(new SPparam(0));
					spParams.add(new SPparam(mm.getFieldType())); //FIELD - VARCHAR- INTEGER - NULL
					spName = "sp_add_movParam";
					
					break;
				case "addMov":
					JSONObject joMov = (JSONObject) param;
					String strMov = joMov.toString();
					Mov mov = (Mov) mylib.serializeJSonStringToObject(strMov, Mov.class);

					if (mylib.isNullOrEmpty(mov.getMovID())) {
						spParams.add(new SPparam(getNewMovID()));
						spParams.add(new SPparam(mov.getMovDesc()));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(mov.getCliID()));
						spParams.add(new SPparam(mov.getsServerID()));
						spParams.add(new SPparam(mov.getsDbID()));
						spParams.add(new SPparam(mov.getsLoginID()));
						spParams.add(new SPparam(mov.getsOwnerID()));
						spParams.add(new SPparam(mov.getsTbName()));
						spParams.add(new SPparam(mov.getdServerID()));
						spParams.add(new SPparam(mov.getdDbID()));
						spParams.add(new SPparam(mov.getdLoginID()));
						spParams.add(new SPparam(mov.getdOwnerID()));
						spParams.add(new SPparam(mov.getdTbName()));
						spParams.add(new SPparam(0));	//sFieldUpdateActive
						spParams.add(new SPparam(""));	//vSFieldUpdateName
						spParams.add(new SPparam(""));	//vSFieldValueType
						spParams.add(new SPparam(""));	//vSFieldValueRead
						spParams.add(new SPparam(""));	//vSFieldValueUpdate
						spParams.add(new SPparam(mov.getWhereActive()));
						spParams.add(new SPparam(mov.getWhereBody()));
						spParams.add(new SPparam(mov.getAppendable()));
						spParams.add(new SPparam(0));  	//createDest
						spParams.add(new SPparam(0));  	//deleteWhereActive
						spParams.add(new SPparam(""));  	//deleteWhereBody
						spParams.add(new SPparam(-1)); 	//maxRows
						spParams.add(new SPparam(5000)); //maxRowsRange
						spParams.add(new SPparam(-1)); 	//maxRowsError
						spParams.add(new SPparam(-1)); 	//maxPctError
						spParams.add(new SPparam(0));	//rollBackError
						spParams.add(new SPparam(0));	//FecExtActive
						spParams.add(new SPparam(0));	//fecExtEpoch
						spParams.add(new SPparam(""));	//fecExtField
						spParams.add(new SPparam("")); 	//fecExtIni
						spParams.add(new SPparam(""));	//fecExtFin
						spParams.add(new SPparam(0));	//fecExtFinIn
						spParams.add(new SPparam(0));	//fecExtIniIn
						spParams.add(new SPparam(""));	//sFieldUpdateKey
						spName = "sp_add_mov";
					} else {
						spParams.add(new SPparam(mov.getMovID()));
						spParams.add(new SPparam(mov.getMovDesc()));
						spParams.add(new SPparam(mov.getEnable()));
						spParams.add(new SPparam(mov.getCliID()));
						spParams.add(new SPparam(mov.getsServerID()));
						spParams.add(new SPparam(mov.getsDbID()));
						spParams.add(new SPparam(mov.getsLoginID()));
						spParams.add(new SPparam(mov.getsOwnerID()));
						spParams.add(new SPparam(mov.getsTbName()));
						spParams.add(new SPparam(mov.getdServerID()));
						spParams.add(new SPparam(mov.getdDbID()));
						spParams.add(new SPparam(mov.getdLoginID()));
						spParams.add(new SPparam(mov.getdOwnerID()));
						spParams.add(new SPparam(mov.getdTbName()));
						spParams.add(new SPparam(0));	//sFieldUpdateActive
						spParams.add(new SPparam(""));	//vSFieldUpdateName
						spParams.add(new SPparam(""));	//vSFieldValueType
						spParams.add(new SPparam(""));	//vSFieldValueRead
						spParams.add(new SPparam(""));	//vSFieldValueUpdate
						spParams.add(new SPparam(mov.getWhereActive()));
						spParams.add(new SPparam(mov.getWhereBody()));
						spParams.add(new SPparam(mov.getAppendable()));
						spParams.add(new SPparam(0));  	//createDest
						spParams.add(new SPparam(0));  	//deleteWhereActive
						spParams.add(new SPparam(""));  	//deleteWhereBody
						spParams.add(new SPparam(-1)); 	//maxRows
						spParams.add(new SPparam(5000)); //maxRowsRange
						spParams.add(new SPparam(-1)); 	//maxRowsError
						spParams.add(new SPparam(-1)); 	//maxPctError
						spParams.add(new SPparam(0));	//rollBackError
						spParams.add(new SPparam(0));	//FecExtActive
						spParams.add(new SPparam(0));	//fecExtEpoch
						spParams.add(new SPparam(""));	//fecExtField
						spParams.add(new SPparam("")); 	//fecExtIni
						spParams.add(new SPparam(""));	//fecExtFin
						spParams.add(new SPparam(0));	//fecExtFinIn
						spParams.add(new SPparam(0));	//fecExtIniIn
						spParams.add(new SPparam(""));	//sFieldUpdateKey
						spName = "sp_upd_mov";
					}
					
					break;
				case "addServer":
					JSONObject joServer = (JSONObject) param;
					
					String serverID = "";
					try {
						serverID = joServer.getString("serverID");
					} catch (Exception e) {}			
					
					if (mylib.isNullOrEmpty(serverID)) {
						spParams.add(new SPparam(getNewServerID()));
						spParams.add(new SPparam(joServer.getString("serverDesc")));
						spParams.add(new SPparam(1));
						spParams.add(new SPparam(joServer.getString("serverHostName")));
						spParams.add(new SPparam(joServer.getString("serverDomain")));
						spParams.add(new SPparam("CLI00000"));
						spParams.add(new SPparam(joServer.getString("serverRoleMain")));
						spParams.add(new SPparam(joServer.getString("serverIP")));
						spName = "sp_add_server";
					} else {
						spParams.add(new SPparam(serverID));
						spParams.add(new SPparam(joServer.getString("serverDesc")));
						spParams.add(new SPparam(joServer.getString("serverEnable")));
						spParams.add(new SPparam(joServer.getString("serverHostName")));
						spParams.add(new SPparam(joServer.getString("serverDomain")));
						spParams.add(new SPparam("CLI00000"));
						spParams.add(new SPparam(joServer.getString("serverRoleMain")));
						spParams.add(new SPparam(joServer.getString("serverIP")));
						spName = "sp_upd_server";
					}
					
					break;
				case "addGroup":
					JSONObject joGroup = (JSONObject) param;
					String strGroup = joGroup.toString();
					Group group = (Group) mylib.serializeJSonStringToObject(strGroup, Group.class);
					
					if (mylib.isNullOrEmpty(group.getGrpID())) {
						spParams.add(new SPparam(getNewGrpID()));
						spParams.add(new SPparam(group.getGrpDesc()));
						spParams.add(new SPparam(group.getEnable()));
						spParams.add(new SPparam(group.getHorID()));
						spParams.add(new SPparam(group.getCliID()));
						spParams.add(new SPparam(group.getMaxTimeExec()));
						spParams.add(new SPparam("MULTI"));
						spParams.add(new SPparam("SERVER"));
						spParams.add(new SPparam(group.getCatID()));
						spName = "sp_add_group";
					} else {
						spParams.add(new SPparam(group.getGrpID()));
						spParams.add(new SPparam(group.getGrpDesc()));
						spParams.add(new SPparam(group.getEnable()));
						spParams.add(new SPparam(group.getHorID()));
						spParams.add(new SPparam(group.getCliID()));
						spParams.add(new SPparam(group.getMaxTimeExec()));
						spParams.add(new SPparam("MULTI"));
						spParams.add(new SPparam("SERVER"));
						spParams.add(new SPparam(group.getCatID()));
						spName = "sp_upd_group";
					}
					
					break;
				case "addDiary":
					JSONObject joDiary = (JSONObject) param;
					
					spParams.add(new SPparam(getNewAgeID()));
					spParams.add(new SPparam(joDiary.getString("ageDesc")));
					spParams.add(new SPparam(joDiary.getString("month")));
					spParams.add(new SPparam(joDiary.getString("dayOfMonth")));
					spParams.add(new SPparam(joDiary.getString("dayOfWeek")));
					spParams.add(new SPparam(joDiary.getString("hourOfDay")));
					spParams.add(new SPparam(joDiary.getString("minute")));
					
					spName = "sp_add_diary";
					
					break;
				case "addSchedule":
					JSONObject joSchedule = (JSONObject) param;
					
					spParams.add(new SPparam(getNewHorID()));
					spParams.add(new SPparam(joSchedule.getString("horDesc")));
					spParams.add(new SPparam(1));
					
					spName = "sp_add_schedule";
					
					break;
				case "addSchedDiary":
					JSONObject joSchedDiary = (JSONObject) param;
					
					spParams.add(new SPparam(joSchedDiary.getString("horID")));
					spParams.add(new SPparam(joSchedDiary.getString("ageID")));
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(1));
					
					spName = "sp_add_schedDiary";
					
					break;
				case "addProcGroup":
					JSONObject joProcGroup = (JSONObject) param;
					String strProcGroup = joProcGroup.toString();
					ProcGroup procGroup = (ProcGroup) mylib.serializeJSonStringToObject(strProcGroup, ProcGroup.class);
					
					spParams.add(new SPparam(procGroup.getGrpID()));
					spParams.add(new SPparam(procGroup.getProcID()));
					spParams.add(new SPparam(procGroup.getnOrder()));
					spParams.add(new SPparam(1));
					spParams.add(new SPparam(procGroup.getType()));
					
					spName = "sp_add_procGroup";
					
					break;
				case "addClient":
					JSONObject joClient = (JSONObject) param;
					String strClient = joClient.toString();
					Client client = (Client) mylib.serializeJSonStringToObject(strClient, Client.class);
					
					if (mylib.isNullOrEmpty(client.getCliID())) {
						spParams.add(new SPparam(getNewClientID()));
						spParams.add(new SPparam(client.getCliDesc()));
						spParams.add(new SPparam(1));
						spName = "sp_add_client";
					} else {
						spParams.add(new SPparam(client.getCliID()));
						spParams.add(new SPparam(client.getCliDesc()));
						spParams.add(new SPparam(client.getEnable()));
						spName = "sp_upd_client";
					}
					
					break;
				case "addCategory":
					JSONObject joCat = (JSONObject) param;
					String strCat = joCat.toString();
					Category cat = (Category) mylib.serializeJSonStringToObject(strCat, Category.class);
					
					if (mylib.isNullOrEmpty(cat.getCatID())) {
						spParams.add(new SPparam(getNewCategoryID()));
						spParams.add(new SPparam(cat.getCatDesc()));
						spParams.add(new SPparam(1));
						spName = "sp_add_category";
					} else {
						spParams.add(new SPparam(cat.getCatID()));
						spParams.add(new SPparam(cat.getCatDesc()));
						spParams.add(new SPparam(cat.getEnable()));
						spName = "sp_upd_category";
					}
					
					
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
				default:
					logger.error("method: "+method+" no es reconocido");
					throw new Exception("addDBresources(): method: "+method+" no es reconocido");
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
	
	
	public Map<String,Object> getCountGroups() throws Exception {
		try {
			Map<String,Object> cols = new HashMap<>();
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_countGroup()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						cols.put("numGroups", rs.getInt("numGroups"));
						cols.put("numActiveGroups", rs.getInt("numActiveGroups"));
						cols.put("numInactiveGroups", rs.getInt("numInactiveGroups"));
					}
				}
				
				dbConn.close();
			}

			return cols;
		} catch (Exception e) {
			throw new Exception("getCountGroups(): "+e.getMessage());
		}
	}

	public String getNewClientID() throws Exception {
		try {
			String cliID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newCliID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						cliID = rs.getString("cliID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(cliID)) {
				throw new Exception("getNewClientID(): no es posible recuperar un nuevo ID de Cliente");
			}
			
			return cliID;
		} catch (Exception e) {
			throw new Exception("getNewClientID(): "+e.getMessage());
		}
	}
	
	public String getNewEtbID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('ETB')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewEtbID(): no es posible recuperar un nuevo ID de Etb");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewEtbID(): "+e.getMessage());
		}
	}
	
	public String getNewLtbID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('LTB')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewLtbID(): no es posible recuperar un nuevo ID de Ltb");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewLtbID(): "+e.getMessage());
		}
	}

	public String getNewOspID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('OSP')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewOspID(): no es posible recuperar un nuevo ID de OSP");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewOspID(): "+e.getMessage());
		}
	}

	public String getNewFtpID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('FTP')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewFtpID(): no es posible recuperar un nuevo ID de FTP");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewFtpID(): "+e.getMessage());
		}
	}

	public String getNewBackID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('BTB')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewBackID(): no es posible recuperar un nuevo ID de BTB");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewBackID(): "+e.getMessage());
		}
	}

	public String getNewMovID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newProcID('MOV')";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("itProcID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewMovID(): no es posible recuperar un nuevo ID de Mov");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewMovID(): "+e.getMessage());
		}
	}
	
	public String getNewServerID() throws Exception {
		try {
			String objID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newServerID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						objID = rs.getString("objID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(objID)) {
				throw new Exception("getNewServerID(): no es posible recuperar un nuevo ID de Servidor");
			}
			
			return objID;
		} catch (Exception e) {
			throw new Exception("getNewServerID(): "+e.getMessage());
		}
	}

	public String getNewUserID() throws Exception {
		try {
			String userID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newUserID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						userID = rs.getString("userID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(userID)) {
				throw new Exception("getNewUserID(): no es posible recuperar un nuevo ID de Usuario");
			}
			
			return userID;
		} catch (Exception e) {
			throw new Exception("getNewUserID(): "+e.getMessage());
		}
	}

	public String getNewGrpID() throws Exception {
		try {
			String grpID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newGrpID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						grpID = rs.getString("grpID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(grpID)) {
				throw new Exception("getNewGrpID(): no es posible recuperar un nuevo ID de Grupo");
			}
			
			return grpID;
		} catch (Exception e) {
			throw new Exception("getNewGrpID(): "+e.getMessage());
		}
	}

	public String getNewHorID() throws Exception {
		try {
			String horID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newHorID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						horID = rs.getString("horID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(horID)) {
				throw new Exception("getNewHorID(): no es posible recuperar un nuevo ID de Horarios");
			}
			
			return horID;
		} catch (Exception e) {
			throw new Exception("getNewHorID(): "+e.getMessage());
		}
	}

	public String getNewAgeID() throws Exception {
		try {
			String ageID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newAgeID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						ageID = rs.getString("ageID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(ageID)) {
				throw new Exception("getNewAgeID(): no es posible recuperar un nuevo ID de Agenda");
			}
			
			return ageID;
		} catch (Exception e) {
			throw new Exception("getNewAgeID(): "+e.getMessage());
		}
	}

	public String getNewCategoryID() throws Exception {
		try {
			String catID = "";
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_get_newCatID()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						catID = rs.getString("catID");
					}
				}
				
				dbConn.close();
			}

			if (mylib.isNullOrEmpty(catID)) {
				throw new Exception("getNewCategoryID(): no es posible recuperar un nuevo ID de Categoria");
			}
			
			return catID;
		} catch (Exception e) {
			throw new Exception("getNewCategoryID(): "+e.getMessage());
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
				case "getAdmGroup":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					spName = "sp_get_AdmGroup";
					break;
				case "getDBGroup":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("grpID");
					spParams.add(new SPparam(param));
					spName = "sp_get_dbGroup";
					break;
				case "getBack":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_btb";
					break;
				case "getFtp":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_ftp";
					break;
				case "getOsp":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_osp";
					break;
				case "getMov":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_mov";
					break;
				case "getEtb":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_exp";
					break;
				case "getLtb":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_ltb";
					break;
				case "getOspParam":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_ospParams";
					break;
				case "getMovParam":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_movMatch";
					break;
				case "getEtbParam":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_expMatch";
					break;
				case "getLtbParam":
					joParams = (JSONObject) params;
					logger.info("Parametros recibidos: "+joParams.toString());
					param = joParams.getString("procID");
					spParams.add(new SPparam(param));
					spName = "sp_get_ltbMatch";
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
					spParams.add(new SPparam(joParams.getString("grpID")));
					spParams.add(new SPparam(joParams.getString("uStatus")));
					spParams.add(new SPparam(joParams.getString("cliID")));
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
				case "getCmbClient":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("cliID")));
					spName = "sp_get_cmbCli";
					break;
				case "getCmbCategory":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("catID")));
					spName = "sp_get_cmbCategory";
					break;
				case "getCmbUser":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("usrID")));
					spName = "sp_get_cmbUser";
					break;
				case "getCmbDbase":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("dbID")));
					spName = "sp_get_cmbDbase";
					break;
				case "getCmbServer":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("srvID")));
					spName = "sp_get_cmbServer";
					break;
				case "getCmbFtp":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbFtp";
					break;
				case "getCmbMov":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbMov";
					break;
				case "getCmbMovParams":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbMovParams";
					break;
				case "getCmbOsp":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbOsp";
					break;
				case "getCmbOspParams":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbOspParams";
					break;
				case "getCmbEtb":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbEtb";
					break;
				case "getCmbEtbParams":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbEtbParams";
					break;
				case "getCmbLtb":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbLtb";
					break;
				case "getCmbLtbParams":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbLtbParams";
					break;
				case "getCmbBack":
					joParams = (JSONObject) params;
					spParams.add(new SPparam(joParams.getString("procID")));
					spName = "sp_get_cmbBack";
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
					
				} else {
					throw new Exception("getProcDependences(): No es posible ejecutar query: "+vSql);
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
	
	public List<Map<String,Object>> getAgeStatDetails(Object params) throws Exception {
		try {
			Map<String,Object> cols = new HashMap<>();
			List<Map<String,Object>> lstRows = new ArrayList<>();
			List<SPparam> spParams = new ArrayList<>();
			
			//Parse params
			JSONObject joParams = (JSONObject) params;
			
			//Add SP Params
			spParams.add(new SPparam(joParams.getInt("day")));
			spParams.add(new SPparam(0));
			spParams.add(new SPparam(0));
			
			String spName = "sp_get_ageStatDetails";
			
			dbConn.open();
			if (dbConn.isConnected()) {
				if (dbConn.executeProcedure(spName, spParams)) {
					ResultSet rs = dbConn.getSpResult();
					ResultSetMetaData rsm = rs.getMetaData();
					
					while (rs.next()) {
						cols = new HashMap<>();

						for (int i=1; i<=rsm.getColumnCount(); i++) {
							switch(rsm.getColumnType(i)) {
							case java.sql.Types.VARCHAR:
								cols.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								break;
							case java.sql.Types.INTEGER:
								cols.put(rsm.getColumnLabel(i), rs.getInt(rsm.getColumnLabel(i)));
								break;
							default:
								cols.put(rsm.getColumnLabel(i), rs.getString(rsm.getColumnLabel(i)));
								break;
							}
						}
						
						lstRows.add(cols);
					}
					rs.close();
				}
				dbConn.close();
			} 			

			return lstRows;
			
		} catch (Exception e) {
			throw new Exception(""+e.getMessage());
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
	
	public List<Map<String,Object>> getAgeGroupDayHourAlert2(int vDay, int vHour) throws Exception {
		try {
			Map<String,Object> cols = new HashMap<>();
			List<Map<String,Object>> lstRows = new ArrayList<>();
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_ageGroupDayHourStat3("+vDay+","+vHour+")";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					
					while (rs.next()) {
						
						cols = new HashMap<>();
						
						String campo1 = rs.getString("campo1");
						String campo2 = rs.getString("campo2");
						String grpID = rs.getString("grpID");
						String numSecExec = rs.getString("numSecExec");
						String grpDesc = rs.getString("grpDesc");
						String horDesc = rs.getString("horDesc");
						String cliDesc = rs.getString("cliDesc");
						int maxTimeExec = rs.getInt("maxTimeExec");
						String typeBalance = rs.getString("typeBalance");
						String typeRequest = rs.getString("typeRequest");
						String uStatus = Objects.isNull(rs.getString("uStatus")) ? "NULL":rs.getString("uStatus") ;
						int errCode = Objects.isNull(rs.getInt("errCode")) ? 0:rs.getInt("errCode") ;
						String errMesg = Objects.isNull(rs.getString("errMesg")) ? "NULL":rs.getString("errMesg") ;
						String fecUpdate = Objects.isNull(rs.getDate("fecUpdate")) ? "NULL":rs.getString("fecUpdate") ;
						
						cols.put("campo1", campo1);
						cols.put("campo2", campo2);
						cols.put("grpID", grpID);
						cols.put("numSecExec", numSecExec);
						cols.put("grpDesc", grpDesc);
						cols.put("horDesc", horDesc);
						cols.put("cliDesc", cliDesc);
						cols.put("maxTimeExec", maxTimeExec);
						cols.put("typeBalance", typeBalance);
						cols.put("typeRequest", typeRequest);
						cols.put("uStatus", uStatus);
						cols.put("errCode", errCode);
						cols.put("errMesg", errMesg);
						cols.put("fecUpdate", fecUpdate);
						
						lstRows.add(cols);
					}
				}
			}
			
			return lstRows;
		} catch (Exception e) {
			throw new Exception("getAgeGroupDayHourAlert2(): "+e.getMessage());
		}
	}

	public List<Map<String,Object>> getActualGroupDayHour(int vDay, int vHour) throws Exception {
		try {
			Map<String,Object> cols = new HashMap<>();
			List<Map<String,Object>> lstRows = new ArrayList<>();
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_get_actualGroupDayHour("+vDay+","+vHour+")";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					
					while (rs.next()) {
						
						cols = new HashMap<>();
						
						String campo1 = rs.getString("campo1");
						String campo2 = rs.getString("campo2");
						String grpID = rs.getString("grpID");
						String numSecExec = rs.getString("numSecExec");
						String grpDesc = rs.getString("grpDesc");
						String horDesc = rs.getString("horDesc");
						String cliDesc = rs.getString("cliDesc");
						int maxTimeExec = rs.getInt("maxTimeExec");
						String typeBalance = rs.getString("typeBalance");
						String typeRequest = rs.getString("typeRequest");
						
						cols.put("campo1", campo1);
						cols.put("campo2", campo2);
						cols.put("grpID", grpID);
						cols.put("numSecExec", numSecExec);
						cols.put("grpDesc", grpDesc);
						cols.put("horDesc", horDesc);
						cols.put("cliDesc", cliDesc);
						cols.put("maxTimeExec", maxTimeExec);
						cols.put("typeBalance", typeBalance);
						cols.put("typeRequest", typeRequest);
						
						lstRows.add(cols);
					}
				}
			}
			
			return lstRows;
		} catch (Exception e) {
			throw new Exception("getActualGroupDayHour(): "+e.getMessage());
		}
	}

	public List<Object> getAgeGroupStat() throws Exception {

		Map<String, AgeGroupStat> mapAgeGroupStat = new TreeMap<>();
		
		try {
			
			int initDay;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				
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
						
						if (!uStatus.equals("SUCCESS")) {
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
							//ags.setAlerts(ags.getAlerts()+numGroups);
						}
						
						ags.setnDay(day);
						ags.setnHour(hour);
						
						mapAgeGroupStat.put(key, ags);
						
						//System.out.println(mylib.serializeObjectToJSon(mapAgeGroupStat.get(key), false));
						
					}
					//System.out.println(dayOfMonth);
					
				} else {
					throw new Exception("Nos es posible ejecutar sp sp_get_ageGroupStat()");
				}
				dbConn.close();
			}
			
			int itDay = 0;
			Map<String, AgeGroupStat> mapAgeGrStatFinal00 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal01 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal02 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal03 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal04 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal05 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal06 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal07 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal08 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal09 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal10 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal11 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal12 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal13 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal14 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal15 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal16 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal17 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal18 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal19 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal20 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal21 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal22 = new TreeMap<>();
			Map<String, AgeGroupStat> mapAgeGrStatFinal23 = new TreeMap<>();
			
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGroupStat.entrySet()) {
				int Day = Integer.valueOf(entry.getKey().split(":")[0]);
				int Hour = Integer.valueOf(entry.getKey().split(":")[1]);
				
				if (itDay!=Day) {
					for (int i=0; i<=23; i++) {
						Map<String, AgeGroupStat> mapAgeGrStatFinal = new TreeMap<>();
						String getKey = entry.getKey().split(":")[0]+":"+String.format("%02d", i);
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
						
						switch(i) {
							case 0:
								mapAgeGrStatFinal00.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 1:
								mapAgeGrStatFinal01.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 2:
								mapAgeGrStatFinal02.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 3:
								mapAgeGrStatFinal03.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 4:
								mapAgeGrStatFinal04.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 5:
								mapAgeGrStatFinal05.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 6:
								mapAgeGrStatFinal06.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 7:
								mapAgeGrStatFinal07.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 8:
								mapAgeGrStatFinal08.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 9:
								mapAgeGrStatFinal09.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 10:
								mapAgeGrStatFinal10.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 11:
								mapAgeGrStatFinal11.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 12:
								mapAgeGrStatFinal12.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 13:
								mapAgeGrStatFinal13.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 14:
								mapAgeGrStatFinal14.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 15:
								mapAgeGrStatFinal15.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 16:
								mapAgeGrStatFinal16.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 17:
								mapAgeGrStatFinal17.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 18:
								mapAgeGrStatFinal18.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 19:
								mapAgeGrStatFinal19.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 20:
								mapAgeGrStatFinal20.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 21:
								mapAgeGrStatFinal21.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 22:
								mapAgeGrStatFinal22.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
							case 23:
								mapAgeGrStatFinal23.put(getKey,mapAgeGrStatFinal.get(getKey));
								break;
						}
					}
					itDay = Day;
				}
			}
			List<Map<String,Object>> lstObjects;
			List<Object> lstListObjects = new ArrayList<>();
			
			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal00.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal01.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal02.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal03.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal04.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal05.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal06.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal07.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal08.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal09.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal10.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal11.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal12.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal13.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal14.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal15.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal16.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal17.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal18.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal19.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal20.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal21.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal22.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

			lstObjects = new ArrayList<>();
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal23.entrySet()) {
				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
				Map<String,Object> cols = new HashMap<>();
				cols = jo.toMap();
				lstObjects.add(cols);
			}
			lstListObjects.add(lstObjects);

//			Map<String, AgeGroupStat> mapAgeGrStatFinal = new TreeMap<>();
//			
//			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGroupStat.entrySet()) {
//				int Day = Integer.valueOf(entry.getKey().split(":")[0]);
//				
//				if (itDay!=Day) {
//					for (int i=0; i<=23; i++) {
//						String getKey = entry.getKey().split(":")[0]+":"+String.format("%02d", i);
//						System.out.println(getKey);
//						if (!mapAgeGroupStat.containsKey(getKey)) {
//							AgeGroupStat ags = new AgeGroupStat();
//							ags.setAlerts(0);
//							ags.setnDay(Day);
//							ags.setnGroups(0);
//							ags.setnHour(i);
//							mapAgeGrStatFinal.put(getKey, ags);
//						} else {
//							mapAgeGrStatFinal.put(getKey, mapAgeGroupStat.get(getKey));
//						}
//					}
//					itDay = Day;
//				}
//			}
//			
//			List<Map<String,Object>> lstObjects = new ArrayList<>();
//			
//			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal.entrySet()) {
//				JSONObject jo = new JSONObject(mylib.serializeObjectToJSon(entry.getValue(), false));
//				Map<String,Object> cols = new HashMap<>();
//				cols = jo.toMap();
//				lstObjects.add(cols);
//			}
			
			return lstListObjects;
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
						pg.setProcDesc(rs.getString("PROCDESC"));
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

	public Map<String, PGPending> addGroupActiveManualActual(String grpID, String numSecExec) {
		final String module = "addGroupActiveManualActual()";
		String logmsg = module + " - ";
		Map<String, PGPending> mp = new HashMap<>();
		
		try {
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_add_groupActiveManualActual('"+ grpID +"','"+numSecExec+"')";
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
						pg.setProcDesc(rs.getString("PROCDESC"));
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

	public Map<String, PGPending> addProcActiveManualActual(String grpID, String numSecExec, String procID) {
		final String module = "addProcActiveManualActual()";
		String logmsg = module + " - ";
		Map<String, PGPending> mp = new HashMap<>();
		
		try {
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_add_procActiveManualActual('"+ grpID +"','"+numSecExec+"','"+procID+"')";
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
						pg.setProcDesc(rs.getString("PROCDESC"));
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

	public Map<String, PGPending> addProcActiveManual(String grpID, String procID) {
		final String module = "addProcActiveManual()";
		String logmsg = module + " - ";
		Map<String, PGPending> mp = new HashMap<>();
		
		try {
			logger.info(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				String vSql = "call srvConf.sp_add_procActiveManual('"+ grpID +"','"+procID+"')";
				logger.info(logmsg+"Ejecutando: "+vSql);
				if (dbConn.executeQuery(vSql)) {
					logger.info(logmsg+"Ejecucion Exitosa");
					logger.info(logmsg+"Recuperando proceso asociado...");
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						PGPending pg = new PGPending();
						pg.setFecIns(mylib.getDate());
						pg.setGrpID(rs.getString("GRPID"));
						pg.setnOrder(rs.getInt("NORDER"));
						pg.setNumSecExec(rs.getString("NUMSECEXEC"));
						pg.setProcID(rs.getString("PROCID"));
						pg.setProcDesc(rs.getString("PROCDESC"));
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
						pg.setProcDesc(rs.getString("PROCDESC"));
						pg.setStatus(rs.getString("STATUS"));
						pg.setTypeProc(rs.getString("TYPEPROC"));
						pg.setCliID(rs.getString("CLIID"));
						pg.setCliDesc(rs.getString("CLIDESC"));
						pg.setTypeExec(rs.getString("TYPEEXEC"));
						pg.setGrpDesc(rs.getString("GRPDESC"));
						
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
			
			logger.debug(logmsg+"Conectando a Metadata...");
			dbConn.open();
			if (dbConn.isConnected()) {
				logger.debug(logmsg+"Conexión establecida a Metadata!");
				switch(procType) {
					case "FTP":
						Ftp ftp = new Ftp();
						
						vSql = "call sp_get_ftp('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase Ftp()");
								ftp = (Ftp) mylib.serializeJSonStringToObject(resp, Ftp.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase Ftp()");
																	
								params = ftp;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}
							
						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;
					case "MOV":
						Mov mov = new Mov();
						List<MovMatch> lstMovMatch = new ArrayList<>();
						
						vSql = "call sp_get_mov('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase Mov()");
								mov = (Mov) mylib.serializeJSonStringToObject(resp, Mov.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase Mov()");
								
								
								//Busca Match de Campos

								String vSql2 = "call sp_get_movMatch('"+procID+"')";
								logger.debug(logmsg+"Ejecutando query: "+vSql2);
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										MovMatch movMatch = new MovMatch();
										resp = rs2.getString("resp");
										logger.debug(logmsg+"Respuesta de la query: "+resp);
										
										logger.debug(logmsg+"Serializando respuesta en objeto clase MovMatch()");
										movMatch = (MovMatch) mylib.serializeJSonStringToObject(resp, MovMatch.class);
								
										logger.debug(logmsg+"Se ha serializado correctamente el objeto clase MovMatch()");
										
										lstMovMatch.add(movMatch);
									}
									rs2.close();
									
									if (lstMovMatch.size()==0) {
										throw new Exception("getProcessParam(): No es posible recuperar match de campos del proceso: "+procID);
									}
									
								} else {
									throw new Exception("getProcessParam(): Error ejecutando query: "+vSql2);
								}
								
								//Asignacion de Lista de Campos al Mov
								mov.setLstMovMatch(lstMovMatch);
								
								//Asigancion de objeto de respuesta
								params = mov;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}

						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;
					case "OSP":
						Osp osp = new Osp();
						Map<String, OspParam> mapOspParam = new TreeMap<>();
						
						vSql = "call sp_get_osp('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase Osp()");
								osp = (Osp) mylib.serializeJSonStringToObject(resp, Osp.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase Osp()");
								
								//Busca Parametros del OSP
								String vSql2 = "call sp_get_ospParams('"+procID+"')";
								logger.debug(logmsg+"Ejecutando query: "+vSql2);
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										OspParam ospParam = new OspParam();
										resp = rs2.getString("resp");
										logger.debug(logmsg+"Respuesta de la query: "+resp);
										
										logger.debug(logmsg+"Serializando respuesta en objeto clase OspParams()");
										ospParam = (OspParam) mylib.serializeJSonStringToObject(resp, OspParam.class);
										
										logger.debug(logmsg+"Se ha serializado correctamente el objeto clase OspParams()");
										
										mapOspParam.put(String.format("%03d", ospParam.getOrder()), ospParam);
									}
									rs2.close();
									
								} else {
									throw new Exception("getProcessParam(): Error ejecutando query: "+vSql2);
								}
								
								osp.setMapOspParam(mapOspParam);
								
								params = osp;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}

						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;
					case "ETB":
						ExpTable etb = new ExpTable();
						Map<String, ExpTableParam> mapEtbParam = new TreeMap<>();
						
						vSql = "call sp_get_exp('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase ExpTable()");
								etb = (ExpTable) mylib.serializeJSonStringToObject(resp, ExpTable.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase ExpTable()");
								
								//Busca Parametros del ExpTable
								String vSql2 = "call sp_get_expMatch('"+procID+"')";
								logger.debug(logmsg+"Ejecutando query: "+vSql2);
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										ExpTableParam etbParam = new ExpTableParam();
										resp = rs2.getString("resp");
										logger.debug(logmsg+"Respuesta de la query: "+resp);
										
										logger.debug(logmsg+"Serializando respuesta en objeto clase ExpTableParam()");
										etbParam = (ExpTableParam) mylib.serializeJSonStringToObject(resp, ExpTableParam.class);
										
										logger.debug(logmsg+"Se ha serializado correctamente el objeto clase ExpTableParam()");
										
										mapEtbParam.put(String.format("%03d", etbParam.getEtbOrder()), etbParam);
									}
									rs2.close();
									
									if (mapEtbParam.size()==0) {
										throw new Exception("getProcessParam(): No es posible recuperar parametros del proceso: "+procID);
									}
									
								} else {
									throw new Exception("getProcessParam(): Error ejecutando query: "+vSql2);
								}
								
								etb.setMapEtbParam(mapEtbParam);
								
								params = etb;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}
							
						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;

					case "BTB":
						BackTable btb = new BackTable();
						
						vSql = "call sp_get_btb('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase BackTable()");
								btb = (BackTable) mylib.serializeJSonStringToObject(resp, BackTable.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase BackTable()");
																
								params = btb;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}

						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;

					case "LTB":
						LoadTable ltb = new LoadTable();
						Map<Integer, LoadTableParam> mapLtbParam = new TreeMap<>();
						
						vSql = "call sp_get_ltb('"+procID+"')";
						logger.debug(logmsg+"Ejecutando query: "+vSql);
						if (dbConn.executeQuery(vSql)) {
							ResultSet rs = dbConn.getQuery();
							if (rs.next()) {
								String resp = rs.getString("resp");
								logger.debug(logmsg+"Respuesta de la query: "+resp);
								
								logger.debug(logmsg+"Serializando respuesta en objeto clase LoadTable()");
								ltb = (LoadTable) mylib.serializeJSonStringToObject(resp, LoadTable.class);
								
								logger.debug(logmsg+"Se ha serializado correctamente el objeto clase LoadTable()");
								
								//Busca Parametros del LoadTable
								String vSql2 = "call sp_get_ltbMatch('"+procID+"')";
								logger.debug(logmsg+"Ejecutando query: "+vSql2);
								if (dbConn.executeQuery(vSql2)) {
									ResultSet rs2 = dbConn.getQuery();
									while (rs2.next()) {
										LoadTableParam ltbParam = new LoadTableParam();
										resp = rs2.getString("resp");
										logger.debug(logmsg+"Respuesta de la query: "+resp);
										
										logger.debug(logmsg+"Serializando respuesta en objeto clase LoadTableParam()");
										ltbParam = (LoadTableParam) mylib.serializeJSonStringToObject(resp, LoadTableParam.class);
										
										logger.debug(logmsg+"Se ha serializado correctamente el objeto clase LoadTableParam()");
										
										mapLtbParam.put(ltbParam.getFileLoadOrder(), ltbParam);
									}
									rs2.close();
									
									if (mapLtbParam.size()==0) {
										throw new Exception("getProcessParam(): No es posible recuperar parametros del proceso: "+procID);
									}
									
								} else {
									throw new Exception("getProcessParam(): Error ejecutando query: "+vSql2);
								}
								
								ltb.setMapLtbParam(mapLtbParam);
								
								params = ltb;
							}
							rs.close();
							
							if (mylib.isNullOrEmpty(params)) {
								throw new Exception("getProcessParam(): No hay parametros recuperados del proceso: "+procID);	
							}

						} else {
							throw new Exception("getProcessParam(): Error ejecutando query: "+vSql);
						}
						
						break;
					default:
						throw new Exception("getProcessParam(): Tipo de Proceso "+procType+" no se encuentrs definido");
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
				spParams.add(new SPparam(""));
				
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
