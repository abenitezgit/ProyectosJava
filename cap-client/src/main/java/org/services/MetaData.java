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


/**
 *
 * @author andresbenitez
 */
public class MetaData {
	Logger logger;
    MysqlAPI myConn;
    OracleAPI oraConn;
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
	            default:
	                break;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean executeProcedure(String ospOwner, String spName, List<String> spParams) throws Exception {
    	try {
    		boolean response = false;
    		
	        switch (dbType) {
	            case "mySQL":
	            	response = false;
	            	break;
	            case "ORA11":
	            	String storeProc = spName;
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
