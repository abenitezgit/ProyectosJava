package org.dataAccess;

import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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
	
	/**
	 * Private Methods
	 */

}
