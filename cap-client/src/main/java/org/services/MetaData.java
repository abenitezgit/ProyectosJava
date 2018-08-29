/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.utilities.MyLogger;

import com.api.MysqlAPI;
import com.api.OracleAPI;
import com.model.SPparam;
//import com.api.SqlAPI;


/**
 *
 * @author andresbenitez
 */
public class MetaData {
	Logger logger;
    MysqlAPI myConn;
    OracleAPI oraConn;
    SqlAPI sqlConn;
    String dbType;
    MyLogger mylog;
    
    public MetaData (String dbType, MyLogger mylog) {
        this.dbType = dbType;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
    }
    
    public void open(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int dbTimeOut) throws Exception{
        switch (dbType) {
            case "mySQL":
                try {
                    myConn = new MysqlAPI(dbHost, dbName, dbPort, dbUser, dbPass, dbTimeOut);
                    myConn.open();
                } catch (Exception e) {
                	//mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
                	throw new Exception(e.getMessage());
                }
                break;
            case "ORA11":
                try {
                    oraConn = new OracleAPI(dbHost, dbName, dbPort, dbUser, dbPass, dbTimeOut);
                    oraConn.open();
                } catch (Exception e) {
                	//mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
                	throw new Exception(e.getMessage());
                }
            case "ORA":
                try {
                    oraConn = new OracleAPI(dbHost, dbName, dbPort, dbUser, dbPass, dbTimeOut);
                    oraConn.open();
                } catch (Exception e) {
                	//mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
                	throw new Exception(e.getMessage());
                }
                break;
            case "SQL":
                try {
                	sqlConn = new SqlAPI(dbHost, dbName, dbPort, dbUser, dbPass, dbTimeOut);
                	sqlConn.open();
                } catch (Exception e) {
                	//mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
                	throw new Exception(e.getMessage());
                }
                break;
            default:
                break;
        }
    }
    
    public Connection getConnection() {
    	switch (dbType) {
    		case "mySQL":
    			return myConn.getConnection();
    		case "ORA11":
    			return oraConn.getConnection();
    		case "ORA":
    			return oraConn.getConnection();
    		case "SQL":
    			return sqlConn.getConnection();
			default:
				return null;
    	} 
    }
    
    public boolean isConnected() throws Exception {
    	try {
	        switch (dbType) {
	            case "mySQL":
	                return myConn.isConnected();
	            case "ORA11":
	                return oraConn.isConnected();
	            case "ORA":
	                return oraConn.isConnected();
	            case "SQL":
	                return sqlConn.isConnected();
	            default:
	                return false;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean executeQuery(String vSQL) throws Exception {
    	try {
	        switch (dbType) {
	        	case "mySQL":
	    			return myConn.executeQuery(vSQL);
	        	case "ORA11":
	    			return oraConn.executeQuery(vSQL);
	        	case "ORA":
	    			return oraConn.executeQuery(vSQL);
	        	case "SQL":
	    			return sqlConn.executeQuery(vSQL);
	    		default:
	    			return false;
	        }
    	}
        catch (Exception e) {
        	throw new Exception(e.getMessage());
    	}
    }
    
    public ResultSet getQuery() throws Exception{
    	try {
	        switch (dbType) {
	            case "mySQL":
                    return myConn.getQuery();
	            case "ORA11":
                    return oraConn.getQuery();
	            case "ORA":
                    return oraConn.getQuery();
	            case "SQL":
                    return sqlConn.getQuery();
	            default:
	                return null;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public int executeUpdate(String upd) throws Exception{
    	try {
	        switch (dbType) {
	            case "mySQL":
                    return myConn.executeUpdate(upd);
	            case "ORA11":
                    return oraConn.executeUpdate(upd);
	            case "ORA":
                    return oraConn.executeUpdate(upd);
	            case "SQL":
                    return sqlConn.executeUpdate(upd);
	            default:
	                return 0;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean isExistRows() throws Exception {
    	try {
	        switch (dbType) {
            case "mySQL":
                return myConn.isExistRows();
            case "ORA11":
                return oraConn.isExistRows();
            case "ORA":
                return oraConn.isExistRows();
            case "SQL":
                return sqlConn.isExistRows();
            default:
                return false;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    
    public void close() throws Exception {
    	try {
	        switch (dbType) {
	            case "mySQL":
	                myConn.close();
	                break;
	            case "ORA11":
	                oraConn.close();
	                break;
	            case "ORA":
	                oraConn.close();
	                break;
	            case "SQL":
	                sqlConn.close();
	                break;
	            default:
	                break;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean executeProcedure(String ospOwner, String spName, List<SPparam> spParams) throws Exception {
    	try {
    		boolean response = false;
    		String storeProc = "";
    		
	        switch (dbType) {
	            case "mySQL":
	            	response = false;
	            	break;
	            case "ORA11":
	            	storeProc = spName;
	            	if (!ospOwner.isEmpty()) {
	            		storeProc = ospOwner +"."+ spName;
	            	} 
	            	
	            	response = oraConn.executeProcedure(storeProc, spParams); 
	            	break;
	            case "ORA":
	            	storeProc = spName;
	            	if (!ospOwner.isEmpty()) {
	            		storeProc = ospOwner +"."+ spName;
	            	} 
	            	
	            	response = oraConn.executeProcedure(storeProc, spParams); 
	            	break;
	            case "SQL":
	            	storeProc = spName;
	            	if (!ospOwner.isEmpty()) {
	            		storeProc = ospOwner +"."+ spName;
	            	} 
	            	
	            	response = oraConn.executeProcedure(storeProc, spParams); 
	            	break;
	            default:
	                response = false;
	        }

	        return response;
    	} catch (Exception e) {
    		throw new Exception(""+e.getMessage());
    	}
    }
}
